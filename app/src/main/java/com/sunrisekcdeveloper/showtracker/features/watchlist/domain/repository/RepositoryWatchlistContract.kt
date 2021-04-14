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

package com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository

import com.sunrisekcdeveloper.showtracker.common.util.Resource
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.FilterMovies
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.FilterShows
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityEpisode
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityShow
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistShow
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.UIModelWatchlisMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.UpdateShowAction
import com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.UIModelWatchlistShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface RepositoryWatchlistContract {

    fun watchlistMovies(filterOption: FilterMovies): Flow<Resource<List<UIModelWatchlisMovie>>>

    fun watchlistShows(filterOption: FilterShows): Flow<Resource<List<UIModelWatchlistShow>>>

    suspend fun updateShowProgress(action: UpdateShowAction)

    suspend fun updateWatchlistShowAsUpToDate(showId: String)

    class Fake() : RepositoryWatchlistContract {
        override fun watchlistMovies(filterOption: FilterMovies): Flow<Resource<List<UIModelWatchlisMovie>>> {
            return flow {
                Resource.success(UIModelWatchlisMovie.create(60))
            }
        }

        override fun watchlistShows(filterOption: FilterShows): Flow<Resource<List<UIModelWatchlistShow>>> {
            return flow {
                Resource.success(UIModelWatchlistShow.create(60))
            }
        }

        override suspend fun updateShowProgress(action: UpdateShowAction) {  }

        override suspend fun updateWatchlistShowAsUpToDate(showId: String) {  }
    }
}