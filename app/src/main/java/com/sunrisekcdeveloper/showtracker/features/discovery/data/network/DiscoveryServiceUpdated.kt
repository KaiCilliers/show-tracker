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

package com.sunrisekcdeveloper.showtracker.features.discovery.data.network

import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedMovieUpdated
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedShowUpdated
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoveryServiceUpdated : DiscoveryServiceContractUpdated {
    @GET("movie/popular")
    override suspend fun popularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<EnvelopePaginatedMovieUpdated>

    @GET("movie/top_rated")
    override suspend fun topRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<EnvelopePaginatedMovieUpdated>

    @GET("movie/upcoming")
    override suspend fun upcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<EnvelopePaginatedMovieUpdated>

    @GET("tv/popular")
    override suspend fun popularShows(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<EnvelopePaginatedShowUpdated>

    @GET("tv/top_rated")
    override suspend fun topRatedShows(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<EnvelopePaginatedShowUpdated>

    @GET("tv/airing_today")
    override suspend fun airingTodayShows(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<EnvelopePaginatedShowUpdated>
}