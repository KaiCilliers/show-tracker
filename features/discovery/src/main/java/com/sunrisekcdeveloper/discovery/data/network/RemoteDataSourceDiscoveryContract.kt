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

package com.sunrisekcdeveloper.discovery.data.network

import com.sunrisekcdeveloper.discovery.data.network.model.EnvelopePaginatedMovies
import com.sunrisekcdeveloper.discovery.data.network.model.EnvelopePaginatedShows
import com.sunrisekcdeveloper.network.NetworkResult

interface RemoteDataSourceDiscoveryContract {
    suspend fun popularMovies(page: Int): NetworkResult<EnvelopePaginatedMovies>
    suspend fun topRatedMovies(page: Int): NetworkResult<EnvelopePaginatedMovies>
    suspend fun upcomingMovies(page: Int): NetworkResult<EnvelopePaginatedMovies>
    suspend fun popularShows(page: Int): NetworkResult<EnvelopePaginatedShows>
    suspend fun topRatedShows(page: Int): NetworkResult<EnvelopePaginatedShows>
    suspend fun airingTodayShows(page: Int): NetworkResult<EnvelopePaginatedShows>

    class Fake() : RemoteDataSourceDiscoveryContract {
        var expectException = false
        override suspend fun popularMovies(page: Int): NetworkResult<EnvelopePaginatedMovies> {
            return if (expectException) NetworkResult.error("Fake - Error fetching Popular Movies (page=$page)")
            else NetworkResult.success(EnvelopePaginatedMovies.single())
        }

        override suspend fun topRatedMovies(page: Int): NetworkResult<EnvelopePaginatedMovies> {
            return if (expectException) NetworkResult.error("Fake - Error fetching Top Rated Movies (page=$page)")
            else NetworkResult.success(EnvelopePaginatedMovies.single())
        }

        override suspend fun upcomingMovies(page: Int): NetworkResult<EnvelopePaginatedMovies> {
            return if (expectException) NetworkResult.error("Fake - Error fetching Upcoming Movies (page=$page)")
            else NetworkResult.success(EnvelopePaginatedMovies.single())
        }

        override suspend fun popularShows(page: Int): NetworkResult<EnvelopePaginatedShows> {
            return if (expectException) NetworkResult.error("Fake - Error fetching Popular Shows (page=$page)")
            else NetworkResult.success(EnvelopePaginatedShows.single())
        }

        override suspend fun topRatedShows(page: Int): NetworkResult<EnvelopePaginatedShows> {
            return if (expectException) NetworkResult.error("Fake - Error fetching Top Rated Shows (page=$page)")
            else NetworkResult.success(EnvelopePaginatedShows.single())
        }

        override suspend fun airingTodayShows(page: Int): NetworkResult<EnvelopePaginatedShows> {
            return if (expectException) NetworkResult.error("Fake - Error fetching Airing Today Shows (page=$page)")
            else NetworkResult.success(EnvelopePaginatedShows.single())
        }
    }
}