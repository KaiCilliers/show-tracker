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
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponsePoster.Companion.createResponsePoster

/**
 * Response Image represents a network response which consists of a movie or show name with all of
 * its associated poster urls
 *
 * @property name is the name of the movie or show
 * @property posters is a list of [ResponsePoster]
 */
data class ResponseImages(
    @Json(name = "name") val name: String,
    @Json(name = "movieposter") val posters: List<ResponsePoster>?,
    @Json(name = "moviedisc") val disc: List<ResponsePoster>?,
    @Json(name = "hdmovielogo") val logo: List<ResponsePoster>?,
    @Json(name = "moviebackground") val background: List<ResponsePoster>?,
    @Json(name = "moviethumb") val thumb: List<ResponsePoster>?,
    @Json(name = "hdmovieclearart") val clearArt: List<ResponsePoster>?,
    @Json(name = "moviebanner") val banner: List<ResponsePoster>?
) {
    companion object {
        fun createResponseImages(amount: Int): List<ResponseImages> {
            val movies = mutableListOf<ResponseImages>()
            var count = 0
            repeat(amount) {
                movies.add(
                    ResponseImages(
                        name = "name$count",
                        posters = createResponsePoster(10),
                        disc = createResponsePoster(10),
                        logo = createResponsePoster(10),
                        background = createResponsePoster(10),
                        thumb = createResponsePoster(10),
                        clearArt = createResponsePoster(10),
                        banner = createResponsePoster(10)
                    )
                )
                count++
            }
            return movies
        }
    }
}