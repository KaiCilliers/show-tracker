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

package com.sunrisekcdeveloper.showtracker.features.detail.data.repository

import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DetailClient
import com.sunrisekcdeveloper.showtracker.features.detail.data.local.DetailDao
import com.sunrisekcdeveloper.showtracker.features.detail.data.network.DetailDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.repository.DetailRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.WatchListType
import com.sunrisekcdeveloper.showtracker.models.local.core.MediaEntity

class DetailRepository(
    @DetailClient private val remote: DetailDataSourceContract,
    private val dao: DetailDao
) : DetailRepositoryContract {

    override suspend fun updateWatchListType(id: Long, type: WatchListType) {
        dao.updateWatchListType(id, type)
    }

    override suspend fun movie(id: Long): MediaEntity.MovieEntityTMDB {
        return dao.fromMovieDump(id)
    }

    override suspend fun show(id: Long): MediaEntity.ShowEntityTMDB {
        return dao.fromShowDump(id)
    }
}

