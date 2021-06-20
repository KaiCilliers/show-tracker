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

package com.sunrisekcdeveloper.detail.data.repository

import androidx.room.withTransaction
import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.cache.models.EntityWatchlistMovie
import com.sunrisekcdeveloper.cache.models.EntityWatchlistShow
import com.sunrisekcdeveloper.detail.asEntityMovie
import com.sunrisekcdeveloper.detail.asEntityShow
import com.sunrisekcdeveloper.detail.asUIModelMovieDetail
import com.sunrisekcdeveloper.detail.asUIModelShowDetail
import com.sunrisekcdeveloper.detail.data.model.ReleaseDateType
import com.sunrisekcdeveloper.detail.data.network.RemoteDataSourceDetailContract
import com.sunrisekcdeveloper.detail.data.util.CertificationsContract
import com.sunrisekcdeveloper.detail.data.util.CertificationsMovie
import com.sunrisekcdeveloper.detail.data.util.CertificationsShow
import com.sunrisekcdeveloper.detail.domain.model.*
import com.sunrisekcdeveloper.detail.domain.repository.RepositoryDetailContract
import com.sunrisekcdeveloper.network.NetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

@ExperimentalCoroutinesApi
class RepositoryDetail(
    private val remote: RemoteDataSourceDetailContract,
    private val database: TrackerDatabase
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

    override suspend fun updateMovieDetails(id: String) {
        val responseMovie = remote.movieDetails(id)
        val responseCertification = remote.movieReleaseDates(id)

        if (responseMovie is NetworkResult.Success && responseCertification is NetworkResult.Success) {
            database.movieDao().insert(
                responseMovie.data.asEntityMovie(
                    CertificationsContract.Smart(
                        CertificationsMovie(
                            responseCertification.data.results,
                            ReleaseDateType.Theatrical
                        )
                    ).fromUS()
                )
            )
        } else {
            Timber.e("Error with fetching movie details and certifications for movie with ID: $id [$responseMovie] [$responseCertification]")
        }
    }

    override suspend fun movieDetails(id: String): Flow<Resource<UIModelMovieDetail>> {
        val detailsFlow = database.movieDao().distinctMovieFlow(id)
        val statusFlow = database.watchlistMovieDao().distinctWatchlistMovieFlow(id)

        return combine(detailsFlow, statusFlow) { details, status ->
            details?.let {
                    Resource.Success(
                        details.asUIModelMovieDetail(
                            status = if (status?.deleted == false) {
                                MovieWatchlistStatus.Watchlisted
                            } else {
                                MovieWatchlistStatus.NotWatchlisted
                            },
                            watched = if (status?.deleted == false && status.watched) {
                                WatchedStatus.Watched
                            } else {
                                WatchedStatus.NotWatched
                            }
                        )
                    )
            } ?: Resource.Error(Exception("No movie with ID: $id exists in database"))
        }.onStart { emit(Resource.Loading) }
    }

    override suspend fun updateShowDetails(id: String) {
        val responseShow = remote.showDetail(id)
        val responseCertification = remote.showCertification(id)

        if (responseShow is NetworkResult.Success && responseCertification is NetworkResult.Success) {
            database.showDao().insert(
                responseShow.data.asEntityShow(
                    CertificationsContract.Smart(CertificationsShow(responseCertification.data.results))
                        .fromUS()
                )
            )
        } else {
            Timber.e("Error with fetching show details and certifications for movie with ID: $id [$responseShow] [$responseCertification]")
        }
    }

    override suspend fun showDetails(id: String): Flow<Resource<UIModelShowDetail>> {
        val detailsFlow = database.showDao().distinctShowFlow(id)
        val statusFlow = database.watchlistShowDao().distinctWatchlistShowFlow(id)

        return combine(detailsFlow, statusFlow) { details, status ->
            details?.let {
                Resource.Success(
                    details.asUIModelShowDetail(
                        watchlist = if (status?.deleted == false) {
                            ShowWatchlistStatus.Watchlisted
                        } else {
                            ShowWatchlistStatus.NotWatchlisted
                        },
                        status = if (status?.deleted == false && status.upToDate) {
                            ShowStatus.UpToDate
                        } else if (status?.deleted == false && status.started) {
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