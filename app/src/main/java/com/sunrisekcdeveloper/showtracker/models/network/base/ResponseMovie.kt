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

package com.sunrisekcdeveloper.showtracker.models.network.base

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.features.discover.models.FeaturedEntity
import com.sunrisekcdeveloper.showtracker.models.local.core.MovieEntity
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie
import org.jetbrains.annotations.TestOnly

/**
 * Response Movie represents a network object containing basic information of a movie
 *
 * @property title is the title of a movie
 * @property year is the year the movie was released
 * @property identifiers contains some IDs used to identify the movie using different APIs
 * @constructor Create empty Response movie
 */
data class ResponseMovie(
    @Json(name = "title") val title: String,
    // TODO convert year to string
    @Json(name = "year") val year: Int?, // network results can give a null year
    @Json(name = "ids") val identifiers: ResponseIdentifiers
) {
    fun asFeaturedMovieEntity() = FeaturedEntity(
        mediaSlug = identifiers.slug,
        tag = "Popular"
    )

    fun asDomain() = Movie(
        title = this.title,
        slug = this.identifiers.slug
    )

    fun asEntity() = MovieEntity(
        slug = this.identifiers.slug,
        title = this.title,
        year = "$year",
        tagline = "",
        overview = "",
        released = "",
        runtime = 0,
        trailerUrl = "",
        homepageUrl = "",
        posterUrl = "",
        status = "",
        rating = 0,
        votes = -1,
        commentCount = -1,
        updatedAt = "",
        certification = ""
    )

    companion object {
        fun createResponseMovies(amount: Int): List<ResponseMovie> {
            val movies = mutableListOf<ResponseMovie>()
            var count = 0
            repeat(amount) {
                movies.add(
                    ResponseMovie(
                        "title",
                        1996,
                        ResponseIdentifiers(
                            count, "$count", "$count", count, count
                        )
                    )
                )
                count++
            }
            return movies
        }
    }
}