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

package com.sunrisekcdeveloper.progress.impl

import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.cache.models.EntityWatchlistEpisode
import com.sunrisekcdeveloper.cache.models.EntityWatchlistSeason
import com.sunrisekcdeveloper.cache.models.EntityWatchlistShow
import com.sunrisekcdeveloper.progress.ProgressRepositoryContract
import com.sunrisekcdeveloper.progress.extras.toDomain
import com.sunrisekcdeveloper.progress.extras.toEntity
import com.sunrisekcdeveloper.show.TVShowRepositoryContract
import com.sunrisekcdeveloper.show.WatchlistTVShowRepositoryContract
import com.sunrisekcdeveloper.show.episode.EpisodeRepositoryContract
import com.sunrisekcdeveloper.show.episode.WatchlistEpisodeRepositoryContract
import com.sunrisekcdeveloper.show.season.SeasonRepositoryContract
import com.sunrisekcdeveloper.show.season.WatchlistSeasonRepositoryContract
import kotlin.collections.forEach as forEachIterable

class ProgressRepository(
    private val episodeRepo: EpisodeRepositoryContract,
    private val watchlistEpisodeRepo: WatchlistEpisodeRepositoryContract,
    private val seasonRepo: SeasonRepositoryContract,
    private val watchlistSeasonRepo: WatchlistSeasonRepositoryContract,
    private val showRepo: TVShowRepositoryContract,
    private val watchlistShowRepo: WatchlistTVShowRepositoryContract
) : ProgressRepositoryContract {

    override suspend fun setShowProgress(showId: String, season: Int, episode: Int) {
        val entityEpisode = episodeRepo.get(showId, season, episode)?.toEntity()
        val entitySeason = seasonRepo.get(showId, season)?.toEntity()

        entityEpisode?.let { episode ->
            entitySeason?.let { season ->
                watchlistEpisodeRepo.add(
                    EntityWatchlistEpisode.notWatchedFrom(
                        showId,
                        season.number,
                        episode.number
                    ).toDomain()
                )
                watchlistSeasonRepo.add(
                    EntityWatchlistSeason.partialFrom(
                        showId,
                        season.number,
                        episode.number
                    ).toDomain()
                )
                watchlistShowRepo.add(
                    EntityWatchlistShow.partialFrom(
                        showId,
                        episode.number,
                        episode.name,
                        season.number,
                        season.episodeTotal
                    ).toDomain()
                )
            }
        }
    }

    override suspend fun setNewShowAsUpToDate(showId: String) {
        seasonRepo.lastInShow(showId)?.toEntity()?.let { season ->
            episodeRepo.lastInSeason(showId, season.number)?.toEntity()?.let { episode ->
                watchlistEpisodeRepo.add(
                    EntityWatchlistEpisode.completedFrom(
                        showId,
                        season.number,
                        episode.number
                    ).toDomain()
                )
                watchlistSeasonRepo.add(
                    EntityWatchlistSeason.upToDateSeasonFrom(
                        showId,
                        season.number,
                        episode.number
                    ).toDomain()
                )
                watchlistShowRepo.add(
                    EntityWatchlistShow.upToDateEntryFrom(
                        showId,
                        episode.number,
                        episode.name,
                        season.number,
                        season.episodeTotal
                    ).toDomain()
                )
            }
        }
    }

    // this logic like most logic related to movies and tv show management can use some refactoring
    override suspend fun cacheEntireShow(showId: String) {
        showRepo.showWithSeasons(showId)?.let { tvShow ->
            tvShow.seasons.forEachIterable { season ->
                // get and save season information
                seasonRepo.get(showId, season.stats.number)?.let { seasonRepo.add(it) }

                // get and save episode information
                seasonRepo.seasonWithEpisodes(showId, season.stats.number)?.let { season ->
                    season.episodes.forEachIterable { episodeRepo.add(it) }
                }
            }
        }
    }

    @ExperimentalStdlibApi
    override suspend fun showSeasons(showId: String): Resource<Map<Int, List<Int>>> {
        // passing -1 as a default value is not ideal
        return Resource.Success(
            seasonRepo.allFromShow(showId).map { season ->
                val episodes = episodeRepo.allInSeason(showId, season.stats.number)
                season.stats.number to episodes.map { it?.identification?.number ?: -1 }
            }.toMap()
        )
    }
}