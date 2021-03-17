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

import com.sunrisekcdeveloper.showtracker.common.NetworkResult
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.SourceProgress
import com.sunrisekcdeveloper.showtracker.features.progress.data.local.DaoProgress
import com.sunrisekcdeveloper.showtracker.features.progress.data.network.RemoteDataSourceProgressContract
import com.sunrisekcdeveloper.showtracker.features.progress.data.network.ResponseEpisode
import com.sunrisekcdeveloper.showtracker.features.progress.data.network.ResponseSeason
import com.sunrisekcdeveloper.showtracker.features.progress.domain.repository.RepositoryProgressContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.*
import kotlinx.coroutines.*
import timber.log.Timber

// todo determine if the annotation is needed at remote source
class RepositoryProgress(
    @SourceProgress private val remote: RemoteDataSourceProgressContract,
    private val local: DaoProgress,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RepositoryProgressContract {

    override suspend fun setShowProgress(showId: String, season: Int, episode: Int) {
        val episode = local.episode(showId, season, episode)
        val season = local.season(showId, season)

        Timber.e("Episode: $episode")
        Timber.e("Season: $season")

        local.setShowProgress(
            episode = EntityWatchlistEpisode.notWatchedFrom(showId, season.number, episode.number),
            season = EntityWatchlistSeason.partialFrom(showId, season.number, episode.number),
            show = EntityWatchlistShow.partialFrom(
                showId,
                episode.number,
                episode.name,
                season.number,
                season.name
            )
        )
    }

    override suspend fun setNewShowAsUpToDate(showId: String) {
        val season = local.lastSeasonOfShow(showId)
        val episode = local.lastEpisodeOfShow(showId, season.number)

        local.setShowProgress(
            episode = EntityWatchlistEpisode.completedFrom(showId, season.number, episode.number),
            season = EntityWatchlistSeason.upToDateSeasonFrom(showId, season.number, episode.number),
            show = EntityWatchlistShow.upToDateEntryFrom(
                showId,
                episode.number,
                episode.name,
                season.number,
                season.name
            )
        )
    }



    // todo this looks shit refactor function
    override suspend fun cacheEntireShow(showId: String) {
        withContext(dispatcher) {
            launch {
                val response = remote.showWithSeasons(showId)
                when (response) {
                    is NetworkResult.Success -> {
                        Timber.e("Seasons: ${response.data.seasonCount}")
                        Timber.e("Seasons: ${response.data.seasons.size}")

                        response.data.seasons.forEach {
                            Timber.e("ResponseSeason: $it")
                            Timber.e("Included Season: $it")
                            Timber.e("$it")
                            val second = remote.seasonDetails(
                                showId, it.number
                            )

                            local.addSeason(it.asEntitySeason(showId))

                            when (second) {
                                is NetworkResult.Success -> {
                                    Timber.e("Season overview: ${second.data.overview}")
                                    Timber.e("Season poster path: ${second.data.posterPath}")
                                    Timber.e("Episodes: ${second.data.episodes.size}")

                                    second.data.episodes.forEach { episode ->
                                        local.addEpisode(episode.asEntityEpisode(showId))
                                    }

                                }
                                is NetworkResult.Error -> {
                                    Timber.e("Error fetching season details with show_id: $showId and season number: ${it.number}. [${second.message}]")
                                }
                            }
                        }
                    }
                    is NetworkResult.Error -> {
                        Timber.e("Error fetching details for show with ID: $showId. [${response.message}]")
                    }
                }
            }
        }
    }

    fun ResponseEpisode.asEntityEpisode(showId: String) = EntityEpisode(
        showId = showId,
        seasonNumber = seasonNumber,
        number = number,
        name = name,
        overview = overview,
        airDate = -1L, // todo date string to date Long
        stillPath = stillPath ?: "",
        lastUpdated = System.currentTimeMillis()
    )

    fun ResponseSeason.asEntitySeason(showId: String) = EntitySeason(
        showId = showId,
        id = id,
        number = number,
        name = name,
        overview = overview,
        posterPath = posterPath,
        airDate = -1L, // todo conversion function to take string date and return Long version
        episodeTotal = episodeCount,
        lastUpdated = System.currentTimeMillis()
    )

    @ExperimentalStdlibApi
    override suspend fun showSeasons(showId: String): Resource<Map<Int, Int>> {
        val seasons = local.seasonsOfShow(showId)
        // todo handle if list is empty
        // todo handle check to ensure this is all the seasons

        return Resource.Success(
            seasons.map {
                it.number to it.episodeTotal
            }.toMap()
        )
    }
}