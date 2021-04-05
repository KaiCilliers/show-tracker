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

package com.sunrisekcdeveloper.showtracker.features.detail.data.repository

import androidx.room.withTransaction
import com.sunrisekcdeveloper.showtracker.common.TrackerDatabase
import com.sunrisekcdeveloper.showtracker.common.util.*
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseMovieReleaseDates
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseShowCertification
import com.sunrisekcdeveloper.showtracker.features.detail.data.network.RemoteDataSourceDetailContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.*
import com.sunrisekcdeveloper.showtracker.features.detail.domain.repository.RepositoryDetailContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityShow
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistShow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

@ExperimentalCoroutinesApi
class RepositoryDetail(
    private val remote: RemoteDataSourceDetailContract,
    private val database: TrackerDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RepositoryDetailContract {

    private val daoWatchlistShow = database.watchlistShowDao()
    private val daoWatchlistMovie = database.watchlistMovieDao()

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

    override suspend fun submitShowAction(action: ActionRepositoryShow) {
        database.withTransaction {
            when (action) {
                is ActionRepositoryShow.Add -> {
                    if (daoWatchlistShow.exist(action.id)) {
                        daoWatchlistShow.update(
                            daoWatchlistShow.withId(action.id).copy(
                                deleted = false,
                                started = false,
                                upToDate = false,
                                lastUpdated = System.currentTimeMillis()
                            )
                        )
                    } else {
                        daoWatchlistShow.insert(EntityWatchlistShow.freshBareEntryFrom(action.id))
                    }
                }
                is ActionRepositoryShow.Remove -> {
                    if (daoWatchlistShow.exist(action.id)) {
                        daoWatchlistShow.update(
                            daoWatchlistShow.withId(action.id).copy(
                                deleted = true,
                                dateDeleted = System.currentTimeMillis(),
                                lastUpdated = System.currentTimeMillis()
                            )
                        )
                    }
                }
            }
        }
    }

    override suspend fun fetchAndSaveMovieDetails(id: String) {
        withContext(dispatcher) {
            launch {
                val responseMovie = remote.movieDetails(id)
                val responseCertification = remote.movieReleaseDates(id)

                when (responseMovie) {
                    is NetworkResult.Success -> {
                        var result = responseMovie.data.asEntityMovie()
                        when (responseCertification) {
                            is NetworkResult.Success -> {
                                result = result.copy(
                                    certification = movieCertificationUSIfPossible(
                                        responseCertification.data.results
                                    )
                                )
                            }
                            is NetworkResult.Error -> {
                                // todo log these somewhere
                                //  dont swallow exception - bubble it up and handle it in UI to maybe retry
                                Timber.e("Error fetching certification data for movie with ID: $id [${responseCertification.exception}]")
                            }
                        }
                        saveMovieDetails(result)
                    }
                    is NetworkResult.Error -> {
                        Timber.e("Error fetching movie details for movie with ID: $id [${responseMovie.exception}]")
                    }
                }
            }
        }
    }

    override suspend fun movieDetails(id: String): Flow<Resource<UIModelMovieDetail>> {
        val detailsFlow = database.movieDao().distinctMovieFlow(id)
        val statusFlow = database.watchlistMovieDao().distinctWatchlistMovieFlow(id)

        return combine(detailsFlow, statusFlow) { details, status ->
            details?.let {
                status?.let {
                    Resource.Success(
                        details.asUIModelMovieDetail(
                            status = if (!status.deleted) {
                                MovieWatchlistStatus.Watchlisted
                            } else {
                                MovieWatchlistStatus.NotWatchlisted
                            },
                            watched = if (!status.deleted && status.watched) {
                                WatchedStatus.Watched
                            } else {
                                WatchedStatus.NotWatched
                            }
                        )
                    )
                }
            } ?: Resource.Error(Exception("No movie with ID: $id exists in database"))
        }.onStart { emit(Resource.Loading) }
    }

    override suspend fun fetchAndSaveShowDetails(id: String) {
        withContext(dispatcher) {
            launch {
                val responseShow = remote.showDetail(id)
                val responseCertification = remote.showCertification(id)

                when (responseShow) {
                    is NetworkResult.Success -> {
                        var result = responseShow.data.asEntityShow()
                        when (responseCertification) {
                            is NetworkResult.Success -> {
                                result = result.copy(
                                    certification = showCertificationUSIfPossible(
                                        responseCertification.data.results
                                    )
                                )
                            }
                            is NetworkResult.Error -> {
                                Timber.e("Error fetching show certification for show: $id")
                            }
                        }
                        saveShowDetails(result)
                    }
                    is NetworkResult.Error -> {
                        Timber.e("Error fetching show details for show with id: $id")
                    }
                }
            }
        }
    }

    override suspend fun showDetails(id: String): Flow<Resource<UIModelShowDetail>> {
        val detailsFlow = database.showDao().distinctShowFlow(id)
        val statusFlow = database.watchlistShowDao().distinctWatchlistShowFlow(id)

        return combine(detailsFlow, statusFlow) { details, status ->
            details?.let {
                status?.let {
                    Resource.Success(
                        details.asUIModelShowDetail(
                            watchlist = if (!status.deleted) {
                                ShowWatchlistStatus.Watchlisted
                            } else {
                                ShowWatchlistStatus.NotWatchlisted
                            },
                            status = if (!status.deleted && status.upToDate) {
                                ShowStatus.UpToDate
                            } else if (!status.deleted && status.started) {
                                ShowStatus.Started
                            } else {
                                ShowStatus.NotStarted
                            }
                        )
                    )
                }
            } ?: Resource.Error(Exception("No show with ID: $id exists in database"))
        }.onStart { emit(Resource.Loading) }
    }

    private suspend fun saveMovieDetails(entity: EntityMovie) {
        database.movieDao().insert(entity)
    }

    private fun movieCertificationUSIfPossible(data: List<ResponseMovieReleaseDates>): String {
        return if (data.isNotEmpty()) {
            val iso = data.find { it.iso == "US" } ?: data[0]
            return if (iso.releaseDates.isNotEmpty()) {
                val certification = iso.releaseDates.find { it.releaseType == 3 }?.certification
                    ?: iso.releaseDates[0].certification
                return if (certification != "") {
                    certification
                } else {
                    NONE_AVAILABLE
                }
            } else {
                NONE_AVAILABLE
            }
        } else {
            NONE_AVAILABLE
        }
    }

    private suspend fun saveShowDetails(entity: EntityShow) {
        database.showDao().insert(entity)
    }

    private fun showCertificationUSIfPossible(data: List<ResponseShowCertification>): String {
        return if (data.isNotEmpty()) {
            data.find {
                it.iso == "US"
            }?.certification ?: data[0].certification
        } else {
            NONE_AVAILABLE
        }
    }

    companion object {
        const val NONE_AVAILABLE = "N/A"
    }
}