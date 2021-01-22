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
import com.sunrisekcdeveloper.showtracker.models.local.categories.BoxOfficeListEntity
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseMovie

data class EnvelopeRevenue(
    @Json(name = "revenue") val revenue: Int,
    @Json(name = "movie") val movie: ResponseMovie
) {
    fun asEntity() = BoxOfficeListEntity(
        mediaSlug = movie.identifiers.slug,
        revenue = revenue
    )

    companion object {
        fun createEnvelopeRevenues(amount: Int): List<EnvelopeRevenue> {
            val movies = mutableListOf<EnvelopeRevenue>()
            var count = 0
            repeat(amount) {
                movies.add(
                    EnvelopeRevenue(
                        revenue = count,
                        movie = ResponseMovie.createResponseMovies(1)[0]
                    )
                )
                count++
            }
            return movies
        }
    }
}