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
import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.show.FullTVShow
import com.sunrisekcdeveloper.show.WatchlistTVShowRepositoryContract
import com.sunrisekcdeveloper.show.valueobjects.*
import com.sunrisekcdeveloper.watchlist.extras.toDomain
import com.sunrisekcdeveloper.watchlist.extras.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WatchlistTVShowRepository(
    storage: TrackerDatabase
) : WatchlistTVShowRepositoryContract {
    private val dao = storage.watchlistShowDao()

    override suspend fun get(id: String): WatchlistTVShow? = dao.withId(id)?.toDomain()

    override suspend fun update(show: WatchlistTVShow) = dao.update(show.toEntity())

    override suspend fun add(show: WatchlistTVShow) = dao.insert(show.toEntity())

    override fun distinctFlow(id: String): Flow<WatchlistTVShow?> {
        return dao.distinctWatchlistShowFlow(id).map { it?.toDomain() }
    }

    override fun distinctFlow(filter: TVShowFilter): Flow<List<FullTVShow>> {
        val tempConversion = when(filter) {
            TVShowFilter.NoFilters -> FilterShows.NoFilters
            TVShowFilter.AddedToday -> FilterShows.AddedToday
            TVShowFilter.WatchedToday -> FilterShows.WatchedToday
            TVShowFilter.Started -> FilterShows.Started
            TVShowFilter.NotStarted -> FilterShows.NotStarted
        }
        return dao.distinctWithDetailsFlow(tempConversion).map { list ->
            list.map { it?.toDomain() }
        }
    }
}
