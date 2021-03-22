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

package com.sunrisekcdeveloper.showtracker.features.discovery.domain.repository

import androidx.paging.PagingData
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import kotlinx.coroutines.flow.Flow

interface RepositoryDiscoveryContract {
    suspend fun popularMovies(page: Int): Resource<List<UIModelDiscovery>>
    suspend fun topRatedMovies(page: Int): Resource<List<UIModelDiscovery>>
    suspend fun upcomingMovies(page: Int): Resource<List<UIModelDiscovery>>
    suspend fun popularShows(page: Int): Resource<List<UIModelDiscovery>>
    suspend fun topRatedShows(page: Int): Resource<List<UIModelDiscovery>>
    suspend fun airingTodayShows(page: Int): Resource<List<UIModelDiscovery>>

    fun popularMoviesStream(): Flow<PagingData<UIModelDiscovery>>
    fun popularShowsStream(): Flow<PagingData<UIModelDiscovery>>
    fun topRatedMoviesStream(): Flow<PagingData<UIModelDiscovery>>
    fun topRatedShowsStream(): Flow<PagingData<UIModelDiscovery>>
    fun upcomingMoviesStream(): Flow<PagingData<UIModelDiscovery>>
    fun airingTodayShowsStream(): Flow<PagingData<UIModelDiscovery>>
}