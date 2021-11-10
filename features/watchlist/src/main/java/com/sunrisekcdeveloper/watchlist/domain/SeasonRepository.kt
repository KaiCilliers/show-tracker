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
import com.sunrisekcdeveloper.show.episode.Episode
import com.sunrisekcdeveloper.show.season.*
import com.sunrisekcdeveloper.watchlist.WatchlistRemoteDataSourceContract
import com.sunrisekcdeveloper.watchlist.extras.ResponseEpisode
import com.sunrisekcdeveloper.watchlist.extras.ResponseSeasonDetailWithEpisodes
import com.sunrisekcdeveloper.watchlist.extras.toDomain
import com.sunrisekcdeveloper.watchlist.extras.toEntity

class SeasonRepository(
    private val remote: WatchlistRemoteDataSourceContract,
    database: TrackerDatabase
) : SeasonRepositoryContract {

    private val seasonDao = database.seasonDao()

    override suspend fun get(showId: String, season: Int): Season? {
        if (!seasonDao.exist(showId, season)) {
            sync(showId, season)
        }
        return seasonDao.withId(showId, season).toDomain()
    }

    override suspend fun add(season: Season) = seasonDao.insert(season.toEntity())

    override suspend fun sync(showId: String, season: Int) {
        remote.season(showId, season).apply {
            if (this is NetworkResult.Success) {
                add(data.toDomain(showId))
            }
        }
    }

    override suspend fun lastInShow(showId: String): Season? = seasonDao.lastInShow(showId)?.toDomain()

    override suspend fun seasonWithEpisodes(showId: String, season: Int): SeasonWithEpisodes? {
        val details = remote.seasonDetails(showId, season)
        return if (details is NetworkResult.Success) {
            details.data.toDomain(showId)
        } else { null }
    }

    override suspend fun allFromShow(showId: String): List<Season> {
        return seasonDao.allFromShow(showId).map { it.toDomain() }
    }
}
