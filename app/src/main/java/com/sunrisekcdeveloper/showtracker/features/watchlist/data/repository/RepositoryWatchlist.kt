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

package com.sunrisekcdeveloper.showtracker.features.watchlist.data.repository

import androidx.room.withTransaction
import com.sunrisekcdeveloper.models.ActionRepositoryMovie
import com.sunrisekcdeveloper.showtracker.common.util.Resource
import com.sunrisekcdeveloper.showtracker.common.TrackerDatabase
import com.sunrisekcdeveloper.showtracker.common.util.asUIModelWatchlistMovie
import com.sunrisekcdeveloper.showtracker.common.util.asUIModelWatchlistShow
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.FilterMovies
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.FilterShows
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.*
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistMovie.Companion
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository.RepositoryWatchlistContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.UIModelWatchlisMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.UpdateShowAction
import com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.UIModelWatchlistShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

// todo class too big - refactor logic
class RepositoryWatchlist(
    private val database: TrackerDatabase
) : RepositoryWatchlistContract {

    private val daoWatchlistMovie = database.watchlistMovieDao()

    override suspend fun updateShowProgress(action: UpdateShowAction) {
        when (action) {
            is UpdateShowAction.IncrementEpisode -> {
                currentWatchlistShow(action.showId).apply {
                    incrementEpisode(this)
                }
            }
            is UpdateShowAction.CompleteSeason -> {
               finishSeason(action.showId)
            }
            is UpdateShowAction.UpToDateWithShow -> {
                setShowUpToDate(action.showId)
            }
        }
    }

    override fun watchlistMovies(filterOption: FilterMovies): Flow<Resource<List<UIModelWatchlisMovie>>> {
        return database.watchlistMovieDao().distinctWithDetailsFlow(filterOption).map {
            Resource.Success(it.map { it.asUIModelWatchlistMovie() })
        }
    }

    override fun watchlistShows(filterOption: FilterShows): Flow<Resource<List<UIModelWatchlistShow>>> {
        return database.watchlistShowDao().distinctWithDetailsFlow(filterOption).map { list ->
            Resource.Success(list.map {
                val lastEpisodeInSeason : EntityEpisode? = database.episodeDao().lastInSeason(it.details.id, it.status.currentSeasonNumber)
                it.asUIModelWatchlistShow(lastEpisodeInSeason?.number ?: -1)
            })
        }
    }

    private suspend fun currentShow(showId: String): EntityShow {
        return database.showDao().withId(showId)
    }

    private suspend fun finishSeason(showId: String) {
        database.withTransaction {
            val show = currentShow(showId)
            val watchlistShow = currentWatchlistShow(showId)

            if (show.seasonTotal == watchlistShow.currentSeasonNumber) {
                setShowUpToDate(showId)
            } else {
                watchlistShow.apply {

                    /// mark current episode as watched
                    database.watchlistEpisodeDao().withId(id, currentEpisodeNumber, currentSeasonNumber)?.let {
                        database.watchlistEpisodeDao().update(
                            it.copy(
                                watched = true,
                                dateWatched = System.currentTimeMillis(),
                                lastUpdated = System.currentTimeMillis()
                            )
                        )
                    }

                    // update season as watched
                    database.watchlistSeasonDao().withId(id, currentSeasonNumber).apply {
                        database.watchlistSeasonDao().update(
                            copy(
                                completed = true,
                                dateCompleted = System.currentTimeMillis(),
                                lastUpdated = System.currentTimeMillis()
                            )
                        )
                    }

                    // insert new watchlist episode
                    database.watchlistEpisodeDao().insert(
                        EntityWatchlistEpisode.notWatchedFrom(
                            id, currentSeasonNumber, currentEpisodeNumber + 1
                        )
                    )

                    // insert new watchlist season
                    database.watchlistSeasonDao().insert(
                        EntityWatchlistSeason.partialFrom(
                            id, currentSeasonNumber + 1, 1
                        )
                    )

                    // get first episode in new season
                    val firstEpisodeInSeason = database.episodeDao().firstInSeason(id, currentSeasonNumber + 1)

                    // update watchlist show
                    val episode = database.episodeDao().withId(id, currentSeasonNumber + 1, firstEpisodeInSeason.number)
                    val season = database.seasonDao().withId(id, currentSeasonNumber + 1)

                    // todo if null objects are returned then try fetch from network and if failed then you need to handle
                    //  consider making room return nullable objects to form handle these null cases
                    // todo some shows return seasons which have zero episodes... handle that case (example is Simpsons last two seasons)
                    database.watchlistShowDao().update(
                        copy(
                            currentEpisodeNumber = episode?.number ?: 1,
                            currentEpisodeName = episode?.name ?: "Episode 1 :)",
                            currentSeasonNumber = season.number,
                            currentSeasonEpisodeTotal = season.episodeTotal,
                            lastUpdated = System.currentTimeMillis()
                        )
                    )
                }
            }
        }
    }

    private suspend fun incrementEpisode(show: EntityWatchlistShow) {
        database.withTransaction {
            show.apply {
                // mark current episode as watched
                database.watchlistEpisodeDao().withId(id, currentEpisodeNumber, currentSeasonNumber)?.let {
                    database.watchlistEpisodeDao().update(
                        it.copy(
                            watched = true,
                            dateWatched = System.currentTimeMillis(),
                            lastUpdated = System.currentTimeMillis()
                        )
                    )
                }

                // insert new watchlist episode
                database.watchlistEpisodeDao().insert(
                    EntityWatchlistEpisode.notWatchedFrom(
                        id, currentSeasonNumber, currentEpisodeNumber + 1
                    )
                )

                // increment watchlist season current episode
                database.watchlistSeasonDao().withId(id, currentSeasonNumber).apply {
                    database.watchlistSeasonDao().update(
                        copy(
                            currentEpisode = currentEpisode + 1,
                            lastUpdated = System.currentTimeMillis()
                        )
                    )
                }

                // increment watchlist show current episode
                database.watchlistShowDao().withId(id).apply {
                    val nextEpisode = database.episodeDao().withId(
                        id, currentSeasonNumber, currentEpisodeNumber + 1
                    )
                    database.watchlistShowDao().update(
                        copy(
                            currentEpisodeNumber = nextEpisode?.number ?: -1,
                            currentEpisodeName = nextEpisode?.name ?: "oops",
                            lastUpdated = System.currentTimeMillis()
                        )
                    )
                }
            }
        }
    }

    override suspend fun updateWatchlistShowAsUpToDate(showId: String) {
        val show = database.watchlistShowDao().withId(showId)
        database.watchlistShowDao().update(
            show.copy(
                upToDate = true,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    private suspend fun currentWatchlistShow(showId: String): EntityWatchlistShow {
        return database.watchlistShowDao().withId(showId)
    }

    private suspend fun setShowUpToDate(showId: String) {
        val show = currentWatchlistShow(showId)
        markEpisodeAsWatched(
            show.id,
            show.currentSeasonNumber,
            show.currentEpisodeNumber
        )
        updateSeasonAsWatched(
            show.id,
            show.currentSeasonNumber
        )
        updateWatchlistShowAsUpToDate(showId)
    }

    private suspend fun markEpisodeAsWatched(showId: String, season: Int, episode: Int) {
        val watchlistEpisode = database.watchlistEpisodeDao().withId(showId, episode, season)

        @Suppress("UNNECESSARY_SAFE_CALL")
        watchlistEpisode?.let {
            database.watchlistEpisodeDao().update(
                watchlistEpisode.copy(
                    watched = true,
                    dateWatched = System.currentTimeMillis(),
                    lastUpdated = System.currentTimeMillis()
                )
            )
        }
    }

    private suspend fun updateSeasonAsWatched(showId: String, season: Int) {
        val watchlistSeason = database.watchlistSeasonDao().withId(showId, season)
        database.watchlistSeasonDao().update(
            watchlistSeason.copy(
                completed = true,
                dateCompleted = System.currentTimeMillis(),
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    override suspend fun submitMovieAction(action: ActionRepositoryMovie) {
        database.withTransaction {
            when (action) {
                is ActionRepositoryMovie.Add -> {
                    if (daoWatchlistMovie.exist(action.id)) {
                        daoWatchlistMovie.update(
                            daoWatchlistMovie.withId(action.id).copy(
                                deleted = false,
                                dateLastUpdated = System.currentTimeMillis()
                            )
                        )
                    } else {
                        daoWatchlistMovie.insert(EntityWatchlistMovie.unwatchedFrom(action.id))
                    }
                }
                is ActionRepositoryMovie.Remove -> {
                    if (daoWatchlistMovie.exist(action.id)) {
                        daoWatchlistMovie.update(
                            daoWatchlistMovie.withId(action.id).copy(
                                deleted = true,
                                dateDeleted = System.currentTimeMillis(),
                                dateLastUpdated = System.currentTimeMillis()
                            )
                        )
                    }
                }
                is ActionRepositoryMovie.Watch -> {
                    if (daoWatchlistMovie.exist(action.id)) {
                        daoWatchlistMovie.update(
                            daoWatchlistMovie.withId(action.id).copy(
                                deleted = false,
                                watched = true,
                                dateWatched = System.currentTimeMillis(),
                                dateLastUpdated = System.currentTimeMillis()
                            )
                        )
                    } else {
                        daoWatchlistMovie.insert(EntityWatchlistMovie.watchedFrom(action.id))
                    }
                }
                is ActionRepositoryMovie.Unwatch -> {
                    if (daoWatchlistMovie.exist(action.id)) {
                        daoWatchlistMovie.update(
                            daoWatchlistMovie.withId(action.id).copy(
                                watched = false,
                                dateLastUpdated = System.currentTimeMillis()
                            )
                        )
                    } else {
                        daoWatchlistMovie.insert(EntityWatchlistMovie.unwatchedFrom(action.id))
                    }
                }
            }
        }
    }
}