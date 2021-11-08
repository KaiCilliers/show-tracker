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
import com.sunrisekcdeveloper.show.episode.WatchlistEpisode
import com.sunrisekcdeveloper.show.episode.WatchlistEpisodeRepositoryContract
import com.sunrisekcdeveloper.watchlist.extras.toDomain
import com.sunrisekcdeveloper.watchlist.extras.toEntity

class WatchlistEpisodeRepository(
    database: TrackerDatabase
) : WatchlistEpisodeRepositoryContract{

    private val watchlistEpisodeDao = database.watchlistEpisodeDao()

    override suspend fun get(showId: String, episode: Int, season: Int): WatchlistEpisode? =
        watchlistEpisodeDao.withId(showId, episode, season)?.toDomain()

    override suspend fun update(episode: WatchlistEpisode) = watchlistEpisodeDao.update(episode.toEntity())

    override suspend fun add(episode: WatchlistEpisode) = watchlistEpisodeDao.insert(episode.toEntity())
}