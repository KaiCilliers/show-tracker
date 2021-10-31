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

package com.sunrisekcdeveloper.watchlist.impl

import com.sunrisekcdeveloper.cache.FilterShows
import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.watchlist.usecase.FetchWatchlistShowsUseCaseContract
import com.sunrisekcdeveloper.watchlist.WatchlistRepositoryContract
import com.sunrisekcdeveloper.watchlist.extras.UIModelWatchlistShow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
class FetchWatchlistShowsUseCase(
    private val repo: WatchlistRepositoryContract
) : FetchWatchlistShowsUseCaseContract {
    override suspend fun invoke(filterOption: FilterShows): Flow<Resource<List<UIModelWatchlistShow>>> {
        return flow {
            emit(Resource.Loading)
            emitAll(repo.watchlistShows(filterOption))
        }
    }
}