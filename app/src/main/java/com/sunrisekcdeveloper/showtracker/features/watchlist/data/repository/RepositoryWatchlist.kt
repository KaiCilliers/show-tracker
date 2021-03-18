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

package com.sunrisekcdeveloper.showtracker.features.watchlist.data.repository

import androidx.room.Embedded
import androidx.room.Relation
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.DaoWatchlist
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.SortMovies
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.SortShows
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.*
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository.RepositoryWatchlistContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.UIModelWatchlisMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.UIModelWatchlistShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class RepositoryWatchlist(
    private val local: DaoWatchlist
) : RepositoryWatchlistContract {

    override suspend fun currentShow(showId: String): EntityShow {
        return local.show(showId)
    }

    override suspend fun markEpisodeAsWatched(showId: String, season: Int, episode: Int) {
        val watchlistEpisode = local.watchlistEpisode(showId, episode, season)
        Timber.d("episode to mark: $watchlistEpisode")
        watchlistEpisode?.let {
            Timber.e("inside")
            local.updateWatchlistEpisode(
                watchlistEpisode.copy(
                    watched = true,
                    dateWatched = System.currentTimeMillis(),
                    lastUpdated = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun insertNewWatchlistEpisode(showId: String, season: Int, episode: Int) {
        local.insertWatchlistEpisode(
            EntityWatchlistEpisode.notWatchedFrom(
                showId, season, episode
            )
        )
    }

    override suspend fun incrementSeasonCurrentEpisode(showId: String, currentSeason: Int) {
        val season = local.watchlistSeason(showId, currentSeason)
        val currentEpisode = season.currentEpisode
        local.updateWatchlistSeason(
            season.copy(
                currentEpisode = (currentEpisode + 1),
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    override suspend fun incrementWatchlistShowCurrentEpisode(showId: String) {
        val watchlistShow = local.watchlistShow(showId)
        val newEpisode = local.episode(
            showId,
            watchlistShow.currentSeasonNumber,
            watchlistShow.currentEpisodeNumber + 1
        )
        Timber.e("OK - here is some data")
        Timber.e("show: $watchlistShow")
        Timber.e("next episode: $newEpisode")
        local.updateWatchlistShow(
            watchlistShow.copy(
                currentEpisodeNumber = newEpisode?.number?: -1,
                currentEpisodeName = newEpisode?.name?: "oops",
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    override suspend fun updateSeasonAsWatched(showId: String, season: Int) {
        val watchlistSeason = local.watchlistSeason(showId, season)
        local.updateWatchlistSeason(
            watchlistSeason.copy(
                completed = true,
                dateCompleted = System.currentTimeMillis(),
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    override suspend fun insertNewWatchlistSeason(showId: String, season: Int, episode: Int) {
        local.insertWatchlistSeason(
            EntityWatchlistSeason.partialFrom(
                showId, season, episode
            )
        )
    }

    override suspend fun updateWatchlistShowEpisodeAndSeason(showId: String, newSeason: Int, newEpisode: Int) {
        val watchlistShow = local.watchlistShow(showId)
        val episode = local.episode(showId, newSeason, newEpisode)
        val season = local.season(showId, newSeason)
        // todo if null objects are returned then try fetch from netowrk and if failed then you need to handle
        //  consider making room return nullable objects to form handle these null cases
        // todo some shows return seasons which have zero episodes... handle that case (example is Simpsons last two seasons)
        Timber.d("show $watchlistShow")
        Timber.d("episode: $episode")
        Timber.d("season: $season")
        local.updateWatchlistShow(
            watchlistShow.copy(
                currentEpisodeNumber = episode?.number?: 1,
                currentEpisodeName = episode?.name?: "Episode 1 :)",
                currentSeasonNumber = season.number,
                currentSeasonEpisodeTotal = season.episodeTotal,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    override suspend fun updateWatchlistShowAsUpToDate(showId: String) {
        val show = local.watchlistShow(showId)
        local.updateWatchlistShow(
            show.copy(
                upToDate = true,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    override suspend fun currentWatchlistShow(showId: String): EntityWatchlistShow {
        return local.watchlistShow(showId)
    }

    override fun watchlistMovies(sortMovies: SortMovies): Flow<Resource<List<UIModelWatchlisMovie>>> {
        return local.distinctWatchlistMoviesDetailsFlow(sortMovies).map {
            if (it.isNotEmpty()) {
                Resource.Success(it.asListUIModelWatchlistMovie())
            } else {
                Resource.Error("There is no results in the database...")
            }
        }
    }

    override fun watchlistShows(sortBy: SortShows): Flow<Resource<List<UIModelWatchlistShow>>> {
        return local.distinctWatchlistShowsDetailsFlow(sortBy).map {
            if (it.isNotEmpty()) {
                Resource.Success(it.asListUIModelWatchlistShow())
            } else {
                Resource.Error("There is no results in the database...")
            }
        }
    }
}

fun WatchlistShowDetails.asUIModelWatchlistShow() = UIModelWatchlistShow(
    id = details.id,
    title = details.title,
    posterPath = details.posterPath,
    currentEpisodeNumber = watchlist.currentEpisodeNumber,
    currentEpisodeName = watchlist.currentEpisodeName,
    currentSeasonNumber = watchlist.currentSeasonNumber,
    episodesInSeason = watchlist.currentSeasonEpisodeTotal,
    started = watchlist.started,
    upToDate = watchlist.upToDate,
    dateAdded = watchlist.dateAdded
)

fun List<WatchlistShowDetails>.asListUIModelWatchlistShow(): List<UIModelWatchlistShow> {
    return this.map { it.asUIModelWatchlistShow() }
}

fun WatchlistMovieDetails.asUIModelWatchlistMovie() = UIModelWatchlisMovie(
    id = details.id,
    title = details.title,
    overview = details.overview,
    posterPath = details.posterPath,
    watched = watchlist.watched,
    dateAdded = watchlist.dateAdded,
    dateWatched = watchlist.dateWatched,
    lastUpdated = watchlist.dateLastUpdated
)

fun List<WatchlistMovieDetails>.asListUIModelWatchlistMovie(): List<UIModelWatchlisMovie> {
    return this.map { it.asUIModelWatchlistMovie() }
}

data class WatchlistShowDetails(
    @Embedded val watchlist: EntityWatchlistShow,
    @Relation(
        parentColumn = "watch_show_id",
        entityColumn = "show_id"
    )
    val details: EntityShow
)

data class WatchlistMovieDetails(
    @Embedded val watchlist: EntityWatchlistMovie,
    @Relation(
        parentColumn = "watch_movie_id",
        entityColumn = "movie_id"
    )
    val details: EntityMovie
)