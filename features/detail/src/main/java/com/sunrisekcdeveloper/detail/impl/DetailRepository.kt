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

package com.sunrisekcdeveloper.detail.impl

import androidx.room.withTransaction
import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.cache.models.EntityWatchlistMovie
import com.sunrisekcdeveloper.cache.models.EntityWatchlistShow
import com.sunrisekcdeveloper.detail.*
import com.sunrisekcdeveloper.detail.extras.*
import com.sunrisekcdeveloper.detail.extras.model.*
import com.sunrisekcdeveloper.detail.extras.model.ActionRepositoryMovie
import com.sunrisekcdeveloper.movie.Movie
import com.sunrisekcdeveloper.movie.MovieRepositoryContract
import com.sunrisekcdeveloper.movie.WatchlistMovieRepositoryContract
import com.sunrisekcdeveloper.network.NetworkResult
import com.sunrisekcdeveloper.show.TVShow
import com.sunrisekcdeveloper.show.TVShowRepositoryContract
import com.sunrisekcdeveloper.show.WatchlistTVShowRepositoryContract
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

@ExperimentalCoroutinesApi
class DetailRepository(
    private val movieRepo: MovieRepositoryContract,
    private val watchlistMovieRepo: WatchlistMovieRepositoryContract,
    private val showRepo: TVShowRepositoryContract,
    private val watchlistShowRepo: WatchlistTVShowRepositoryContract
) : RepositoryDetailContract {

    override suspend fun submitMovieAction(action: ActionRepositoryMovie) {
            when (action) {
                is ActionRepositoryMovie.Add -> {
                    watchlistMovieRepo.get(action.id).let {
                        if (it != null) {
                            watchlistMovieRepo.update(
                                it.copy(
                                    status = it.status.copy(deleted = false),
                                    meta = it.meta.copy(dateLastUpdated = System.currentTimeMillis())
                                )
                            )
                        } else {
                            watchlistMovieRepo.add(EntityWatchlistMovie.unwatchedFrom(action.id).toDomain())
                        }
                    }
                }
                is ActionRepositoryMovie.Remove -> {
                    watchlistMovieRepo.get(action.id)?.let {
                        watchlistMovieRepo.update(
                            it.copy(
                                status = it.status.copy(deleted = true),
                                meta = it.meta.copy(
                                    dateDeleted = System.currentTimeMillis(),
                                    dateLastUpdated = System.currentTimeMillis()
                                )
                            )
                        )
                    }
                }
                is ActionRepositoryMovie.Watch -> {
                    watchlistMovieRepo.get(action.id).let {
                        if (it != null) {
                            watchlistMovieRepo.update(
                                it.copy(
                                    status = it.status.copy(
                                        deleted = false,
                                        watched = true
                                    ),
                                    meta = it.meta.copy(
                                        dateWatched = System.currentTimeMillis(),
                                        dateLastUpdated = System.currentTimeMillis()
                                    )
                                )
                            )
                        } else {
                            watchlistMovieRepo.add(EntityWatchlistMovie.watchedFrom(action.id).toDomain())
                        }
                    }
                }
                is ActionRepositoryMovie.Unwatch -> {
                    watchlistMovieRepo.get(action.id).let {
                        if (it != null) {
                            watchlistMovieRepo.update(
                                it.copy(
                                    status = it.status.copy(watched = false),
                                    meta = it.meta.copy(dateLastUpdated = System.currentTimeMillis())
                                )
                            )
                        } else {
                            watchlistMovieRepo.add(EntityWatchlistMovie.unwatchedFrom(action.id).toDomain())
                        }
                    }
                }
            }
    }

    override suspend fun submitShowAction(action: ActionRepositoryShow) {
        when (action) {
            is ActionRepositoryShow.Add -> {
                watchlistShowRepo.get(action.id).let {
                    if (it != null) {
                        watchlistShowRepo.update(
                            it.copy(
                                status = it.status.copy(
                                    deleted = false,
                                    started = false,
                                    upToDate = false
                                ),
                                meta = it.meta.copy(lastUpdated = System.currentTimeMillis())
                            )
                        )
                    } else {
                        watchlistShowRepo.add(EntityWatchlistShow.freshBareEntryFrom(action.id).toDomain())
                    }
                }
            }
            is ActionRepositoryShow.Remove -> {
                watchlistShowRepo.get(action.id)?.let {
                    watchlistShowRepo.update(
                        it.copy(
                            status = it.status.copy(deleted = true),
                            meta = it.meta.copy(
                                dateDeleted = System.currentTimeMillis(),
                                lastUpdated = System.currentTimeMillis()
                            )
                        )
                    )
                }
            }
        }
    }

    override suspend fun updateMovieDetails(id: String) = movieRepo.sync(id)

    override suspend fun movieDetails(id: String): Flow<Resource<UIModelMovieDetail>> {
        val detailsFlow = movieRepo.distinctFlow(id)
        val statusFlow = watchlistMovieRepo.distinctFlow(id)

        return combine(detailsFlow, statusFlow) { details, status ->
            details?.let {
                Resource.Success(
                    details.asUIModel(
                        status = if (status?.status?.deleted == false) {
                            MovieWatchlistStatus.Watchlisted
                        } else {
                            MovieWatchlistStatus.NotWatchlisted
                        },
                        watched = if (status?.status?.deleted == false && status.status.watched) {
                            WatchedStatus.Watched
                        } else {
                            WatchedStatus.NotWatched
                        }
                    )
                )
            } ?: Resource.Error(Exception("No movie with ID: $id exists in database"))
        }.onStart { emit(Resource.Loading) }
    }

    override suspend fun updateShowDetails(id: String) = showRepo.sync(id)

    override suspend fun showDetails(id: String): Flow<Resource<UIModelShowDetail>> {
        val detailsFlow = showRepo.distinctFlow(id)
        val statusFlow = watchlistShowRepo.distinctFlow(id)

        return combine(detailsFlow, statusFlow) { details, status ->
            details?.let {
                Resource.Success(
                    details.toUiModel(
                        // TODO [E07-002] [Refactor] This logic can be placed someplace else. The logic that uses a watchlist entity to determine the status of a show
                        watchlist = if (status?.status?.deleted == false) {
                            ShowWatchlistStatus.Watchlisted
                        } else {
                            ShowWatchlistStatus.NotWatchlisted
                        },
                        status = if (status?.status?.deleted == false && status.status.upToDate) {
                            ShowStatus.UpToDate
                        } else if (status?.status?.deleted == false && status.status.started) {
                            ShowStatus.Started
                        } else {
                            ShowStatus.NotStarted
                        }
                    )
                )
            } ?: Resource.Error(Exception("No show with ID: $id exists in database"))
        }.onStart { emit(Resource.Loading) }
    }
}
