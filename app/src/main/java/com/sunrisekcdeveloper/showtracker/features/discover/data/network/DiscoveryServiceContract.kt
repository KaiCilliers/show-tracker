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

package com.sunrisekcdeveloper.showtracker.features.discover.data.network

import com.sunrisekcdeveloper.showtracker.models.network.base.*
import com.sunrisekcdeveloper.showtracker.models.network.envelopes.*
import retrofit2.Response

interface DiscoveryServiceContract {
    /**
     * Trending movies all movies currently being watched sorted with most
     * watched returned first
     * Supports Pagination, Extended, Filter
     *
     * @return
     */
    suspend fun trendingMovies(
        extended: String = ""
    ): Response<List<EnvelopeWatchers>>

    /**
     * Popular movies popularity calculated using rating percentage and the number
     * of ratings
     * Supports Pagination, Extended, Filter
     *
     * @return
     */
    suspend fun popularMovies(
        extended: String = ""
    ): Response<List<ResponseMovie>>

    /**
     * Most watched movies in specified time period (unique watches)
     * Supports Pagination, Extended, Filter
     *
     * @param period Optional.
     *      Possible values:  daily , weekly , monthly , yearly , all
     *      Default value: weekly
     * @return
     */
    // TODO you can create less methods in Repo that takes params which will decide which of
    //  these methods to call (like it'll choose between most played and most watched)
    suspend fun mostWatchedMovies(
        period: String = "weekly",
        extended: String = ""
    ): Response<List<EnvelopeViewStats>>

    /** IMAGES */
    suspend fun poster(id: String): Response<ResponseImages>

    class Fake : DiscoveryServiceContract {

        var happyPath = true

        override suspend fun trendingMovies(extended: String): Response<List<EnvelopeWatchers>> {
            return if (happyPath) {
                Response.success(EnvelopeWatchers.createEnvelopeWatchers(10))
            } else Response.error(0, null)
        }

        override suspend fun popularMovies(extended: String): Response<List<ResponseMovie>> {
            return if (happyPath) {
                Response.success(ResponseMovie.createResponseMovies(10))
            } else Response.error(0, null)
        }

        override suspend fun mostWatchedMovies(
            period: String,
            extended: String
        ): Response<List<EnvelopeViewStats>> {
            return if (happyPath) {
                Response.success(EnvelopeViewStats.createEnvelopeViewStats(10))
            } else Response.error(0, null)
        }

        override suspend fun poster(id: String): Response<ResponseImages> {
            return if (happyPath) {
                Response.success(ResponseImages.createResponseImages(1)[0])
            } else Response.error(0, null)
        }
    }
}