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

package com.sunrisekcdeveloper.watchlist.domain.usecase

import com.sunrisekcdeveloper.models.ActionRepositoryMovie
import com.sunrisekcdeveloper.models.MovieWatchedStatus
import com.sunrisekcdeveloper.watchlist.application.UpdateMovieWatchedStatusUseCaseContract
import com.sunrisekcdeveloper.watchlist.domain.repository.RepositoryWatchlistContract
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UpdateMovieWatchedStatusUseCase(
    private val repo: RepositoryWatchlistContract
) : UpdateMovieWatchedStatusUseCaseContract {
    override suspend fun invoke(movieId: String, watchedStatus: MovieWatchedStatus) {
        repo.submitMovieAction(
            when (watchedStatus) {
                MovieWatchedStatus.Watched -> {
                    ActionRepositoryMovie.Watch(movieId)
                }
                MovieWatchedStatus.NotWatched -> {
                    ActionRepositoryMovie.Unwatch(movieId)
                }
            }
        )
    }
}