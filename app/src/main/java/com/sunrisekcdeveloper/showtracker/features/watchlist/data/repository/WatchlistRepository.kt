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

import com.sunrisekcdeveloper.showtracker.commons.util.asDomainMovieSealed
import com.sunrisekcdeveloper.showtracker.commons.util.asDomainShowSealed
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.WatchlistClient
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository.WatchListRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.WatchlistDao
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.network.WatchlistDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.MediaModelSealed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WatchlistRepository(
    @WatchlistClient private val remote: WatchlistDataSourceContract,
    private val dao: WatchlistDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : WatchListRepositoryContract {

    override fun watchListMediaFlow(): Flow<Resource<List<MediaModelSealed>>> =
        dao.watchListMediaFlow()
            .map { list ->
                Resource.Success(
                    list.map {
                        // todo watchlistentity can only be movie or show
                        //  fix the model conversions
                        when (it.mediaType) {
                            MediaType.MOVIE -> it.asDomainMovieSealed()
                            MediaType.SHOW -> it.asDomainShowSealed()
                        }
                    }
                )
            }
}

