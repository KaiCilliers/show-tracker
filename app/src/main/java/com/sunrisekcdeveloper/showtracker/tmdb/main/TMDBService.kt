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

package com.sunrisekcdeveloper.showtracker.tmdb.main

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBService {
    @GET("movie/popular")
    suspend fun popularMovies(
        @Query("api_key") apiKey: String = "9b3b6234bb46dbbd68fedc64b4d46e63",
        @Query("page") page: Int
    ): Response<EnvelopePageMovieTMDB>

    @GET("movie/top_rated")
    suspend fun topRatedMovies(
        @Query("api_key") apiKey: String = "9b3b6234bb46dbbd68fedc64b4d46e63",
        @Query("page") page: Int
    ): Response<EnvelopePageMovieTMDB>

    @GET("movie/upcoming")
    suspend fun upcomingMovies(
        @Query("api_key") apiKey: String = "9b3b6234bb46dbbd68fedc64b4d46e63",
        @Query("page") page: Int
    ): Response<EnvelopePageMovieTMDB>
}