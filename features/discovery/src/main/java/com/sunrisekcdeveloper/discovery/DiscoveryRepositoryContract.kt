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

package com.sunrisekcdeveloper.discovery

import androidx.paging.PagingData
import com.sunrisekcdeveloper.cache.UIModelDiscovery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface DiscoveryRepositoryContract {
    fun popularMoviesStream(): Flow<PagingData<UIModelDiscovery>>
    fun popularShowsStream(): Flow<PagingData<UIModelDiscovery>>
    fun topRatedMoviesStream(): Flow<PagingData<UIModelDiscovery>>
    fun topRatedShowsStream(): Flow<PagingData<UIModelDiscovery>>
    fun upcomingMoviesStream(): Flow<PagingData<UIModelDiscovery>>
    fun airingTodayShowsStream(): Flow<PagingData<UIModelDiscovery>>

    class Fake() : DiscoveryRepositoryContract {
        override fun popularMoviesStream(): Flow<PagingData<UIModelDiscovery>> {
            return flowOf(PagingData.from(UIModelDiscovery.create(20)))
        }

        override fun popularShowsStream(): Flow<PagingData<UIModelDiscovery>> {
            return flowOf(PagingData.from(UIModelDiscovery.create(20)))
        }

        override fun topRatedMoviesStream(): Flow<PagingData<UIModelDiscovery>> {
            return flowOf(PagingData.from(UIModelDiscovery.create(20)))
        }

        override fun topRatedShowsStream(): Flow<PagingData<UIModelDiscovery>> {
            return flowOf(PagingData.from(UIModelDiscovery.create(20)))
        }

        override fun upcomingMoviesStream(): Flow<PagingData<UIModelDiscovery>> {
            return flowOf(PagingData.from(UIModelDiscovery.create(20)))
        }

        override fun airingTodayShowsStream(): Flow<PagingData<UIModelDiscovery>> {
            return flowOf(PagingData.from(UIModelDiscovery.create(20)))
        }
    }
}