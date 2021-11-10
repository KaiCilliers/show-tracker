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

package com.sunrisekcdeveloper.watchlist.impl

import com.sunrisekcdeveloper.cache.FilterMovies
import com.sunrisekcdeveloper.cache.FilterShows
import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.cache.models.*
import com.sunrisekcdeveloper.movie.WatchlistMovieRepositoryContract
import com.sunrisekcdeveloper.movie.valueobjects.MovieFilter
import com.sunrisekcdeveloper.show.TVShowRepositoryContract
import com.sunrisekcdeveloper.show.WatchlistTVShowRepositoryContract
import com.sunrisekcdeveloper.show.episode.*
import com.sunrisekcdeveloper.show.season.SeasonRepositoryContract
import com.sunrisekcdeveloper.show.season.WatchlistSeasonRepositoryContract
import com.sunrisekcdeveloper.show.valueobjects.TVShowFilter
import com.sunrisekcdeveloper.watchlist.extras.model.ActionRepositoryMovie
import com.sunrisekcdeveloper.watchlist.extras.model.UIModelWatchlistMovie
import com.sunrisekcdeveloper.watchlist.extras.model.UpdateShowAction
import com.sunrisekcdeveloper.watchlist.WatchlistRepositoryContract
import com.sunrisekcdeveloper.watchlist.extras.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WatchlistRepository(
    private val watchlistShowRepo: WatchlistTVShowRepositoryContract,
    private val showRepository: TVShowRepositoryContract,
    private val watchlistMovieRepo: WatchlistMovieRepositoryContract,
    private val watchlistSeasonRepo: WatchlistSeasonRepositoryContract,
    private val seasonRepo: SeasonRepositoryContract,
    private val watchlistEpisodeRepo: WatchlistEpisodeRepositoryContract,
    private val episodeRepo: EpisodeRepositoryContract
) : WatchlistRepositoryContract {

    override suspend fun updateShowProgress(action: UpdateShowAction) {
        when (action) {
            is UpdateShowAction.IncrementEpisode -> {
                currentWatchlistShow(action.showId).apply {
                    incrementEpisode(this)
                }
            }
            is UpdateShowAction.CompleteSeason -> {
                finishSeason(action.showId)
            }
            is UpdateShowAction.UpToDateWithShow -> {
                setShowUpToDate(action.showId)
            }
        }
    }

    override fun watchlistMovies(filterOption: FilterMovies): Flow<Resource<List<UIModelWatchlistMovie>>> {
        val tempFilterConversion = when(filterOption) {
            FilterMovies.AddedToday -> MovieFilter.AddedToday
            FilterMovies.NoFilters -> MovieFilter.NoFilters
            FilterMovies.Unwatched -> MovieFilter.Unwatched
            FilterMovies.Watched -> MovieFilter.Watched
        }
        return watchlistMovieRepo.distinctFlow(tempFilterConversion).map {
            Resource.success(it.map { it.toWatchlistMovieUi() })
        }
    }

    override fun watchlistShows(filterOption: FilterShows): Flow<Resource<List<UIModelWatchlistShow>>> {
        val tempFilterConversion = when(filterOption) {
            FilterShows.AddedToday -> TVShowFilter.AddedToday
            FilterShows.NoFilters -> TVShowFilter.NoFilters
            FilterShows.NotStarted -> TVShowFilter.NotStarted
            FilterShows.Started -> TVShowFilter.Started
            FilterShows.WatchedToday -> TVShowFilter.WatchedToday
        }
         return watchlistShowRepo.distinctFlow(tempFilterConversion).map { list ->
            Resource.Success(list.map {
                val lastInSeason = episodeRepo.lastInSeason(
                    it.details.identification.id,
                    it.status.status.currentSeasonNumber
                )
                it.toWatchlistShowUi(lastInSeason?.identification?.number ?: -1)
            })
        }
    }

    private suspend fun currentShow(showId: String): EntityShow {
        var show = showRepository.get(showId)
        if (show == null) {
            showRepository.sync(showId)
            show = showRepository.get(showId)
        }
        return show!!.toEntity()
    }

    // TODO [E07-002] [BugWithCompletingSeason] It does not work and gives default values. The entire watchlist logic is busted anyways :(
    private suspend fun finishSeason(showId: String) {
        val show = currentShow(showId)
        val watchlistShow = currentWatchlistShow(showId)

        if (show.seasonTotal == watchlistShow.currentSeasonNumber) {
            setShowUpToDate(showId)
        } else {
            watchlistShow.apply {

                /// mark current episode as watched
                watchlistEpisodeRepo.get(id, currentEpisodeNumber, currentSeasonNumber)?.let {
                    watchlistEpisodeRepo.update(it.copy(
                        status = it.status.copy(watched = true),
                        meta = it.meta.copy(dateWatched = System.currentTimeMillis(), lastUpdated = System.currentTimeMillis())
                    ))
                }

                // update season as watched
                watchlistSeasonRepo.get(id, currentSeasonNumber)?.let {
                    watchlistSeasonRepo.update(it.copy(
                        status = it.status.copy(completed = true),
                        meta = it.meta.copy(dateCompleted = System.currentTimeMillis(), lastUpdated = System.currentTimeMillis())
                    ))
                }

                // insert new watchlist episode
                watchlistEpisodeRepo.add(
                    EntityWatchlistEpisode.notWatchedFrom(id, currentSeasonNumber, currentEpisodeNumber + 1).toDomain()
                )

                // insert new watchlist season
                watchlistSeasonRepo.add(EntityWatchlistSeason.partialFrom(
                    id, currentSeasonNumber + 1, 1).toDomain()
                )

                // get first episode in new season
                val firstEpisodeInSeason = episodeRepo.firstInSeason(id, currentSeasonNumber + 1)!!

                // update watchlist show
                val episode = episodeRepo.get(id, currentSeasonNumber + 1, firstEpisodeInSeason.identification.number)
                val season = seasonRepo.get(id, currentSeasonNumber + 1)

                // todo if null objects are returned then try fetch from network and if failed then you need to handle
                //  consider making room return nullable objects to form handle these null cases
                // todo some shows return seasons which have zero episodes... handle that case (example is Simpsons last two seasons)
                this?.toDomain().let {
                    watchlistShowRepo.update(it.copy(
                        status = it.status.copy(
                            currentEpisodeNumber = episode?.identification?.number ?: 1,
                            currentEpisodeName = episode?.identification?.name ?: "Episode 1 :)",
                            currentSeasonNumber = season?.stats?.number ?: 0,
                            currentSeasonEpisodeTotal = season?.stats?.episodeTotal ?: 0
                        ),
                        meta = it.meta.copy(lastUpdated = System.currentTimeMillis())
                    ))
                }
            }
        }
    }

    private suspend fun incrementEpisode(show: EntityWatchlistShow) {
        show.apply {
            // mark current episode as watched
            watchlistEpisodeRepo.get(id, currentEpisodeNumber, currentSeasonNumber)?.let {
                watchlistEpisodeRepo.update(it.copy(
                    status = it.status.copy(watched = true),
                    meta = it.meta.copy(
                        dateWatched = System.currentTimeMillis(),
                        lastUpdated = System.currentTimeMillis()
                    )
                ))
            }

            // insert new watchlist episode
            watchlistEpisodeRepo.add(EntityWatchlistEpisode.notWatchedFrom(
                id, currentSeasonNumber, currentEpisodeNumber + 1).toDomain()
            )

            // increment watchlist season current episode
            watchlistSeasonRepo.get(id, currentSeasonNumber)?.let {
                watchlistSeasonRepo.update(it.copy(
                    status = it.status.copy(currentEpisode = it.status.currentEpisode + 1),
                    meta = it.meta.copy(lastUpdated = System.currentTimeMillis())
                ))
            }

            // increment watchlist show current episode
            watchlistShowRepo.get(id)?.let {
                val nextEpisode = episodeRepo.get(id, currentSeasonNumber, currentEpisodeNumber + 1)
                watchlistShowRepo.update(it.copy(
                    status = it.status.copy(
                        currentEpisodeNumber = nextEpisode?.identification?.number ?: -1,
                        currentEpisodeName = nextEpisode?.identification?.name ?: "No name available"
                    ),
                    meta = it.meta.copy(lastUpdated = System.currentTimeMillis())
                ))
            }
        }
    }

    override suspend fun updateWatchlistShowAsUpToDate(showId: String) {
        watchlistShowRepo.get(showId)?.let {
            watchlistShowRepo.update(it.copy(
                status = it.status.copy(upToDate = true),
                meta = it.meta.copy(lastUpdated = System.currentTimeMillis())
            ))
        }
    }

    private suspend fun currentWatchlistShow(showId: String): EntityWatchlistShow {
        return watchlistShowRepo.get(showId)?.toEntity()!!
    }

    private suspend fun setShowUpToDate(showId: String) {
        val show = currentWatchlistShow(showId)
        markEpisodeAsWatched(show.id, show.currentSeasonNumber, show.currentEpisodeNumber)
        updateSeasonAsWatched(show.id, show.currentSeasonNumber)
        updateWatchlistShowAsUpToDate(showId)
    }

    private suspend fun markEpisodeAsWatched(showId: String, season: Int, episode: Int) {
        watchlistEpisodeRepo.get(showId, episode, season)?.let {
            watchlistEpisodeRepo.update(it.copy(
                status = it.status.copy(watched = true),
                meta = it.meta.copy(dateWatched = System.currentTimeMillis(), lastUpdated = System.currentTimeMillis())
            ))
        }
    }

    private suspend fun updateSeasonAsWatched(showId: String, season: Int) {
        watchlistSeasonRepo.get(showId, season)?.let {
            watchlistSeasonRepo.update(it.copy(
                status = it.status.copy(completed = true),
                meta = it.meta.copy(dateCompleted = System.currentTimeMillis(), lastUpdated = System.currentTimeMillis())
            ))
        }
    }

    override suspend fun submitMovieAction(action: ActionRepositoryMovie) {
        when (action) {
            is ActionRepositoryMovie.Add -> {
                val movie = watchlistMovieRepo.get(action.id)
                if (movie != null) {
                    watchlistMovieRepo.update(movie.copy(
                        status = movie.status.copy(deleted = false),
                        meta = movie.meta.copy(dateLastUpdated = System.currentTimeMillis())
                    ))
                } else {
                    watchlistMovieRepo.add(EntityWatchlistMovie.unwatchedFrom(action.id).toDomain())
                }
            }
            is ActionRepositoryMovie.Remove -> {
                val movie = watchlistMovieRepo.get(action.id)
                if (movie != null) {
                    watchlistMovieRepo.update(movie.copy(
                        status = movie.status.copy(deleted = true),
                        meta = movie.meta.copy(
                            dateDeleted = System.currentTimeMillis(),
                            dateLastUpdated = System.currentTimeMillis())
                    ))
                }
            }
            is ActionRepositoryMovie.Watch -> {
                val movie = watchlistMovieRepo.get(action.id)
                if (movie != null) {
                    watchlistMovieRepo.update(movie.copy(
                        status = movie.status.copy(deleted = false, watched = true),
                        meta = movie.meta.copy(
                            dateWatched = System.currentTimeMillis(),
                            dateLastUpdated = System.currentTimeMillis())
                    ))
                } else {
                    watchlistMovieRepo.add(EntityWatchlistMovie.watchedFrom(action.id).toDomain())
                }
            }
            is ActionRepositoryMovie.Unwatch -> {
                val movie = watchlistMovieRepo.get(action.id)
                if (movie != null) {
                    watchlistMovieRepo.update(movie.copy(
                        status = movie.status.copy(watched = false),
                        meta = movie.meta.copy(dateLastUpdated = System.currentTimeMillis())
                    ))
                } else {
                    watchlistMovieRepo.add(EntityWatchlistMovie.unwatchedFrom(action.id).toDomain())
                }
            }
        }
    }
}