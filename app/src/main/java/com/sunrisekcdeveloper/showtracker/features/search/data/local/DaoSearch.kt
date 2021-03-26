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

package com.sunrisekcdeveloper.showtracker.features.search.data.local

import androidx.room.*
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityShow
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistShow

@Dao
interface DaoSearch {

    @Transaction
    @Query("SELECT * FROM tbl_watchlist_movie WHERE watch_movie_deleted = 0 AND watch_movie_watched = 0")
    suspend fun unwatchedMovies(): List<WatchlistMovieWithDetails>

    @Transaction
    @Query("SELECT * FROM tbl_watchlist_show WHERE watch_show_deleted = 0 AND watch_show_started = 0")
    suspend fun unwatchedShows(): List<WatchlistShowWithDetails>
}

data class WatchlistShowWithDetails(
    @Embedded val status: EntityWatchlistShow,
    @Relation(
        parentColumn = "watch_show_id",
        entityColumn = "show_id"
    )
    val details : EntityShow
)

data class WatchlistMovieWithDetails(
    @Embedded val status: EntityWatchlistMovie,
    @Relation(
        parentColumn = "watch_movie_id",
        entityColumn = "movie_id"
    )
    val details : EntityMovie
)