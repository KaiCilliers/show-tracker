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

import com.sunrisekcdeveloper.showtracker.commons.BaseRemoteDataSource
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DiscoveryApi
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.EnvelopePaginatedMovie

class DiscoveryRemoteDataSource(
    @DiscoveryApi private val api: DiscoveryServiceContract
) : DiscoveryRemoteDataSourceContract, BaseRemoteDataSource() {
    override suspend fun popularMovies(page: Int): Resource<EnvelopePaginatedMovie> = safeApiCall {
        api.popularMovies(page = page)
    }

    override suspend fun topRatedMovies(page: Int): Resource<EnvelopePaginatedMovie> = safeApiCall {
        api.topRatedMovies(page = page)
    }

    override suspend fun upcomingMovies(page: Int): Resource<EnvelopePaginatedMovie> = safeApiCall {
        api.upcomingMovies(page = page)
    }
}

