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

import com.sunrisekcdeveloper.showtracker.BuildConfig
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedMovies
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedShows
import retrofit2.Response

interface ServiceDiscoveryContract {
    suspend fun popularMovies(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        page: Int
    ): Response<EnvelopePaginatedMovies>

    suspend fun topRatedMovies(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        page: Int
    ): Response<EnvelopePaginatedMovies>

    suspend fun upcomingMovies(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        page: Int
    ): Response<EnvelopePaginatedMovies>

    suspend fun popularShows(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        page: Int
    ): Response<EnvelopePaginatedShows>

    suspend fun topRatedShows(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        page: Int
    ): Response<EnvelopePaginatedShows>

    suspend fun airingTodayShows(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        page: Int
    ): Response<EnvelopePaginatedShows>
}