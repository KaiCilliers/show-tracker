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

package com.sunrisekcdeveloper.showtracker.features.discover

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.sunrisekcdeveloper.showtracker.features.discover.models.*

@Dao
abstract class DiscoveryDao {
    @Transaction
    @Query("SELECT * FROM tbl_trending")
    abstract fun trendingMovies(): List<TrendingMovies>

    @Transaction
    @Query("SELECT * FROM tbl_popular")
    abstract suspend fun popularMovies(): List<PopularMovies>

    @Transaction
    @Query("SELECT * FROM tbl_box_office")
    abstract suspend fun boxOfficeMovies(): List<BoxOfficeMovies>

    @Transaction
    @Query("SELECT * FROM tbl_anticipated")
    abstract suspend fun mostAnticipatedMovies(): List<AnticipatedMovies>

    @Transaction
    @Query("SELECT * FROM tbl_most_watched")
    abstract suspend fun mostWatchedMovies(): List<MostWatchedMovies>

    @Transaction
    @Query("SELECT * FROM tbl_most_played")
    abstract suspend fun mostPlayedMovies(): List<MostPlayedMovies>

    @Transaction
    @Query("SELECT * FROM tbl_recommended")
    abstract suspend fun recommended(): List<RecommendedMovies>
}