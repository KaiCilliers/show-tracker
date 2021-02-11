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

package com.sunrisekcdeveloper.showtracker.models.local.core

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sunrisekcdeveloper.showtracker.features.discover.data.local.model.FeaturedEntity
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie

@Entity(tableName = "tbl_movie")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "movie_slug")
    val slug: String,
    @ColumnInfo(name = "movie_title")
    val title: String,
    @ColumnInfo(name = "movie_year")
    val year: String,
    @ColumnInfo(name = "movie_tagline")
    val tagline: String,
    @ColumnInfo(name = "movie_overview")
    val overview: String,
    @ColumnInfo(name = "movie_released_date")
    val released: String,
    @ColumnInfo(name = "movie_updated_at_date")
    val updatedAt: String,
    @ColumnInfo(name = "movie_runtime")
    val runtime: Int,
    @ColumnInfo(name = "movie_trailer_url")
    val trailerUrl: String?,
    @ColumnInfo(name = "movie_homepage_url")
    val homepageUrl: String,
    @ColumnInfo(name = "movie_poster_url")
    // TODO replace with different implementation
    var posterUrl: String, // contains multiple urls split by ';'
    @ColumnInfo(name = "movie_status")
    val status: String,
    @ColumnInfo(name = "movie_vote_count")
    val votes: Int,
    @ColumnInfo(name = "movie_rating")
    val rating: Int,
    @ColumnInfo(name = "movie_comment_count")
    val commentCount: Int,
    @ColumnInfo(name = "movie_certification")
    val certification: String
) {
    fun asFeaturedEntity(tag: String) = FeaturedEntity(
        mediaSlug = slug,
        tag = tag
    )
    fun asDomain() = Movie(
        title = title,
        slug = slug,
        posterUrl = posterUrl
    )

    companion object {
        fun createMovieEntities(amount: Int): List<MovieEntity> {
            val movies = mutableListOf<MovieEntity>()
            var count = 0
            repeat(amount) {
                movies.add(
                    MovieEntity(
                        slug = "slug$count",
                        title = "title$count",
                        year = "year$count",
                        tagline = "tagline$count",
                        overview = "overview$count",
                        released = "released$count",
                        runtime = count,
                        trailerUrl = "trailerUrl$count",
                        homepageUrl = "homepageUrl$count",
                        posterUrl = "posterUrl$count",
                        status = "status$count",
                        rating = count,
                        votes = count,
                        commentCount = count,
                        updatedAt = "updatedAt$count",
                        certification = "certification$count"
                    )
                )
                count++
            }
            return movies
        }
    }
}