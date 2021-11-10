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

package com.sunrisekcdeveloper.discovery

import com.sunrisekcdeveloper.discovery.extras.model.EnvelopePaginatedMovies
import com.sunrisekcdeveloper.discovery.extras.model.EnvelopePaginatedShows
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

interface DiscoveryServiceContract {
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

    class Fake() : DiscoveryServiceContract {
        var expectException = false
        override suspend fun popularMovies(
            apiKey: String,
            page: Int
        ): Response<EnvelopePaginatedMovies> {
            return if (expectException) Response.error(404, "Fake - Error fetching popular movies".toResponseBody())
            else Response.success(EnvelopePaginatedMovies.single())
        }

        override suspend fun topRatedMovies(
            apiKey: String,
            page: Int
        ): Response<EnvelopePaginatedMovies> {
            return if (expectException) Response.error(404, "Fake - Error fetching top rated movies".toResponseBody())
            else Response.success(EnvelopePaginatedMovies.single())
        }

        override suspend fun upcomingMovies(
            apiKey: String,
            page: Int
        ): Response<EnvelopePaginatedMovies> {
            return if (expectException) Response.error(404, "Fake - Error fetching upcoming movies".toResponseBody())
            else Response.success(EnvelopePaginatedMovies.single())
        }

        override suspend fun popularShows(
            apiKey: String,
            page: Int
        ): Response<EnvelopePaginatedShows> {
            return if (expectException) Response.error(404, "Fake - Error fetching popular shows".toResponseBody())
            else Response.success(EnvelopePaginatedShows.single())
        }

        override suspend fun topRatedShows(
            apiKey: String,
            page: Int
        ): Response<EnvelopePaginatedShows> {
            return if (expectException) Response.error(404, "Fake - Error fetching top rated shows".toResponseBody())
            else Response.success(EnvelopePaginatedShows.single())
        }

        override suspend fun airingTodayShows(
            apiKey: String,
            page: Int
        ): Response<EnvelopePaginatedShows> {
            return if (expectException) Response.error(404, "Fake - Error fetching airing today shows".toResponseBody())
            else Response.success(EnvelopePaginatedShows.single())
        }
    }
}