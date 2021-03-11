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

import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedMovieUpdated
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.EnvelopePaginatedShowUpdated

interface DiscoveryRemoteDataSourceContractUpdated {
    suspend fun popularMovies(page: Int): NetworkResult<EnvelopePaginatedMovieUpdated>
    suspend fun topRatedMovies(page: Int): NetworkResult<EnvelopePaginatedMovieUpdated>
    suspend fun upcomingMovies(page: Int): NetworkResult<EnvelopePaginatedMovieUpdated>
    suspend fun popularShows(page: Int): NetworkResult<EnvelopePaginatedShowUpdated>
    suspend fun topRatedShows(page: Int): NetworkResult<EnvelopePaginatedShowUpdated>
    suspend fun airingTodayShows(page: Int): NetworkResult<EnvelopePaginatedShowUpdated>
}
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(message: String) = Error(message)
    }
}