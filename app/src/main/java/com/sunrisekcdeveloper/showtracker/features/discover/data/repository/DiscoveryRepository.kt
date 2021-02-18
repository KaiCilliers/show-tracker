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

package com.sunrisekcdeveloper.showtracker.features.discover.data.repository

import com.sunrisekcdeveloper.showtracker.commons.util.asMediaEntity
import com.sunrisekcdeveloper.showtracker.commons.util.asRecentlyAddedEntity
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DiscoveryClient
import com.sunrisekcdeveloper.showtracker.features.discover.data.local.DiscoveryDao
import com.sunrisekcdeveloper.showtracker.features.discover.data.network.DiscoveryRemoteDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.discover.domain.repository.DiscoveryRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.EnvelopePaginatedMovie
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.ResponseMovieTMDB
import kotlinx.coroutines.*
import timber.log.Timber

class DiscoveryRepository(
    @DiscoveryClient private val remote: DiscoveryRemoteDataSourceContract,
    private val dao: DiscoveryDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : DiscoveryRepositoryContract {
    override suspend fun popularMovies(page: Int): Resource<EnvelopePaginatedMovie> {
        val response = remote.popularMovies(page)
        saveMedia(response)
        return response
    }

    override suspend fun topRatedMovies(page: Int): Resource<EnvelopePaginatedMovie> {
        val response = remote.topRatedMovies(page)
        saveMedia(response)
        return response
    }

    override suspend fun upcomingMovies(page: Int): Resource<EnvelopePaginatedMovie> {
        val response = remote.upcomingMovies(page)
        saveMedia(response)
        return response
    }

    override suspend fun saveMediaToWatchList(media: ResponseMovieTMDB) {
        dao.insertRecentlyAddedMedia(media.asRecentlyAddedEntity())
    }

    private suspend fun saveMedia(input: Resource<EnvelopePaginatedMovie>) {
        if (input is Resource.Success) {
            input.data.movies.forEach {
                dao.insertMedia(it.asMediaEntity("movie"))
            }
        }
    }
}