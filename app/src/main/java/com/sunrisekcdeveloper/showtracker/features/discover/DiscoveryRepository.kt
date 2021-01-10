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

package com.sunrisekcdeveloper.showtracker.features.discover

import com.sunrisekcdeveloper.showtracker.di.NetworkModule
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DiscoveryClient
import com.sunrisekcdeveloper.showtracker.features.discover.models.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

class DiscoveryRepository(
    private val local: DiscoveryDao,
    @DiscoveryClient private val remote: DiscoveryDataSourceContract
) : DiscoveryRepositoryContract {

    private val ioScope = CoroutineScope(Job() + Dispatchers.IO)
    private val cpuScope = CoroutineScope(Job() + Dispatchers.Default)

    override suspend fun trendingMovie(): List<Movie> {
        val result = withContext(ioScope.coroutineContext) { local.trendingMovies() }
        return withContext(cpuScope.coroutineContext) {
            result.map { item ->
                item.movie?.asDomain()!!
            }
        }
    }

    override suspend fun popularMovie(): List<Movie> {
        val result = withContext(ioScope.coroutineContext) { local.popularMovies() }
        return withContext(cpuScope.coroutineContext) {
            result.map { item ->
                item.movie?.asDomain()!!
            }
        }
    }

    override suspend fun boxofficeMovie(): List<Movie> {
        val result = withContext(ioScope.coroutineContext) { local.boxOfficeMovies() }
        return withContext(cpuScope.coroutineContext) {
            result.map { item ->
                item.movie?.asDomain()!!
            }
        }
    }

    override suspend fun mostPlayedMovie(): List<Movie> {
        val result = withContext(ioScope.coroutineContext) { local.mostPlayedMovies() }
        return withContext(cpuScope.coroutineContext) {
            result.map { item ->
                item.movie?.asDomain()!!
            }
        }
    }

    override suspend fun mostWatchedMovie(): List<Movie> {
        val result = withContext(ioScope.coroutineContext) { local.mostWatchedMovies() }
        return withContext(cpuScope.coroutineContext) {
            result.map { item ->
                item.movie?.asDomain()!!
            }
        }
    }

    override suspend fun mostAnticipatedMovie(): List<Movie> {
        val result = withContext(ioScope.coroutineContext) { local.mostAnticipatedMovies() }
        return withContext(cpuScope.coroutineContext) {
            result.map { item ->
                item.movie?.asDomain()!!
            }
        }
    }

    override suspend fun recommendedMovie(): List<Movie> {
        val result = withContext(ioScope.coroutineContext) { local.recommended() }
        return withContext(cpuScope.coroutineContext) {
            result.map { item ->
                item.movie?.asDomain()!!
            }
        }
    }
}

interface DiscoveryRepositoryContract {
    suspend fun trendingMovie(): List<Movie>
    suspend fun popularMovie(): List<Movie>
    suspend fun boxofficeMovie(): List<Movie>
    suspend fun mostPlayedMovie(): List<Movie>
    suspend fun mostWatchedMovie(): List<Movie>
    suspend fun mostAnticipatedMovie(): List<Movie>
    suspend fun recommendedMovie(): List<Movie>
}