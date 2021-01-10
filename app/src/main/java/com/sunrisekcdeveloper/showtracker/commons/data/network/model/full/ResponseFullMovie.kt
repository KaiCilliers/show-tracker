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

package com.sunrisekcdeveloper.showtracker.commons.data.network.model.full

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.commons.data.network.model.base.ResponseIdentifiers
import com.sunrisekcdeveloper.showtracker.model.DetailedMovie
import com.sunrisekcdeveloper.showtracker.features.discover.models.Movie

data class ResponseFullMovie(
    @Json(name = "title") val title: String,
    @Json(name = "year") val year: Int?, // network results can give a null year
    @Json(name = "ids") val identifiers: ResponseIdentifiers,
    val tagline: String,
    val overview: String,
    val released: String,
    val runtime: Int,
    val country: String?,
    @Json(name = "trailer") val trailerUrl: String?,
    @Json(name = "homepage") val homepageUrl: String?,
    val status: String,
    val rating: Float,
    val votes: Int,
    @Json(name = "comment_count") val commentCount: Int,
    @Json(name = "updated_at") val updatedAt: String,
    val language: String,
    @Json(name = "available_translations") val translations: List<String>,
    val genres: List<String>,
    val certification: String?
) {
    fun asDomain(): DetailedMovie {
        return DetailedMovie(
            basics = Movie(
                title = title,
                slug = identifiers.slug,
                posterUrl = ""
            ),
            releaseDate = released,
            ageRating = "certification",
            runningTime = "$runtime",
            description = overview,
            notableActors = "{ None Yet }",
            director = "{ None Yet }"
        )
    }
}