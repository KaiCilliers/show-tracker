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

package com.sunrisekcdeveloper.discovery.impl

import com.sunrisekcdeveloper.discovery.DiscoveryRemoteDataSourceContract
import com.sunrisekcdeveloper.discovery.DiscoveryServiceContract
import com.sunrisekcdeveloper.discovery.extras.model.EnvelopePaginatedMovies
import com.sunrisekcdeveloper.discovery.extras.model.EnvelopePaginatedShows
import com.sunrisekcdeveloper.network.NetworkContract
import com.sunrisekcdeveloper.network.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DiscoveryRemoteDataSource(
    private val api: DiscoveryServiceContract,
    private val network: NetworkContract
) : DiscoveryRemoteDataSourceContract {
    override suspend fun popularMovies(page: Int): NetworkResult<EnvelopePaginatedMovies> = network.request {
        api.popularMovies(page = page)
    }

    override suspend fun topRatedMovies(page: Int): NetworkResult<EnvelopePaginatedMovies> = network.request {
        api.topRatedMovies(page = page)
    }

    override suspend fun upcomingMovies(page: Int): NetworkResult<EnvelopePaginatedMovies> = network.request {
        api.upcomingMovies(page = page)
    }

    override suspend fun popularShows(page: Int): NetworkResult<EnvelopePaginatedShows> = network.request {
        api.popularShows(page = page)
    }

    override suspend fun topRatedShows(page: Int): NetworkResult<EnvelopePaginatedShows> = network.request {
        api.topRatedShows(page = page)
    }

    override suspend fun airingTodayShows(page: Int): NetworkResult<EnvelopePaginatedShows> = network.request {
        api.airingTodayShows(page = page)
    }
}