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

package com.sunrisekcdeveloper.watchlist.domain

import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.network.NetworkResult
import com.sunrisekcdeveloper.show.TVShow
import com.sunrisekcdeveloper.show.TVShowRepositoryContract
import com.sunrisekcdeveloper.show.TVShowWithSeasons
import com.sunrisekcdeveloper.watchlist.WatchlistRemoteDataSourceContract
import com.sunrisekcdeveloper.watchlist.extras.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// TODO [E07-002] [CommonImplementationModule] Have a module which houses repository implementations of domain objects to prevent duplicate implementations in feature modules
class TVShowRepository(
    private val remote: WatchlistRemoteDataSourceContract,
    database: TrackerDatabase
) : TVShowRepositoryContract {

    private val dao = database.showDao()
    private val watchlistShowDao = database.watchlistShowDao()

    override suspend fun get(id: String): TVShow? {
        if (!dao.exist(id)) {
            sync(id)
        }
        return dao.withId(id)?.toDomain()
    }

    override suspend fun add(show: TVShow) = dao.insert(show.toEntity())

    override suspend fun sync(id: String) {
        val certification = remote.showCertification(id)
        remote.showDetail(id).apply {
            if (this is NetworkResult.Success && certification is NetworkResult.Success) {
                add(data.asEntityShow(
                    CertificationsContractWatchlist.Smart(CertificationsShowWatchlist(certification.data.results))
                        .fromUS()
                ).toDomain()
                )
            }
        }
    }

    override fun distinctFlow(id: String): Flow<TVShow?> = dao.distinctShowFlow(id).map { it?.toDomain() }

    override suspend fun unwatched(): List<TVShow> {
        return watchlistShowDao.unwatched().map { it.toTVShowDomain() }
    }

    override suspend fun showWithSeasons(id: String): TVShowWithSeasons? {
        TODO("Not yet implemented")
    }
}
