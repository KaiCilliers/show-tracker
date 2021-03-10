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

package com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.repository

import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.network.DiscoveryRemoteDataSourceContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.network.NetworkResult
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.network.model.ResponseStandardMediaUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.domain.model.DiscoveryUIModel
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.domain.model.ListType
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.domain.model.MediaTypeUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.domain.repository.DiscoveryRepositoryContractUpdated

class DiscoveryRepositoryUpdated(
    private val remote: DiscoveryRemoteDataSourceContractUpdated
) : DiscoveryRepositoryContractUpdated {
    override suspend fun popularMovies(page: Int): Resource<List<DiscoveryUIModel>> {
        return when (val response = remote.popularMovies(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asDiscoveryUIModel(ListType.MoviePopular) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.message)
            }
        }
    }

    override suspend fun topRatedMovies(page: Int): Resource<List<DiscoveryUIModel>> {
        return when (val response = remote.topRatedMovies(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asDiscoveryUIModel(ListType.MovieTopRated) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.message)
            }
        }
    }

    override suspend fun upcomingMovies(page: Int): Resource<List<DiscoveryUIModel>> {
        return when (val response = remote.upcomingMovies(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asDiscoveryUIModel(ListType.MovieUpcoming) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.message)
            }
        }
    }

    override suspend fun popularShows(page: Int): Resource<List<DiscoveryUIModel>> {
        return when (val response = remote.popularShows(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asDiscoveryUIModel(ListType.ShowPopular) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.message)
            }
        }
    }

    override suspend fun topRatedShows(page: Int): Resource<List<DiscoveryUIModel>> {
        return when (val response = remote.topRatedShows(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asDiscoveryUIModel(ListType.ShowTopRated) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.message)
            }
        }
    }

    override suspend fun airingTodayShows(page: Int): Resource<List<DiscoveryUIModel>> {
        return when (val response = remote.airingTodayShows(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asDiscoveryUIModel(ListType.ShowAiringToday) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.message)
            }
        }
    }
}

fun ResponseStandardMediaUpdated.ResponseMovieUpdated.asDiscoveryUIModel(listType: ListType) = DiscoveryUIModel(
    id = "$id",
    mediaType = MediaTypeUpdated.Movie,
    posterPath = posterPath ?: ""
)
fun ResponseStandardMediaUpdated.ResponseShowUpdated.asDiscoveryUIModel(listType: ListType) = DiscoveryUIModel(
    id = "$id",
    mediaType = MediaTypeUpdated.Movie,
    posterPath = posterPath ?: ""
)