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

import com.sunrisekcdeveloper.showtracker.BuildConfig
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.EnvelopePaginatedMovie
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.EnvelopePaginatedShow
import retrofit2.Response

interface DiscoveryServiceContract {

    suspend fun popularMovies(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        page: Int
    ): Response<EnvelopePaginatedMovie>

    suspend fun topRatedMovies(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        page: Int
    ): Response<EnvelopePaginatedMovie>

    suspend fun upcomingMovies(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        page: Int
    ): Response<EnvelopePaginatedMovie>

    suspend fun popularShows(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        page: Int
    ): Response<EnvelopePaginatedShow>

    suspend fun topRatedShows(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        page: Int
    ): Response<EnvelopePaginatedShow>

    suspend fun latestShows(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        page: Int
    ): Response<EnvelopePaginatedShow>
}