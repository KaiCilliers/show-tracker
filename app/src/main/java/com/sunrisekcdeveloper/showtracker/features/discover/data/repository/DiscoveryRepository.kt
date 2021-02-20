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
import com.sunrisekcdeveloper.showtracker.commons.util.asMediaModel
import com.sunrisekcdeveloper.showtracker.commons.util.asWatchListEntity
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DiscoveryClient
import com.sunrisekcdeveloper.showtracker.features.discover.data.local.DiscoveryDao
import com.sunrisekcdeveloper.showtracker.features.discover.data.network.DiscoveryRemoteDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.discover.domain.repository.DiscoveryRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.EnvelopePaginatedMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.MediaModelSealed
import kotlinx.coroutines.*

class DiscoveryRepository(
    @DiscoveryClient private val remote: DiscoveryRemoteDataSourceContract,
    private val dao: DiscoveryDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : DiscoveryRepositoryContract {

    override suspend fun popularMovies(page: Int): Resource<List<MediaModelSealed>> {
        val response = remote.popularMovies(page)
        return when (response) {
            is Resource.Success -> {
                saveMedia(response)
                Resource.Success(response.data.media.map { it.asMediaModel() })
            }
            is Resource.Error -> Resource.Error(response.message)
            Resource.Loading -> Resource.Loading
        }
    }

    override suspend fun topRatedMovies(page: Int): Resource<List<MediaModelSealed>> {
        return when (val response = remote.topRatedMovies(page)) {
            is Resource.Success -> {
                saveMedia(response)
                Resource.Success(response.data.media.map { it.asMediaModel() })
            }
            is Resource.Error -> Resource.Error(response.message)
            Resource.Loading -> Resource.Loading
        }
    }

    override suspend fun upcomingMovies(page: Int): Resource<List<MediaModelSealed>> {
        return when (val response = remote.upcomingMovies(page)) {
            is Resource.Success -> {
                saveMedia(response)
                Resource.Success(response.data.media.map { it.asMediaModel() })
            }
            is Resource.Error -> Resource.Error(response.message)
            Resource.Loading -> Resource.Loading
        }
    }

    override suspend fun saveMediaToWatchList(media: MediaModelSealed) {
        when (media) {
            is MediaModelSealed.ShowModel -> TODO()
            is MediaModelSealed.MovieModel -> dao.insertWatchListEntity(media.asWatchListEntity())
        }
    }

    private suspend fun saveMedia(input: Resource<EnvelopePaginatedMovie>) {
        if (input is Resource.Success) {
            input.data.media.forEach {
                dao.insertMovieDump(it.asMediaEntity())
            }
        }
    }
}