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

import com.sunrisekcdeveloper.network.NetworkResult
import com.sunrisekcdeveloper.network.RemoteDataSourceBase
import com.sunrisekcdeveloper.network.models.EnvelopePaginatedMovies
import com.sunrisekcdeveloper.network.models.EnvelopePaginatedShows
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RemoteDataSourceSearch(
    private val api: ServiceSearchContract,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteDataSourceSearchContract, RemoteDataSourceBase(dispatcher) {
    override suspend fun moviesByTitle(
        query: String,
        page: Int
    ): NetworkResult<EnvelopePaginatedMovies> = safeApiCall {
        api.searchMoviesByTitle(query = query, page = page)
    }

    override suspend fun showsByTitle(
        query: String,
        page: Int
    ): NetworkResult<EnvelopePaginatedShows> = safeApiCall {
        api.searchShowByTitle(query = query, page = page)
    }
}