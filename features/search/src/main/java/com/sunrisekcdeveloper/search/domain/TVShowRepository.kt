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

package com.sunrisekcdeveloper.search.domain

import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.search.extras.toTVShowDomain
import com.sunrisekcdeveloper.show.TVShow
import com.sunrisekcdeveloper.show.TVShowRepositoryContract
import com.sunrisekcdeveloper.show.TVShowWithSeasons
import kotlinx.coroutines.flow.Flow

class TVShowRepository(
    database: TrackerDatabase
) : TVShowRepositoryContract {

    private val dao = database.watchlistShowDao()

    override suspend fun get(id: String): TVShow? {
        TODO("Not yet implemented")
    }

    override suspend fun add(show: TVShow) {
        TODO("Not yet implemented")
    }

    override suspend fun sync(id: String) {
        TODO("Not yet implemented")
    }

    override fun distinctFlow(id: String): Flow<TVShow?> {
        TODO("Not yet implemented")
    }

    override suspend fun unwatched(): List<TVShow> =  dao.unwatched().map { it.toTVShowDomain() }

    override suspend fun showWithSeasons(id: String): TVShowWithSeasons? {
        TODO("Not yet implemented")
    }
}
