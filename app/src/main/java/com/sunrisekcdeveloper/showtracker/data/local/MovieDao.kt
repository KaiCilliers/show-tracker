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
import com.sunrisekcdeveloper.showtracker.model.roomresults.*
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE

@Dao
// TODO organize
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

    @Transaction
    open suspend fun replaceMostPlayed(vararg item: MostPlayedListEntity) {
        clearMostPlayed()
        insertMostPlayed(*item)
    }

    @Transaction
    open suspend fun replaceMostWatched(vararg item: MostWatchedListEntity) {
        clearMostWatched()
        insertMostWatched(*item)
    }

    @Transaction
    open suspend fun replaceAnticipated(vararg item: AnticipatedListEntity) {
        clearAnticipated()
        insertAnticipated(*item)
    }

    @Transaction
    open suspend fun replaceRecommended(vararg item: RecommendedListEntity) {
        clearRecommended()
        insertRecommended(*item)
    }

    @Query("DELETE FROM tbl_trending")
    abstract suspend fun clearTrending()

    @Query("DELETE FROM tbl_popular")
    abstract suspend fun clearPopular()

    @Query("DELETE FROM tbl_box_office")
    abstract suspend fun clearBox()

    @Query("DELETE FROM tbl_most_watched")
    abstract suspend fun clearMostWatched()

    @Query("DELETE FROM tbl_most_played")
    abstract suspend fun clearMostPlayed()

    @Query("DELETE FROM tbl_anticipated")
    abstract suspend fun clearAnticipated()

    @Query("DELETE FROM tbl_recommended")
    abstract suspend fun clearRecommended()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertTrending(vararg item: TrendingListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPopular(vararg item: PopularListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertBox(vararg item: BoxOfficeListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMostPlayed(vararg item: MostPlayedListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMostWatched(vararg item: MostWatchedListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAnticipated(vararg item: AnticipatedListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMovie(vararg item: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertRecommended(vararg item: RecommendedListEntity)

    @Query("SELECT fk_popular_media_slug FROM  tbl_popular")
    abstract suspend fun fetchPopular(): List<String>

    @Query("SELECT fk_trending_media_slug FROM  tbl_trending")
    abstract suspend fun fetchTrending(): List<String>

    @Query("SELECT fk_box_media_slug FROM  tbl_box_office")
    abstract suspend fun fetchBox(): List<String>

    @Query("SELECT fk_played_media_slug FROM  tbl_most_played")
    abstract suspend fun fetchMostPlayed(): List<String>

    @Query("SELECT fk_watched_media_slug FROM  tbl_most_watched")
    abstract suspend fun fetchMostWatched(): List<String>

    @Query("SELECT fk_anticipated_media_slug FROM  tbl_anticipated")
    abstract suspend fun fetchAnticipated(): List<String>

    @Query("SELECT fk_rec_media_slug FROM tbl_recommended")
    abstract suspend fun fetchRecommended(): List<String>

    @Update
    abstract suspend fun updatePopular(vararg item: PopularListEntity): Int

    @Update
    abstract suspend fun updateTrending(vararg item: TrendingListEntity): Int

    @Update
    abstract suspend fun updateBox(vararg item: BoxOfficeListEntity): Int

    @Update
    abstract suspend fun updateMostPlayed(vararg item: MostPlayedListEntity): Int

    @Update
    abstract suspend fun updateMostWatched(vararg item: MostWatchedListEntity): Int

    @Update
    abstract suspend fun updateAnticipated(vararg item: AnticipatedListEntity): Int

    @Update
    abstract suspend fun updateRecommended(vararg item: RecommendedListEntity): Int

    @Transaction
    @Query("SELECT * FROM tbl_trending")
    abstract fun trendingMoviesFlow(): Flow<List<TrendingMovies>>

    @Transaction
    @Query("SELECT * FROM tbl_trending")
    abstract fun trendingMovies(): List<TrendingMovies>

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

