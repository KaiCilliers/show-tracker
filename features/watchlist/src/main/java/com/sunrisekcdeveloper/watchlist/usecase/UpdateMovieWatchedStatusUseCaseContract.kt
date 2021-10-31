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

package com.sunrisekcdeveloper.watchlist.usecase

import com.sunrisekcdeveloper.watchlist.extras.model.MovieWatchedStatus
import timber.log.Timber

interface UpdateMovieWatchedStatusUseCaseContract {
    suspend operator fun invoke(movieId: String, watchedStatus: MovieWatchedStatus)
    class Fake() : UpdateMovieWatchedStatusUseCaseContract {
        override suspend fun invoke(movieId: String, watchedStatus: MovieWatchedStatus) {
            when (watchedStatus) {
                MovieWatchedStatus.Watched -> { Timber.d("Fake - Updated Movie )id=$movieId) status to Watched") }
                MovieWatchedStatus.NotWatched -> { Timber.d("Fake - Updated Movie )id=$movieId) status to Unwatched") }
            }
        }
    }
}