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

package com.sunrisekcdeveloper.watchlist.domain

import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.show.season.WatchlistSeason
import com.sunrisekcdeveloper.show.season.WatchlistSeasonRepositoryContract
import com.sunrisekcdeveloper.watchlist.extras.toDomain
import com.sunrisekcdeveloper.watchlist.extras.toEntity

class WatchlistSeasonRepository(database: TrackerDatabase) : WatchlistSeasonRepositoryContract {

    private val dao = database.watchlistSeasonDao()

    override suspend fun get(showId: String, season: Int): WatchlistSeason? = dao.withId(showId, season)?.toDomain()

    override suspend fun update(season: WatchlistSeason) = dao.update(season.toEntity())

    override suspend fun add(season: WatchlistSeason) = dao.insert(season.toEntity())
}