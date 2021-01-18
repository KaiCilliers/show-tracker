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

import androidx.room.*
import com.sunrisekcdeveloper.showtracker.features.discover.models.FeaturedEntity
import com.sunrisekcdeveloper.showtracker.features.discover.models.FeaturedMovies
import com.sunrisekcdeveloper.showtracker.models.local.core.MovieEntity

@Dao
abstract class DiscoveryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMovie(vararg movie: MovieEntity)

    @Query("SELECT movie_poster_url FROM tbl_movie WHERE movie_slug = :slug")
    abstract suspend fun moviePosterUrl(slug: String): String?

    @Insert
    abstract suspend fun insertFeatured(vararg movies: FeaturedEntity)

    @Update
    abstract suspend fun updateFeatured(movie: FeaturedEntity)

    @Query("DELETE FROM tbl_featured")
    abstract suspend fun clearAllFeatured()

    @Query("""
        SELECT * FROM tbl_featured ORDER BY tag
    """)
    abstract suspend fun groupedFeatured(): List<FeaturedMovies>

}
// insert
// update
// transaction (clear and insert)
// fetch all slugs
// return embedded values