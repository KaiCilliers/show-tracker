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

package com.sunrisekcdeveloper.showtracker.features.discover.client

import com.sunrisekcdeveloper.showtracker.models.network.base.*
import com.sunrisekcdeveloper.showtracker.models.network.envelopes.*
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.math.log

interface DiscoveryServiceContract {
    /**
     * Trending movies all movies currently being watched sorted with most
     * watched returned first
     * Supports Pagination, Extended, Filter
     *
     * @return
     */
    suspend fun trendingMovies(): Response<List<EnvelopeWatchers>>

    /**
     * Popular movies popularity calculated using rating percentage and the number
     * of ratings
     * Supports Pagination, Extended, Filter
     *
     * @return
     */
    suspend fun popularMovies(): Response<List<ResponseMovie>>

    /**
     * Recommended movies in specified time period
     * Supports Pagination, Extended, Filter
     *
     * TODO implement the optional query parameters to calls that can be Extended
     *
     * @param period Optional.
     *      Possible values:  daily , weekly , monthly , yearly , all
     *      Default value: weekly
     * @return
     */
    suspend fun recommendedMovies(
        period: String = "weekly",
        extended: String = ""
    ): Response<List<EnvelopeUserCount>>

    /**
     * Most played movies in specified time period (single account can watch multiple times)
     * Supports Pagination, Extended, Filter
     *
     * @param period Optional.
     *      Possible values:  daily , weekly , monthly , yearly , all
     *      Default value: weekly
     * @return
     */
    suspend fun mostPlayedMovies(period: String = "weekly"): Response<List<EnvelopeViewStats>>

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
    suspend fun mostWatchedMovies(period: String = "weekly"): Response<List<EnvelopeViewStats>>

    /**
     * Most anticipated based on number of lists movie appears in
     * Supports Pagination, Extended, Filter
     *
     * @return
     */
    suspend fun mostAnticipated(): Response<List<EnvelopeListCount>>

    /**
     * Box office based on top 10 grossing movies in U.S. box office the weekend past. Updates
     * every Monday morning
     * Supports Extended
     *
     * @return
     */
    suspend fun boxOffice(): Response<List<EnvelopeRevenue>>

    /** IMAGES */
    suspend fun poster(id: String): Response<ResponseImages>

    class Fake() : DiscoveryServiceContract {

        var happyPath = true

        override suspend fun trendingMovies(): Response<List<EnvelopeWatchers>> {
            return if (happyPath) {
                Response.success(EnvelopeWatchers.createEnvelopeWatchers(10))
            } else Response.error(0, null)
        }

        override suspend fun popularMovies(): Response<List<ResponseMovie>> {
            return if (happyPath) {
                Response.success(ResponseMovie.createResponseMovies(10))
            } else Response.error(0, null)
        }

        override suspend fun recommendedMovies(
            period: String,
            extended: String
        ): Response<List<EnvelopeUserCount>> {
            return if (happyPath) {
                Response.success(EnvelopeUserCount.createEnvelopeUserCounts(10))
            } else Response.error(0, null)
        }

        override suspend fun mostPlayedMovies(period: String): Response<List<EnvelopeViewStats>> {
            return if (happyPath) {
                Response.success(EnvelopeViewStats.createEnvelopeViewStats(10))
            } else Response.error(0, null)
        }

        override suspend fun mostWatchedMovies(period: String): Response<List<EnvelopeViewStats>> {
            return if (happyPath) {
                Response.success(EnvelopeViewStats.createEnvelopeViewStats(10))
            } else Response.error(0, null)
        }

        override suspend fun mostAnticipated(): Response<List<EnvelopeListCount>> {
            return if (happyPath) {
                Response.success(EnvelopeListCount.createEnvelopeListCounts(10))
            } else Response.error(0, null)
        }

        override suspend fun boxOffice(): Response<List<EnvelopeRevenue>> {
            return if (happyPath) {
                Response.success(EnvelopeRevenue.createEnvelopeRevenues(10))
            } else Response.error(0, null)
        }

        override suspend fun poster(id: String): Response<ResponseImages> {
            return if (happyPath) {
                Response.success(ResponseImages.createResponseImages(1)[0])
            } else Response.error(0, null)
        }
    }
}