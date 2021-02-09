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

package com.sunrisekcdeveloper.showtracker.commons

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.sunrisekcdeveloper.showtracker.models.local.core.MovieEntity
import com.sunrisekcdeveloper.showtracker.features.detail.DetailDao
import com.sunrisekcdeveloper.showtracker.features.discover.local.DiscoveryDao
import com.sunrisekcdeveloper.showtracker.features.discover.models.FeaturedEntity
import com.sunrisekcdeveloper.showtracker.features.watchlist.WatchlistDao
import com.sunrisekcdeveloper.showtracker.models.local.categories.*
import java.util.Date

@Database(
    entities = [
        MovieEntity::class, TrendingListEntity::class,
        PopularListEntity::class, RecommendedListEntity::class,
        BoxOfficeListEntity::class, AnticipatedListEntity::class,
        MostPlayedListEntity::class, MostWatchedListEntity::class,
        FeaturedEntity::class
    ],
    version = 26,
    exportSchema = false
)
@TypeConverters(TrackerTypeConverters::class)
abstract class TrackerDatabase : RoomDatabase() {
    abstract fun discoveryDao(): DiscoveryDao
    abstract fun watchlistDao(): WatchlistDao
    abstract fun detailDao(): DetailDao
}

/**
 * Tracker type converters
 *
 * Two big issues
 *  Date class does not support timezones
 *  Persisted value is a simple Long which also can't store timezone info
 * https://medium.com/androiddevelopers/room-time-2b4cf9672b98
 *
 * @constructor Create empty Tracker type converters
 */
class TrackerTypeConverters() {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}