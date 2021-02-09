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

import com.sunrisekcdeveloper.showtracker.BuildConfig
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseImages
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.models.network.envelopes.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface DiscoveryService : DiscoveryServiceContract {
    @GET("movies/trending")
    override suspend fun trendingMovies(): Response<List<EnvelopeWatchers>>

    @GET("movies/popular")
    override suspend fun popularMovies(): Response<List<ResponseMovie>>

    // TODO implement this on all Extended calls
    @GET("movies/recommended/{period}")
    override suspend fun recommendedMovies(
        @Path("period") period: String,
        @Query("extended") extended: String
    ): Response<List<EnvelopeUserCount>>

    @GET("movies/played/{period}")
    override suspend fun mostPlayedMovies(@Path("period") period: String): Response<List<EnvelopeViewStats>>

    @GET("movies/watched/{period}")
    override suspend fun mostWatchedMovies(@Path("period") period: String): Response<List<EnvelopeViewStats>>

    @GET("movies/anticipated")
    override suspend fun mostAnticipated(): Response<List<EnvelopeListCount>>

    @GET("movies/boxoffice")
    override suspend fun boxOffice(): Response<List<EnvelopeRevenue>>

    /** IMAGES */
    @Headers("Fanart-Api: true")
    @GET("${BuildConfig.FANART_BASE_URL}movies/{id}?api_key=${BuildConfig.FANART_API_KEY}")
    override suspend fun poster(@Path("id") id: String): Response<ResponseImages>
}