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

import com.sunrisekcdeveloper.showtracker.di.RepositoryModule
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.RepoWatchlist
import com.sunrisekcdeveloper.showtracker.features.watchlist.application.UpdateShowProgressUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository.RepositoryWatchlistContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.UpdateShowAction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
class UpdateShowProgressUseCase(
    @RepoWatchlist private val repo: RepositoryWatchlistContract
) : UpdateShowProgressUseCaseContract {
    // todo this implementation and accompanying repository is bad
    //  requires refactoring and streamline design
    override suspend fun invoke(action: UpdateShowAction) {
        Timber.e("use case $action")
        when (action) {
            is UpdateShowAction.IncrementEpisode -> {
                val show = repo.currentWatchlistShow(action.showId)

                Timber.e("marking episode1")
                repo.markEpisodeAsWatched(
                    show.id,
                    show.currentSeasonNumber,
                    show.currentEpisodeNumber
                )
                Timber.e("inserting new episode")
                repo.insertNewWatchlistEpisode(
                    show.id,
                    show.currentSeasonNumber,
                    show.currentEpisodeNumber + 1
                )
                Timber.e("incrementing season current episode")
                repo.incrementSeasonCurrentEpisode(
                    show.id,
                    show.currentSeasonNumber
                )

                Timber.e("incrementing show current episode")
                repo.incrementWatchlistShowCurrentEpisode(show.id)
            }
            is UpdateShowAction.CompleteSeason -> {
                val justShow = repo.currentShow(action.showId)
                val show = repo.currentWatchlistShow(action.showId)
                if (justShow.seasonTotal == show.currentSeasonNumber) {
                    noDups(show.id)
                } else {

                    Timber.e("marking episode as watched...")
                    repo.markEpisodeAsWatched(
                        show.id,
                        show.currentSeasonNumber,
                        show.currentEpisodeNumber
                    )
                    Timber.e("marking season as watched...")
                    repo.updateSeasonAsWatched(
                        show.id,
                        show.currentSeasonNumber
                    )

                    Timber.e("inserting next episode...")
                    repo.insertNewWatchlistEpisode(
                        show.id,
                        show.currentSeasonNumber+1,
                        1
                    )
                    Timber.e("inserting next season...")
                    repo.insertNewWatchlistSeason(
                        show.id,
                        show.currentSeasonNumber + 1,
                        1
                    )
                    Timber.e("updating show episode and season...")
                    repo.updateWatchlistShowEpisodeAndSeason(
                        show.id,
                        show.currentSeasonNumber + 1,
                        1
                    )
                }

            }
            is UpdateShowAction.UpToDateWithShow -> {
                noDups(action.showId)
            }
        }
    }

    private suspend fun noDups(showId: String) {
        val show = repo.currentWatchlistShow(showId)
        Timber.e("marking episode as watched...")
        repo.markEpisodeAsWatched(
            show.id,
            show.currentSeasonNumber,
            show.currentEpisodeNumber
        )
        Timber.e("marking season as watched...")
        repo.updateSeasonAsWatched(
            show.id,
            show.currentSeasonNumber
        )
        Timber.e("marking show as up to date...")
        repo.updateWatchlistShowAsUpToDate(showId)
    }
}