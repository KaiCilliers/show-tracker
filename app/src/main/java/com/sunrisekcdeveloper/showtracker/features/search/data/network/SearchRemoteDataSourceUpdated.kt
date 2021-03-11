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

import com.sunrisekcdeveloper.showtracker.di.NetworkModule.SearchApiUpdated
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.NetworkResult
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedMovieUpdated
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedShowUpdated
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class SearchRemoteDataSourceUpdated(
    @SearchApiUpdated private val api: SearchServiceContractUpdated,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SearchRemoteDataSourceContractUpdated {
    override suspend fun moviesByTitle(
        query: String,
        page: Int
    ): NetworkResult<EnvelopePaginatedMovieUpdated> = safeApiCall {
        api.searchMoviesByTitle(query = query, page = page)
    }

    override suspend fun showsByTitle(
        query: String,
        page: Int
    ): NetworkResult<EnvelopePaginatedShowUpdated> = safeApiCall {
        api.searchShowByTitle(query = query, page = page)
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