/*
 * Copyright © 2021. The Android Open Source Project
 *
 * @author Kai Cilliers
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sunrisekcdeveloper.showtracker.features.watchlist.domain.usecase

import com.sunrisekcdeveloper.showtracker.features.watchlist.application.UpdateShowProgressUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository.RepositoryWatchlistContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.UpdateShowAction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
class UpdateShowProgressUseCase(
    private val repo: RepositoryWatchlistContract
) : UpdateShowProgressUseCaseContract {
    // todo this implementation and accompanying repository is bad
    //  requires refactoring and streamline design
    //  like really, this is just wrong
    //  i mean you should be doing these database operations in transactions
    override suspend fun invoke(action: UpdateShowAction) {
        when (action) {
            is UpdateShowAction.IncrementEpisode -> {
                val show = repo.currentWatchlistShow(action.showId)

                repo.markEpisodeAsWatched(
                    show.id,
                    show.currentSeasonNumber,
                    show.currentEpisodeNumber
                )
                repo.insertNewWatchlistEpisode(
                    show.id,
                    show.currentSeasonNumber,
                    show.currentEpisodeNumber + 1
                )
                repo.incrementSeasonCurrentEpisode(
                    show.id,
                    show.currentSeasonNumber
                )

                repo.incrementWatchlistShowCurrentEpisode(show.id)
            }
            is UpdateShowAction.CompleteSeason -> {
                val justShow = repo.currentShow(action.showId)
                Timber.d("I got the show: ${justShow}")
                val show = repo.currentWatchlistShow(action.showId)
                Timber.d("Watchlist data: $show")

                if (justShow.seasonTotal == show.currentSeasonNumber) {
                    setShowUpToDate(show.id)
                } else {

                    val firstEpisodeInSeason = repo.firstEpisodeFromSeason(show.id, show.currentSeasonNumber+1)

                    repo.markEpisodeAsWatched(
                        show.id,
                        show.currentSeasonNumber,
                        show.currentEpisodeNumber
                    )
                    repo.updateSeasonAsWatched(
                        show.id,
                        show.currentSeasonNumber
                    )

                    repo.insertNewWatchlistEpisode(
                        show.id,
                        show.currentSeasonNumber+1,
                        1
                    )
                    repo.insertNewWatchlistSeason(
                        show.id,
                        show.currentSeasonNumber + 1,
                        1
                    )
                    repo.updateWatchlistShowEpisodeAndSeason(
                        show.id,
                        show.currentSeasonNumber + 1,
                        firstEpisodeInSeason.number
                    )
                }

            }
            is UpdateShowAction.UpToDateWithShow -> {
                setShowUpToDate(action.showId)
            }
        }
    }

    private suspend fun setShowUpToDate(showId: String) {
        val show = repo.currentWatchlistShow(showId)
        repo.markEpisodeAsWatched(
            show.id,
            show.currentSeasonNumber,
            show.currentEpisodeNumber
        )
        repo.updateSeasonAsWatched(
            show.id,
            show.currentSeasonNumber
        )
        repo.updateWatchlistShowAsUpToDate(showId)
    }
}