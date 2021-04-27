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

import com.sunrisekcdeveloper.showtracker.common.util.NetworkResult
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedMovies
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedShows

interface RemoteDataSourceSearchContract {
    suspend fun moviesByTitle(query: String, page: Int) : NetworkResult<EnvelopePaginatedMovies>
    suspend fun showsByTitle(query: String, page: Int) : NetworkResult<EnvelopePaginatedShows>
    class Fake() : RemoteDataSourceSearchContract {
        var expectException = false
        override suspend fun moviesByTitle(
            query: String,
            page: Int
        ): NetworkResult<EnvelopePaginatedMovies> {
            return if (expectException) NetworkResult.error("Fake - Error fetching Movies with query: \"$query\"")
            else NetworkResult.success(EnvelopePaginatedMovies.single())
        }

        override suspend fun showsByTitle(
            query: String,
            page: Int
        ): NetworkResult<EnvelopePaginatedShows> {
            return if (expectException) NetworkResult.error("Fake - Error fetching Shows with query: \"$query\"")
            else NetworkResult.success(EnvelopePaginatedShows.single())
        }
    }
}