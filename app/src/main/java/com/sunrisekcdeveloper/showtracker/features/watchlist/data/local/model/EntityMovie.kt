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

package com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_movie")
data class EntityMovie (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "movie_id") val id: String,
    @ColumnInfo(name = "movie_title") val title: String,
    @ColumnInfo(name = "movie_overview") val overview: String,
    @ColumnInfo(name = "movie_poster_path") val posterPath: String,
    @ColumnInfo(name = "movie_certification") val certification: String,
    @ColumnInfo(name = "movie_release_date") val releaseDate: String,
    @ColumnInfo(name = "movie_runtime") val runTime: String,
    @ColumnInfo(name = "movie_last_updated") val dateLastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "tbl_watchlist_movie")
data class EntityWatchlistMovie(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "watch_movie_id") val id: String,
    @ColumnInfo(name = "watch_movie_watched") val watched: Boolean,
    @ColumnInfo(name = "watch_movie_date_added") val dateAdded: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "watch_movie_date_watched") val dateWatched: Long,
    @ColumnInfo(name = "watch_movie_last_updated") val dateLastUpdated: Long = System.currentTimeMillis()
) {
    companion object {
        fun unWatchedfrom(id: String) = EntityWatchlistMovie(
            id = id,
            watched = false,
            dateAdded = System.currentTimeMillis(),
            dateWatched = -1L,
            dateLastUpdated = System.currentTimeMillis()
        )
    }
}