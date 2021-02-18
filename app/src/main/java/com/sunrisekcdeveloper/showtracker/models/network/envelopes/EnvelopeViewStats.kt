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
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseShow

data class EnvelopeViewStats(
    @Json(name = "watcher_count") val watchers: Int,
    @Json(name = "play_count") val playCount: Int,
    @Json(name = "collected_count") val collectedCount: Int,
    @Json(name = "collector_count") val collectorCount: Int?,
    @Json(name = "show") val show: ResponseShow?,
    @Json(name = "movie") val movie: ResponseMovie?
) {
    companion object {
        fun createEnvelopeViewStats(amount:Int): List<EnvelopeViewStats> {
            val movies = mutableListOf<EnvelopeViewStats>()
            var count = 0
            repeat(amount) {
                movies.add(
                    EnvelopeViewStats(
                        watchers = count,
                        playCount = count,
                        collectedCount = count,
                        collectorCount = count,
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