/*
 * Copyright © 2020. The Android Open Source Project
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

package com.sunrisekcdeveloper.showtracker.commons

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sunrisekcdeveloper.showtracker.models.local.core.MovieEntity
import com.sunrisekcdeveloper.showtracker.features.detail.DetailDao
import com.sunrisekcdeveloper.showtracker.features.discover.DiscoveryDao
import com.sunrisekcdeveloper.showtracker.features.search.SearchDao
import com.sunrisekcdeveloper.showtracker.features.watchlist.WatchlistDao
import com.sunrisekcdeveloper.showtracker.models.local.categories.*

@Database(
    entities = [
        MovieEntity::class, TrendingListEntity::class,
        PopularListEntity::class, RecommendedListEntity::class,
        BoxOfficeListEntity::class, AnticipatedListEntity::class,
        MostPlayedListEntity::class, MostWatchedListEntity::class
    ],
    version = 16,
    exportSchema = false
)
abstract class TrackerDatabase : RoomDatabase() {
    abstract fun discoveryDao(): DiscoveryDao
    abstract fun searchDao(): SearchDao
    abstract fun watchlistDao(): WatchlistDao
    abstract fun detailDao(): DetailDao
}