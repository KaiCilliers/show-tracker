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

package com.sunrisekcdeveloper.showtracker.models.network.envelopes

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.features.discover.data.local.model.FeaturedEntity
import com.sunrisekcdeveloper.showtracker.models.local.categories.RecommendedListEntity
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseShow

data class EnvelopeUserCount(
    @Json(name = "user_count") val userCount: Int,
    @Json(name = "movie") val movie: ResponseMovie?,
    @Json(name = "show") val show: ResponseShow?
) {
    fun asFeaturedMovieEntity() = FeaturedEntity(
        mediaSlug = movie!!.identifiers.slug,
        tag = "Recommended"
    )

    fun asRecommendedMovie(period: String) = RecommendedListEntity(
        mediaSlug = movie!!.identifiers.slug,
        users = userCount,
        period = period
    )

    companion object {
        fun createEnvelopeUserCounts(amount: Int): List<EnvelopeUserCount> {
            val movies = mutableListOf<EnvelopeUserCount>()
            var count = 0
            repeat(amount) {
                movies.add(
                    EnvelopeUserCount(
                        userCount = count,
                        movie = ResponseMovie.createResponseMovies(1)[0],
                        show = null
                    )
                )
                count++
            }
            return movies
        }
    }
}