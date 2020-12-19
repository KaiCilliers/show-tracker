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
import com.sunrisekcdeveloper.showtracker.data.local.model.categories.BoxOfficeListEntity
import com.sunrisekcdeveloper.showtracker.data.local.model.categories.PopularListEntity
import com.sunrisekcdeveloper.showtracker.data.local.model.categories.TrendingListEntity
import com.sunrisekcdeveloper.showtracker.data.local.model.core.MovieEntity
import com.sunrisekcdeveloper.showtracker.model.roomresults.BoxOfficeMovies
import com.sunrisekcdeveloper.showtracker.model.roomresults.PopularMovies
import com.sunrisekcdeveloper.showtracker.model.roomresults.TrendingMovies
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE

@Dao
abstract class MovieDao {

    @Transaction
    open suspend fun replaceTrending(vararg item: TrendingListEntity) {
        clearTrending()
        insertTrending(*item)
    }

    @Transaction
    open suspend fun replacePopular(vararg item: PopularListEntity) {
        clearPopular()
        insertPopular(*item)
    }

    @Transaction
    open suspend fun replaceBox(vararg item: BoxOfficeListEntity) {
        clearBox()
        insertBox(*item)
    }

    @Query("DELETE FROM tbl_trending")
    abstract suspend fun clearTrending()

    @Query("DELETE FROM tbl_popular")
    abstract suspend fun clearPopular()

    @Query("DELETE FROM tbl_box_office")
    abstract suspend fun clearBox()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertTrending(vararg item: TrendingListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPopular(vararg item: PopularListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertBox(vararg item: BoxOfficeListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMovie(vararg item: MovieEntity)

    @Query("SELECT fk_popular_media_slug FROM  tbl_popular")
    abstract suspend fun fetchPopular(): List<String>

    @Update
    abstract suspend fun updatePopular(vararg item: PopularListEntity): Int

    @Transaction
    @Query("SELECT * FROM tbl_trending")
    abstract fun trendingMoviesFlow(): Flow<List<TrendingMovies>>

    @Query("SELECT * FROM tbl_trending")
    abstract fun trendingMovies(): List<TrendingMovies>

    @Transaction
    @Query("SELECT * FROM tbl_popular")
    abstract fun popularMoviesFlow(): Flow<List<PopularMovies>>

    @Transaction
    @Query("SELECT * FROM tbl_box_office")
    abstract fun boxOfficeMoviesFlow(): Flow<List<BoxOfficeMovies>>

    @Query("SELECT * FROM tbl_movie")
    abstract fun allMovies(): Flow<List<MovieEntity>>

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

