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

package com.sunrisekcdeveloper.showtracker.features.discovery.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sunrisekcdeveloper.showtracker.common.NetworkResult
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.common.util.asUIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.RemoteDataSourceDiscoveryContract
import com.sunrisekcdeveloper.showtracker.features.discovery.data.paging.PagingSourceDiscoveryPopularMovies
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ListType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.repository.RepositoryDiscoveryContract
import kotlinx.coroutines.flow.Flow

class RepositoryDiscovery(
    private val remote: RemoteDataSourceDiscoveryContract
) : RepositoryDiscoveryContract {

    override suspend fun popularMoviesStream(): Flow<PagingData<UIModelDiscovery>> = Pager(
            config = PagingConfig(
                pageSize = 20, // todo my api does not use page size attribute (so 20 is just random number)
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSourceDiscoveryPopularMovies(remote) }
        ).flow

    override suspend fun popularMovies(page: Int): Resource<List<UIModelDiscovery>> {
        return when (val response = remote.popularMovies(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asUIModelDiscovery(ListType.MoviePopular) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.message)
            }
        }
    }

    override suspend fun topRatedMovies(page: Int): Resource<List<UIModelDiscovery>> {
        return when (val response = remote.topRatedMovies(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asUIModelDiscovery(ListType.MovieTopRated) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.message)
            }
        }
    }

    override suspend fun upcomingMovies(page: Int): Resource<List<UIModelDiscovery>> {
        return when (val response = remote.upcomingMovies(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asUIModelDiscovery(ListType.MovieUpcoming) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.message)
            }
        }
    }

    override suspend fun popularShows(page: Int): Resource<List<UIModelDiscovery>> {
        return when (val response = remote.popularShows(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asUIModelDiscovery(ListType.ShowPopular) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.message)
            }
        }
    }

    override suspend fun topRatedShows(page: Int): Resource<List<UIModelDiscovery>> {
        return when (val response = remote.topRatedShows(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asUIModelDiscovery(ListType.ShowTopRated) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.message)
            }
        }
    }

    override suspend fun airingTodayShows(page: Int): Resource<List<UIModelDiscovery>> {
        return when (val response = remote.airingTodayShows(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asUIModelDiscovery(ListType.ShowAiringToday) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.message)
            }
        }
    }
}