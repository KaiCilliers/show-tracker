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

package com.sunrisekcdeveloper.search.data.network

import com.sunrisekcdeveloper.network.models.EnvelopePaginatedMovies
import com.sunrisekcdeveloper.network.models.EnvelopePaginatedShows
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceSearch : ServiceSearchContract {
    @GET("search/movie")
    override suspend fun searchMoviesByTitle(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): Response<EnvelopePaginatedMovies>

    @GET("search/tv")
    override suspend fun searchShowByTitle(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): Response<EnvelopePaginatedShows>
}