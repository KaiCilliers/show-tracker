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

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sunrisekcdeveloper.showtracker.common.util.NetworkResult
import com.sunrisekcdeveloper.showtracker.common.util.Resource
import com.sunrisekcdeveloper.showtracker.common.TrackerDatabase
import com.sunrisekcdeveloper.showtracker.common.util.asUIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.RemoteDataSourceDiscoveryContract
import com.sunrisekcdeveloper.showtracker.features.discovery.data.paging.*
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ListType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.repository.RepositoryDiscoveryContract
import kotlinx.coroutines.flow.Flow

// todo The base discovery lists need to look different each day (so why not rank them based on popularity? - just to give it a less static feel)
class RepositoryDiscovery(
    private val remote: RemoteDataSourceDiscoveryContract,
    private val database: TrackerDatabase
) : RepositoryDiscoveryContract {

    @ExperimentalPagingApi
    override fun popularMoviesStream(): Flow<PagingData<UIModelDiscovery>> = Pager(
        config = PagingConfig(
            initialLoadSize = 40,
            pageSize = 20, // todo my api does not use page size attribute (so 20 is just random number)
            enablePlaceholders = false
        ),
        remoteMediator = RemoteMediatorDiscoveryPopularMovies(
            remote, database
        ),
        pagingSourceFactory = {
            database.discoveryDao().mediaList(ListType.MoviePopular)
        }
    ).flow

    @ExperimentalPagingApi
    override fun popularShowsStream(): Flow<PagingData<UIModelDiscovery>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            enablePlaceholders = false
        ),
        remoteMediator = RemoteMediatorDiscoveryPopularShows(remote,database),
        pagingSourceFactory = {
            database.discoveryDao().mediaList(ListType.ShowPopular)
        }
    ).flow

    @ExperimentalPagingApi
    override fun topRatedMoviesStream(): Flow<PagingData<UIModelDiscovery>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            enablePlaceholders = false
        ),
        remoteMediator = RemoteMediatorDiscoveryTopRatedMovies(remote,database),
        pagingSourceFactory = {
            database.discoveryDao().mediaList(ListType.MovieTopRated)
        }
    ).flow

    @ExperimentalPagingApi
    override fun topRatedShowsStream(): Flow<PagingData<UIModelDiscovery>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            enablePlaceholders = false
        ),
        remoteMediator = RemoteMediatorDiscoveryTopRatedShows(remote,database),
        pagingSourceFactory = {
            database.discoveryDao().mediaList(ListType.ShowTopRated)
        }
    ).flow

    @ExperimentalPagingApi
    override fun upcomingMoviesStream(): Flow<PagingData<UIModelDiscovery>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            enablePlaceholders = false
        ),
        remoteMediator = RemoteMediatorDiscoveryUpcomingMovies(remote,database),
        pagingSourceFactory = {
            database.discoveryDao().mediaList(ListType.MovieUpcoming)
        }
    ).flow

    @ExperimentalPagingApi
    override fun airingTodayShowsStream(): Flow<PagingData<UIModelDiscovery>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            enablePlaceholders = false
        ),
        remoteMediator = RemoteMediatorDiscoveryAiringShows(remote,database),
        pagingSourceFactory = {
            database.discoveryDao().mediaList(ListType.ShowAiringToday)
        }
    ).flow

    override suspend fun popularMovies(page: Int): Resource<List<UIModelDiscovery>> {
        return when (val response = remote.popularMovies(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asUIModelDiscovery(ListType.MoviePopular) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.exception)
            }
        }
    }

    override suspend fun topRatedMovies(page: Int): Resource<List<UIModelDiscovery>> {
        return when (val response = remote.topRatedMovies(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asUIModelDiscovery(ListType.MovieTopRated) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.exception)
            }
        }
    }

    override suspend fun upcomingMovies(page: Int): Resource<List<UIModelDiscovery>> {
        return when (val response = remote.upcomingMovies(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asUIModelDiscovery(ListType.MovieUpcoming) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.exception)
            }
        }
    }

    override suspend fun popularShows(page: Int): Resource<List<UIModelDiscovery>> {
        return when (val response = remote.popularShows(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asUIModelDiscovery(ListType.ShowPopular) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.exception)
            }
        }
    }

    override suspend fun topRatedShows(page: Int): Resource<List<UIModelDiscovery>> {
        return when (val response = remote.topRatedShows(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asUIModelDiscovery(ListType.ShowTopRated) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.exception)
            }
        }
    }

    override suspend fun airingTodayShows(page: Int): Resource<List<UIModelDiscovery>> {
        return when (val response = remote.airingTodayShows(page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asUIModelDiscovery(ListType.ShowAiringToday) })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.exception)
            }
        }
    }
}