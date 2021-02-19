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

package com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.converter

import androidx.room.TypeConverter
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.WatchListType

class WatchListConverter {
    @TypeConverter
    fun fromMediaType(mediaType: MediaType): String {
        return when (mediaType) {
            MediaType.MOVIE -> MediaType.MOVIE.name
            MediaType.SHOW -> MediaType.SHOW.name
        }
    }

    @TypeConverter
    fun stringToMediaType(stringValue: String): MediaType {
        return if (stringValue == MediaType.MOVIE.name) {
            MediaType.MOVIE
        } else {
            MediaType.SHOW
        }
    }

    @TypeConverter
    fun fromWatchListType(watchListType: WatchListType): String {
        return when (watchListType) {
            WatchListType.RECENTLY_ADDED -> WatchListType.RECENTLY_ADDED.name
            WatchListType.IN_PROGRESS -> WatchListType.IN_PROGRESS.name
            WatchListType.UPCOMING -> WatchListType.UPCOMING.name
            WatchListType.ANTICIPATED -> WatchListType.ANTICIPATED.name
            WatchListType.COMPLETED -> WatchListType.COMPLETED.name
            WatchListType.NONE -> WatchListType.NONE.name
        }
    }

    @TypeConverter
    fun stringToWatchListType(stringValue: String): WatchListType {
        return when(stringValue) {
            WatchListType.RECENTLY_ADDED.name -> WatchListType.RECENTLY_ADDED
            WatchListType.IN_PROGRESS.name -> WatchListType.IN_PROGRESS
            WatchListType.UPCOMING.name -> WatchListType.UPCOMING
            WatchListType.ANTICIPATED.name -> WatchListType.ANTICIPATED
            WatchListType.COMPLETED.name -> WatchListType.COMPLETED
            else -> WatchListType.NONE
        }
    }
}