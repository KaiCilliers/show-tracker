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

import com.sunrisekcdeveloper.cache.FilterMovies
import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.watchlist.application.FetchWatchlistMoviesUseCaseContract
import com.sunrisekcdeveloper.watchlist.domain.model.UIModelWatchlistMovie
import com.sunrisekcdeveloper.watchlist.domain.repository.RepositoryWatchlistContract
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
class FetchWatchlistMoviesUseCase(
    private val repo: RepositoryWatchlistContract
) : FetchWatchlistMoviesUseCaseContract {
    override suspend fun invoke(filterOption: FilterMovies): Flow<Resource<List<UIModelWatchlistMovie>>> {
        return flow {
            emit(Resource.Loading)
            emitAll(repo.watchlistMovies(filterOption))
        }
    }
}