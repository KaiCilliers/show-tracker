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

package com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository

import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.SortMovies
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.SortShows
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityShow
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistShow
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.UIModelWatchlisMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.UIModelWatchlistShow
import kotlinx.coroutines.flow.Flow

interface RepositoryWatchlistContract {
    fun watchlistMovies(sortBy: SortMovies): Flow<Resource<List<UIModelWatchlisMovie>>>
    fun watchlistShows(sortBy: SortShows): Flow<Resource<List<UIModelWatchlistShow>>>

    // todo this is not ideal to return value like this
    suspend fun currentShow(showId: String): EntityShow

    // todo this is not ideal to return value like this
    suspend fun currentWatchlistShow(showId: String): EntityWatchlistShow

    suspend fun markEpisodeAsWatched(showId: String, season: Int, episode: Int)

    suspend fun insertNewWatchlistEpisode(showId: String, season: Int, episode: Int)

    suspend fun incrementSeasonCurrentEpisode(showId: String, currentSeason: Int)

    suspend fun incrementWatchlistShowCurrentEpisode(showId: String)

    suspend fun updateSeasonAsWatched(showId: String, season: Int)

    suspend fun insertNewWatchlistSeason(showId: String, season: Int, episode: Int)

    suspend fun updateWatchlistShowEpisodeAndSeason(showId: String, newSeason: Int, newEpisode: Int)

    suspend fun updateWatchlistShowAsUpToDate(showId: String)
}