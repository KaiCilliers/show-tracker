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

import com.sunrisekcdeveloper.showtracker.common.NetworkResult
import com.sunrisekcdeveloper.showtracker.common.base.RemoteDataSourceBase
import com.sunrisekcdeveloper.showtracker.di.ModuleNetwork.ApiSearch
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedMovies
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedShows
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class RemoteDataSourceSearch(
    @ApiSearch private val api: ServiceSearchContract,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteDataSourceSearchContract, RemoteDataSourceBase(dispatcher) {
    override suspend fun moviesByTitle(
        query: String,
        page: Int
    ): NetworkResult<EnvelopePaginatedMovies> = safeApiCall {
        Timber.e("inside datasource...")
        api.searchMoviesByTitle(query = query, page = page)
    }

    override suspend fun showsByTitle(
        query: String,
        page: Int
    ): NetworkResult<EnvelopePaginatedShows> = safeApiCall {
        api.searchShowByTitle(query = query, page = page)
    }
}