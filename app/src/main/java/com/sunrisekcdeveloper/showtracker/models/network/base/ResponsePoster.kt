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

/**
 * Response Poster represents a network object containing information primarily a url to a poster
 * of a movie or show
 *
 * @property id is a network id associated with the specific poster
 * @property url is where the poster image is located
 * @property lang is the language the poster is written in
 * @property likes is the amount of likes the poster has received
 */
data class ResponsePoster(
    @Json(name = "id") val id: String,
    @Json(name = "url") val url: String,
    @Json(name = "lang") val lang: String,
    @Json(name = "likes") val likes: String
) {
    companion object {
        fun createResponsePoster(amount: Int): List<ResponsePoster> {
            val movies = mutableListOf<ResponsePoster>()
            var count = 0
            repeat(amount) {
                movies.add(
                    ResponsePoster(
                        id = "id$count",
                        url = "url$count",
                        lang = "language$count",
                        likes = "likes$count"
                    )
                )
                count++
            }
            return movies
        }
    }
}