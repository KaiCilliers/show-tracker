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

package com.sunrisekcdeveloper.showtracker.features.progress.data.repository

import androidx.room.withTransaction
import com.sunrisekcdeveloper.showtracker.common.util.NetworkResult
import com.sunrisekcdeveloper.showtracker.common.util.Resource
import com.sunrisekcdeveloper.showtracker.common.TrackerDatabase
import com.sunrisekcdeveloper.showtracker.common.util.asEntityEpisode
import com.sunrisekcdeveloper.showtracker.common.util.asEntitySeason
import com.sunrisekcdeveloper.showtracker.features.progress.data.network.RemoteDataSourceProgressContract
import com.sunrisekcdeveloper.showtracker.features.progress.domain.repository.RepositoryProgressContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.*
import kotlinx.coroutines.*
import timber.log.Timber

class RepositoryProgress(
    private val remote: RemoteDataSourceProgressContract,
    private val database: TrackerDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RepositoryProgressContract {

    override suspend fun setShowProgress(showId: String, season: Int, episode: Int) {
        val entityEpisode = database.episodeDao().withId(showId, season, episode)
        val entitySeason = database.seasonDao().withId(showId, season)

        database.withTransaction {
            database.watchlistEpisodeDao().insert(
                EntityWatchlistEpisode.notWatchedFrom(
                    showId,
                    entitySeason.number,
                    entityEpisode.number
                )
            )
            database.watchlistSeasonDao().insert(
                EntityWatchlistSeason.partialFrom(
                    showId,
                    entitySeason.number,
                    entityEpisode.number
                )
            )
            database.watchlistShowDao().insert(
                EntityWatchlistShow.partialFrom(
                    showId,
                    entityEpisode.number,
                    entityEpisode.name,
                    entitySeason.number,
                    entitySeason.episodeTotal
                )
            )
        }
    }

    override suspend fun setNewShowAsUpToDate(showId: String) {
        val season = database.seasonDao().lastInShow(showId)
        val episode = database.episodeDao().lastInSeason(showId, season.number)

        database.withTransaction {
            database.watchlistEpisodeDao()
                .insert(EntityWatchlistEpisode.completedFrom(showId, season.number, episode.number))
            database.watchlistSeasonDao().insert(
                EntityWatchlistSeason.upToDateSeasonFrom(
                    showId,
                    season.number,
                    episode.number
                )
            )
            database.watchlistShowDao().insert(
                EntityWatchlistShow.upToDateEntryFrom(
                    showId,
                    episode.number,
                    episode.name,
                    season.number,
                    season.episodeTotal
                )
            )
        }
    }

    // todo refactor logic
    override suspend fun cacheEntireShow(showId: String) {
        withContext(dispatcher) {
            launch {
                val response = remote.showWithSeasons(showId)
                when (response) {
                    is NetworkResult.Success -> {
                        response.data.seasons.forEach {
                            val second = remote.seasonDetails(
                                showId, it.number
                            )

                            database.seasonDao().insert(it.asEntitySeason(showId))

                            when (second) {
                                is NetworkResult.Success -> {
                                    second.data.episodes.forEach { episode ->
                                        database.episodeDao()
                                            .insert(episode.asEntityEpisode(showId))
                                    }
                                }
                                is NetworkResult.Error -> {
                                    // todo dont swallow exceptions
                                    Timber.e("Error fetching season details with show_id: $showId and season number: ${it.number}. [${second.exception}]")
                                }
                            }
                        }
                    }
                    is NetworkResult.Error -> {
                        Timber.e("Error fetching details for show with ID: $showId. [${response.exception}]")
                    }
                }
            }
        }
    }

    @ExperimentalStdlibApi
    override suspend fun showSeasons(showId: String): Resource<Map<Int, List<Int>>> {
        val seasons = database.seasonDao().allFromShow(showId)
        // todo handle if list is empty
        // todo handle check to ensure this is all the seasons

        return Resource.Success(
            seasons.map { season ->
                val episodes = database.episodeDao().allInSeason(showId, season.number)
                season.number to episodes.map { it.number }
            }.toMap()
        )
    }
}