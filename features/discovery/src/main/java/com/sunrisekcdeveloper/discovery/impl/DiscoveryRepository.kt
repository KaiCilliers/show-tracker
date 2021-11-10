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

package com.sunrisekcdeveloper.discovery.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sunrisekcdeveloper.cache.ListType
import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.cache.UIModelDiscovery
import com.sunrisekcdeveloper.discovery.DiscoveryRemoteDataSourceContract
import com.sunrisekcdeveloper.discovery.extras.paging.*
import com.sunrisekcdeveloper.discovery.DiscoveryRepositoryContract
import kotlinx.coroutines.flow.Flow

// TODO [E07-002] [DiscoveryWithDomainModules] Discovery needs to use the domain module - not yet implemented
class DiscoveryRepository(
    private val remote: DiscoveryRemoteDataSourceContract,
    private val database: TrackerDatabase
) : DiscoveryRepositoryContract {

    @ExperimentalPagingApi
    override fun popularMoviesStream(): Flow<PagingData<UIModelDiscovery>> = Pager(
        config = PagingConfig(
            initialLoadSize = 40, // todo i dont think this does anything
            pageSize = 20, // todo my api does not use page size attribute (so 20 TMDB page size)
            // placeholders prevent feature focused grid layout to restructure layout when new data is loaded
            enablePlaceholders = true,
            prefetchDistance = 60
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
            enablePlaceholders = true,
            prefetchDistance = 60
        ),
        remoteMediator = RemoteMediatorDiscoveryPopularShows(remote, database),
        pagingSourceFactory = {
            database.discoveryDao().mediaList(ListType.ShowPopular)
        }
    ).flow

    @ExperimentalPagingApi
    override fun topRatedMoviesStream(): Flow<PagingData<UIModelDiscovery>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            enablePlaceholders = true,
            prefetchDistance = 60
        ),
        remoteMediator = RemoteMediatorDiscoveryTopRatedMovies(remote, database),
        pagingSourceFactory = {
            database.discoveryDao().mediaList(ListType.MovieTopRated)
        }
    ).flow

    @ExperimentalPagingApi
    override fun topRatedShowsStream(): Flow<PagingData<UIModelDiscovery>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            enablePlaceholders = true,
            prefetchDistance = 60
        ),
        remoteMediator = RemoteMediatorDiscoveryTopRatedShows(remote, database),
        pagingSourceFactory = {
            database.discoveryDao().mediaList(ListType.ShowTopRated)
        }
    ).flow

    @ExperimentalPagingApi
    override fun upcomingMoviesStream(): Flow<PagingData<UIModelDiscovery>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            enablePlaceholders = true,
            prefetchDistance = 60
        ),
        remoteMediator = RemoteMediatorDiscoveryUpcomingMovies(remote, database),
        pagingSourceFactory = {
            database.discoveryDao().mediaList(ListType.MovieUpcoming)
        }
    ).flow

    @ExperimentalPagingApi
    override fun airingTodayShowsStream(): Flow<PagingData<UIModelDiscovery>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            enablePlaceholders = true,
            prefetchDistance = 60
        ),
        remoteMediator = RemoteMediatorDiscoveryAiringShows(remote, database),
        pagingSourceFactory = {
            database.discoveryDao().mediaList(ListType.ShowAiringToday)
        }
    ).flow
}