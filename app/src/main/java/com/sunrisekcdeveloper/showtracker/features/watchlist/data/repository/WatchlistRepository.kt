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

package com.sunrisekcdeveloper.showtracker.features.watchlist.data.repository

import com.sunrisekcdeveloper.showtracker.commons.models.local.*
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.WatchlistClient
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository.WatchListRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.WatchlistDao
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.network.WatchlistDataSourceContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class WatchlistRepository(
    @WatchlistClient private val remote: WatchlistDataSourceContract,
    private val dao: WatchlistDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : WatchListRepositoryContract {
    override suspend fun recentlyAddedMedia(): Resource<List<RecentlyAddedMediaEntity>> {
        val list = dao.recentlyAddedMedia()
        return if (!list.isNullOrEmpty()) {
            Resource.Success(list)
        } else {
            Resource.Error("No media in recently added table")
        }
    }

    override suspend fun inProgressMedia(): Resource<List<InProgressMediaEntity>> {
        val list = dao.inProgressMedia()
        return if (!list.isNullOrEmpty()) {
            Resource.Success(list)
        } else {
            Resource.Error("No media in recently added table")
        }
    }

    override suspend fun upcomingMedia(): Resource<List<UpcomingMediaEntity>> {
        val list = dao.upcomingMedia()
        return if (!list.isNullOrEmpty()) {
            Resource.Success(list)
        } else {
            Resource.Error("No media in recently added table")
        }
    }

    override suspend fun completedMedia(): Resource<List<CompletedMediaEntity>> {
        val list = dao.completedMedia()
        return if (!list.isNullOrEmpty()) {
            Resource.Success(list)
        } else {
            Resource.Error("No media in recently added table")
        }
    }

    override suspend fun anticipatedMedia(): Resource<List<AnticipatedMediaEntity>> {
        val list = dao.anticipatedMedia()
        return if (!list.isNullOrEmpty()) {
            Resource.Success(list)
        } else {
            Resource.Error("No media in recently added table")
        }
    }
}

