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

package com.sunrisekcdeveloper.showtracker.models.network.envelopes

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.models.local.categories.TrendingListEntity
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseShow

/**
 * Response Watcher represents a network response which consists of a [ResponseMovie] with the
 * amount of current [watchers] of the [ResponseMovie]
 *
 * TODO update documentation
 *
 * @property watchers is the amount of accounts currently watching the [ResponseMovie]
 * @property movie represents a [ResponseMovie] object
 */
data class EnvelopeWatchers(
    @Json(name = "watchers") val watchers: Int,
    @Json(name = "movie") val movie: ResponseMovie?,
    @Json(name = "show") val show: ResponseShow?
) {
    fun asTrendingMovieEntity() = TrendingListEntity(
        mediaSlug = movie!!.identifiers.slug,
        watchers = watchers
    )

    companion object {
        fun createEnvelopeWatchers(amount: Int): List<EnvelopeWatchers> {
            val movies = mutableListOf<EnvelopeWatchers>()
            var count = 0
            repeat(amount) {
                movies.add(
                    EnvelopeWatchers(
                        watchers = count,
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