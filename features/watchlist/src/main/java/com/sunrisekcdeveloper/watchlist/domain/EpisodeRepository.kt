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
import com.sunrisekcdeveloper.show.episode.EpisodeRepositoryContract
import com.sunrisekcdeveloper.watchlist.WatchlistRemoteDataSourceContract
import com.sunrisekcdeveloper.watchlist.extras.toDomain
import com.sunrisekcdeveloper.watchlist.extras.toEntity

class EpisodeRepository(
    private val remote: WatchlistRemoteDataSourceContract,
    database: TrackerDatabase
) : EpisodeRepositoryContract {

    private val episodeDao = database.episodeDao()

    override suspend fun get(showId: String, season: Int, episode: Int): Episode? {
        if (!episodeDao.exist(showId, season, episode)) {
            sync(showId, season, episode)
        }
        return episodeDao.withId(showId, season, episode)?.toDomain()
    }

    override suspend fun add(episode: Episode) {
        episodeDao.insert(episode.toEntity())
    }

    override suspend fun sync(showId: String, season: Int, episode: Int) {
        remote.episodeDetails(showId, season, episode).apply {
            if (this is NetworkResult.Success) {
                add(data.toDomain(showId))
            }
        }
    }

    override suspend fun firstInSeason(showId: String, season: Int): Episode? = episodeDao.firstInSeason(showId, season)?.toDomain()

    override suspend fun allInSeason(showId: String, season: Int): List<Episode?> = episodeDao.allInSeason(showId, season).map {
        it?.toDomain()
    }

    override suspend fun lastInSeason(showId: String, season: Int): Episode? = episodeDao.lastInSeason(showId, season)?.toDomain()
}