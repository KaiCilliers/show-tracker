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

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.sunrisekcdeveloper.showtracker.common.dao.combined.WatchlistMovieWithDetails
import com.sunrisekcdeveloper.showtracker.common.dao.combined.WatchlistShowWithDetails

@Deprecated("Extracted all methods to new table specific Dao's")
@Dao
interface DaoSearch {

    @Transaction
    @Query("SELECT * FROM tbl_watchlist_movie WHERE watch_movie_deleted = 0 AND watch_movie_watched = 0")
    suspend fun unwatchedMovies(): List<WatchlistMovieWithDetails>

    @Transaction
    @Query("SELECT * FROM tbl_watchlist_show WHERE watch_show_deleted = 0 AND watch_show_started = 0")
    suspend fun unwatchedShows(): List<WatchlistShowWithDetails>
}