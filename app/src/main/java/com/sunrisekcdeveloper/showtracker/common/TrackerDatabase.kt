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

package com.sunrisekcdeveloper.showtracker.common

import androidx.room.*
import com.sunrisekcdeveloper.showtracker.features.detail.data.local.DaoDetail
import com.sunrisekcdeveloper.showtracker.features.discovery.data.local.DaoDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.data.local.DaoRemoteKeys
import com.sunrisekcdeveloper.showtracker.features.discovery.data.local.models.*
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ListType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.progress.data.local.DaoProgress
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.DaoWatchlist
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.*
import java.util.Date

@Database(
    entities = [
        EntityMovie::class, EntityWatchlistMovie::class,
        EntityShow::class, EntityWatchlistShow::class,
        EntitySeason::class, EntityWatchlistSeason::class,
        EntityEpisode::class, EntityWatchlistEpisode::class,
        EntityWatchlistBatch::class, UIModelDiscovery::class,
        RemoteKeys::class
    ],
    version = 62,
    exportSchema = false
)
@TypeConverters(TrackerTypeConverters::class)
abstract class TrackerDatabase : RoomDatabase() {

    abstract fun remoteKeysDiscovery(): DaoRemoteKeys
    abstract fun discoveryDao(): DaoDiscovery
    abstract fun progressDao(): DaoProgress
    abstract fun detailDao(): DaoDetail
    abstract fun watchlistDao(): DaoWatchlist

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

    @TypeConverter
    fun fromMediaType(type: MediaType): String = type.javaClass.simpleName

    @TypeConverter
    fun fromStringMediaType(value: String): MediaType {
        return when (value) {
            MediaType.Movie.javaClass.simpleName -> {
                MediaType.Movie
            }
            MediaType.Show.javaClass.simpleName -> {
                MediaType.Show
            }
            else -> {
                MediaType.Movie
            } // not best way to handle, but it's fine
        }
    }

    @TypeConverter
    fun fromListType(type: ListType): String {
        return type.javaClass.simpleName
    }

    @TypeConverter
    fun fromStringListType(value: String): ListType {
        return when (value) {
            ListType.MoviePopular.javaClass.simpleName -> {
                ListType.MoviePopular
            }
            ListType.MovieTopRated.javaClass.simpleName -> {
                ListType.MovieTopRated
            }
            ListType.MovieUpcoming.javaClass.simpleName -> {
                ListType.MovieUpcoming
            }
            ListType.ShowPopular.javaClass.simpleName -> {
                ListType.ShowPopular
            }
            ListType.ShowTopRated.javaClass.simpleName -> {
                ListType.ShowTopRated
            }
            ListType.ShowAiringToday.javaClass.simpleName -> {
                ListType.ShowAiringToday
            }
            else -> {
                ListType.MoviePopular
            } // Not best way to handle, but it's fine
        }
    }

}