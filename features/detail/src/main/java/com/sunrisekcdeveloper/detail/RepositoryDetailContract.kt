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

package com.sunrisekcdeveloper.detail

import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.detail.extras.model.*
import com.sunrisekcdeveloper.detail.extras.model.ActionRepositoryMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

// TODO [E07-002] [Update_class_naming] These feature repos need to be called services and the existing services need to be called API
interface RepositoryDetailContract {
    suspend fun movieDetails(id: String): Flow<Resource<UIModelMovieDetail>>
    suspend fun showDetails(id: String): Flow<Resource<UIModelShowDetail>>
    suspend fun submitMovieAction(action: ActionRepositoryMovie)
    suspend fun submitShowAction(action: ActionRepositoryShow)
    suspend fun updateMovieDetails(id: String)
    suspend fun updateShowDetails(id: String)

    class Fake() : RepositoryDetailContract {

        var expectException = false

        override suspend fun movieDetails(id: String): Flow<Resource<UIModelMovieDetail>> {
            return flow {
                if (expectException) emit(Resource.error(Exception("Fake - No movie with ID: $id exists in database")))
                emit(
                    Resource.success(
                        UIModelMovieDetail(
                            id = id,
                            title = "a",
                            posterPath = "a",
                            overview = "a",
                            releaseYear = "a",
                            certification = "a",
                            runtime = "a",
                            status = MovieWatchlistStatus.NotWatchlisted,
                            watched = WatchedStatus.NotWatched
                        )
                    )
                )
            }.onStart { emit(Resource.loading()) }
        }

        override suspend fun showDetails(id: String): Flow<Resource<UIModelShowDetail>> {
            return flow {
                if (expectException) emit(Resource.error(Exception("Fake - No show with ID: $id exists in database")))
                emit(
                    Resource.success(
                        UIModelShowDetail(
                            id = id,
                            name = "a",
                            posterPath = "a",
                            overview = "a",
                            certification = "a",
                            firstAirDate = "a",
                            seasonsTotal = 1,
                            watchlist = ShowWatchlistStatus.NotWatchlisted,
                            status = ShowStatus.NotStarted
                        )
                    )
                )
            }.onStart { emit(Resource.loading()) }
        }

        override suspend fun submitMovieAction(action: ActionRepositoryMovie) {
            when (action) {
                is ActionRepositoryMovie.Add -> { Timber.d("Fake - Add Movie") }
                is ActionRepositoryMovie.Remove -> { Timber.d("Fake - Remove Movie") }
                is ActionRepositoryMovie.Watch -> { Timber.d("Fake - Watch Movie") }
                is ActionRepositoryMovie.Unwatch -> { Timber.d("Fake - Unwatch Movie") }
            }
        }

        override suspend fun submitShowAction(action: ActionRepositoryShow) {
            when (action) {
                is ActionRepositoryShow.Add -> { Timber.d("Fake - Add Show") }
                is ActionRepositoryShow.Remove -> { Timber.d("Fake - Remove Show") }
            }
        }

        override suspend fun updateMovieDetails(id: String) {
            Timber.d("Fake - Update Movie details with id: $id")
        }

        override suspend fun updateShowDetails(id: String) {
            Timber.d("Fake - Update Show details with id: $id")
        }
    }
}