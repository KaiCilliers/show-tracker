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
import com.sunrisekcdeveloper.showtracker.data.local.model.categories.PopularListEntity
import com.sunrisekcdeveloper.showtracker.data.local.model.categories.RecommendedListEntity
import com.sunrisekcdeveloper.showtracker.data.local.model.categories.TrendingListEntity
import com.sunrisekcdeveloper.showtracker.data.local.model.core.MovieEntity
import com.sunrisekcdeveloper.showtracker.model.roomresults.TrendingMoviesNew
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MovieDao {

    @Query("SELECT movie_title FROM tbl_movie")
    abstract fun test(): List<String>
    // trending
    @Transaction
    @Query("SELECT * FROM tbl_trending")
    abstract fun trendingMovies(): List<TrendingMoviesNew> // todo this shit sould return flow if you want to observe changes in the table...

    @Query("""
        DELETE FROM tbl_trending
        WHERE fk_trending_media_slug IN (
            SELECT fk_trending_media_slug
            FROM tbl_trending
            ORDER BY fk_trending_media_slug DESC
            LIMIT 1
        )
    """)
    abstract fun tempClearTopThree()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMovie(vararg movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTrendingMedia(vararg item: TrendingListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPopularMedia(vararg item: PopularListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRecommendedMedia(vararg item: RecommendedListEntity)

    @Transaction
    open fun upsertTrendingMedia(vararg item: TrendingListEntity) {
        clearTrending()
        insertTrendingMedia(*item)
    }
    @Transaction
    open fun upsertPopularMedia(vararg item: PopularListEntity) {
        clearPopular()
        insertPopularMedia(*item)
    }
    @Transaction
    open fun upsertRecommendedMedia(vararg item: RecommendedListEntity) {
        clearRecommended(item[0].period)
        insertRecommendedMedia(*item)
    }

    @Query("DELETE FROM tbl_trending")
    abstract fun clearTrending()
    @Query("DELETE FROM tbl_popular")
    abstract fun clearPopular()
    @Query("DELETE FROM tbl_recommended WHERE rec_period_count = :period")
    abstract fun clearRecommended(period: String)
}

