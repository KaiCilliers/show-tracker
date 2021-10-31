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

package com.sunrisekcdeveloper.watchlist

import com.sunrisekcdeveloper.cache.FilterMovies
import com.sunrisekcdeveloper.cache.FilterShows
import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.watchlist.extras.model.ActionRepositoryMovie
import com.sunrisekcdeveloper.watchlist.extras.UIModelWatchlistShow
import com.sunrisekcdeveloper.watchlist.extras.model.UIModelWatchlistMovie
import com.sunrisekcdeveloper.watchlist.extras.model.UpdateShowAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface WatchlistRepositoryContract {
    fun watchlistMovies(filterOption: FilterMovies): Flow<Resource<List<UIModelWatchlistMovie>>>

    fun watchlistShows(filterOption: FilterShows): Flow<Resource<List<UIModelWatchlistShow>>>

    suspend fun updateShowProgress(action: UpdateShowAction)

    suspend fun updateWatchlistShowAsUpToDate(showId: String)

    suspend fun submitMovieAction(action: ActionRepositoryMovie)

    class Fake() : WatchlistRepositoryContract {
        override fun watchlistMovies(filterOption: FilterMovies): Flow<Resource<List<UIModelWatchlistMovie>>> {
            return flow {
                Resource.success(UIModelWatchlistMovie.create(60))
            }
        }

        override fun watchlistShows(filterOption: FilterShows): Flow<Resource<List<UIModelWatchlistShow>>> {
            return flow {
                Resource.success(UIModelWatchlistShow.create(60))
            }
        }

        override suspend fun updateShowProgress(action: UpdateShowAction) {  }

        override suspend fun updateWatchlistShowAsUpToDate(showId: String) {  }

        override suspend fun submitMovieAction(action: ActionRepositoryMovie) {
            TODO("Not yet implemented")
        }
    }
}