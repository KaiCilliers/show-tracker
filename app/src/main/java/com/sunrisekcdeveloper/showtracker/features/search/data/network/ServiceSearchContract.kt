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

package com.sunrisekcdeveloper.showtracker.features.search.data.network

import com.sunrisekcdeveloper.showtracker.BuildConfig
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedMovies
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedShows
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

interface ServiceSearchContract {
    suspend fun searchMoviesByTitle(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        page: Int,
        query: String
    ) : Response<EnvelopePaginatedMovies>

    suspend fun searchShowByTitle(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        page: Int,
        query: String
    ) : Response<EnvelopePaginatedShows>

    class Fake() : ServiceSearchContract {
        var expectException = false
        override suspend fun searchMoviesByTitle(
            apiKey: String,
            page: Int,
            query: String
        ): Response<EnvelopePaginatedMovies> {
            return if (expectException) Response.error(404, "Fake - Error fetching Movies".toResponseBody())
            else Response.success(EnvelopePaginatedMovies.single())
        }

        override suspend fun searchShowByTitle(
            apiKey: String,
            page: Int,
            query: String
        ): Response<EnvelopePaginatedShows> {
            return if (expectException) Response.error(404, "Fake - Error fetching Shows".toResponseBody())
            else Response.success(EnvelopePaginatedShows.single())
        }
    }
}