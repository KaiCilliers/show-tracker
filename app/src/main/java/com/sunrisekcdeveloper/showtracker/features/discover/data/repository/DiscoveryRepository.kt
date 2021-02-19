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

import com.sunrisekcdeveloper.showtracker.commons.util.*
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DiscoveryClient
import com.sunrisekcdeveloper.showtracker.features.discover.data.local.DiscoveryDao
import com.sunrisekcdeveloper.showtracker.features.discover.data.network.DiscoveryRemoteDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.DiscoveryPopularEntity
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.DiscoveryType
import com.sunrisekcdeveloper.showtracker.features.discover.domain.repository.DiscoveryRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.EnvelopePaginatedMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.MediaModelSealed
import kotlinx.coroutines.*
import timber.log.Timber

// TODO network results saves to local in wrong order
// TODO send database results first then refresh local data if needed
class DiscoveryRepository(
    @DiscoveryClient private val remote: DiscoveryRemoteDataSourceContract,
    private val dao: DiscoveryDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : DiscoveryRepositoryContract {

    override suspend fun popularMovies(page: Int): Resource<List<MediaModelSealed>> {
        return when (val response = remote.popularMovies(page)) {
            is Resource.Success -> {
                saveMedia(response)
                persisDiscoveryMedia(
                    response.data.movies.map { it.asMediaModel(MediaType.MOVIE) },
                    DiscoveryType.POPULAR
                )
                Resource.Success(
                    dao.popularList().map { it.asMediaModel(MediaType.MOVIE) }
                )
            }
            is Resource.Error -> Resource.Error(response.message)
            Resource.Loading -> Resource.Loading
        }
    }

    override suspend fun topRatedMovies(page: Int): Resource<List<MediaModelSealed>> {
        return when (val response = remote.topRatedMovies(page)) {
            is Resource.Success -> {
                saveMedia(response)
                persisDiscoveryMedia(
                    response.data.movies.map { it.asMediaModel(MediaType.MOVIE) },
                    DiscoveryType.TOP_RATED
                )
                Resource.Success(
                    dao.topRatedList().map { it.asMediaModel(MediaType.MOVIE) }
                )
            }
            is Resource.Error -> Resource.Error(response.message)
            Resource.Loading -> Resource.Loading
        }
    }

    override suspend fun upcomingMovies(page: Int): Resource<List<MediaModelSealed>> {
        return when (val response = remote.upcomingMovies(page)) {
            is Resource.Success -> {
                saveMedia(response)
                persisDiscoveryMedia(
                    response.data.movies.map { it.asMediaModel(MediaType.MOVIE) },
                    DiscoveryType.UPCOMING
                )
                Resource.Success(
                    dao.upcomingList().map { it.asMediaModel(MediaType.MOVIE) }
                )
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

    private suspend fun persisDiscoveryMedia(media: List<MediaModelSealed>, type: DiscoveryType) {
        when (type) {
            DiscoveryType.POPULAR -> {
                dao.insertPopularEntity(*media.map { it.asDiscoveryPopularEntity() }.toTypedArray())
            }
            DiscoveryType.TOP_RATED -> {
                dao.insertTopRatedEntity(*media.map { it.asDiscoveryTopRatedEntity() }.toTypedArray())
            }
            DiscoveryType.UPCOMING -> {
                dao.insertUpcomingEntity(*media.map { it.asDiscoveryUpcomingEntity() }.toTypedArray())
            }
        }
    }

    private suspend fun saveMedia(input: Resource<EnvelopePaginatedMovie>) {
        if (input is Resource.Success) {
            input.data.movies.forEach {
                dao.insertMedia(it.asMediaEntity("movie"))
            }
        }
    }
}