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

package com.sunrisekcdeveloper.showtracker.data.local

import androidx.room.*
import com.sunrisekcdeveloper.showtracker.data.local.model.categories.*
import com.sunrisekcdeveloper.showtracker.data.local.model.core.MovieEntity
import com.sunrisekcdeveloper.showtracker.features.discover.models.*
import kotlinx.coroutines.flow.Flow

@Dao
// TODO organize
abstract class MovieDao {
    @Transaction
    @Query("SELECT * FROM tbl_trending")
    abstract fun trendingMoviesFlow(): Flow<List<TrendingMovies>>

    @Transaction
    @Query("SELECT * FROM tbl_popular")
    abstract fun popularMoviesFlow(): Flow<List<PopularMovies>>

    @Transaction
    @Query("SELECT * FROM tbl_box_office")
    abstract fun boxOfficeMoviesFlow(): Flow<List<BoxOfficeMovies>>

    @Transaction
    @Query("SELECT * FROM tbl_anticipated")
    abstract fun mostAnticipatedMoviesFlow(): Flow<List<AnticipatedMovies>>

    @Transaction
    @Query("SELECT * FROM tbl_most_watched")
    abstract fun mostWatchedMoviesFlow(): Flow<List<MostWatchedMovies>>

    @Transaction
    @Query("SELECT * FROM tbl_most_played")
    abstract fun mostPlayedMoviesFlow(): Flow<List<MostPlayedMovies>>

    @Transaction
    @Query("SELECT * FROM tbl_recommended")
    abstract fun recommendedFlow(): Flow<List<RecommendedMovies>>

    @Query("SELECT * FROM tbl_movie")
    abstract fun allMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMovie(vararg item: MovieEntity)

    @Transaction
    @Query(
        """
        DELETE FROM tbl_movie
        WHERE movie_slug IN (
            SELECT movie_slug FROM tbl_movie
            LIMIT 1
        )
    """
    )
    abstract suspend fun deleteOneMovie()
}

