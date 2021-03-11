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

import com.sunrisekcdeveloper.showtracker.common.NetworkResult
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedMovieUpdated
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedShowUpdated
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class RemoteDataSourceDiscovery(
    private val api: ServiceDiscoveryContract,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteDataSourceDiscoveryContract {
    override suspend fun popularMovies(page: Int): NetworkResult<EnvelopePaginatedMovieUpdated> = safeApiCall {
        api.popularMovies(page = page)
    }

    override suspend fun topRatedMovies(page: Int): NetworkResult<EnvelopePaginatedMovieUpdated> = safeApiCall {
        api.topRatedMovies(page = page)
    }

    override suspend fun upcomingMovies(page: Int): NetworkResult<EnvelopePaginatedMovieUpdated> = safeApiCall {
        api.upcomingMovies(page = page)
    }

    override suspend fun popularShows(page: Int): NetworkResult<EnvelopePaginatedShowUpdated> = safeApiCall {
        api.popularShows(page = page)
    }

    override suspend fun topRatedShows(page: Int): NetworkResult<EnvelopePaginatedShowUpdated> = safeApiCall {
        api.topRatedShows(page = page)
    }

    override suspend fun airingTodayShows(page: Int): NetworkResult<EnvelopePaginatedShowUpdated> = safeApiCall {
        api.airingTodayShows(page = page)
    }

    private suspend fun <T> safeApiCall(request: suspend () -> Response<T>): NetworkResult<T> =
        withContext(dispatcher) {
            return@withContext try {
                val response = request()
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let { data ->
                        return@withContext NetworkResult.success(data)
                    }
                }
                NetworkResult.error("${response.code()} ${response.message()}")
            } catch (e: Exception) {
                NetworkResult.error((e.message ?: e.toString()))
            }
        }
}