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

import com.sunrisekcdeveloper.showtracker.common.util.Resource
import com.sunrisekcdeveloper.showtracker.common.TrackerDatabase
import com.sunrisekcdeveloper.showtracker.common.dao.relations.WatchlistMovieWithDetails
import com.sunrisekcdeveloper.showtracker.common.dao.relations.WatchlistShowWithDetails
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.FilterMovies
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.FilterShows
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.*
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository.RepositoryWatchlistContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.UIModelWatchlisMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.UIModelWatchlistShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class RepositoryWatchlist(
    private val database: TrackerDatabase
) : RepositoryWatchlistContract {

    override suspend fun currentShow(showId: String): EntityShow {
        return database.showDao().withId(showId)
    }

    override suspend fun markEpisodeAsWatched(showId: String, season: Int, episode: Int) {
        val watchlistEpisode = database.watchlistEpisodeDao().withId(showId, episode, season)
        Timber.d("episode to mark: $watchlistEpisode")
        @Suppress("UNNECESSARY_SAFE_CALL")
        watchlistEpisode?.let {
            Timber.e("inside")
            database.watchlistEpisodeDao().update(
                watchlistEpisode.copy(
                    watched = true,
                    dateWatched = System.currentTimeMillis(),
                    lastUpdated = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun insertNewWatchlistEpisode(showId: String, season: Int, episode: Int) {
        database.watchlistEpisodeDao().insert(
            EntityWatchlistEpisode.notWatchedFrom(
                showId, season, episode
            )
        )
    }

    override suspend fun incrementSeasonCurrentEpisode(showId: String, currentSeason: Int) {
        val season = database.watchlistSeasonDao().watchlistSeason(showId, currentSeason)
        val currentEpisode = season.currentEpisode
        database.watchlistSeasonDao().update(
            season.copy(
                currentEpisode = (currentEpisode + 1),
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    override suspend fun incrementWatchlistShowCurrentEpisode(showId: String) {
        val watchlistShow = database.watchlistShowDao().watchlistShow(showId)
        val newEpisode = database.episodeDao().withId(
            showId,
            watchlistShow.currentSeasonNumber,
            watchlistShow.currentEpisodeNumber + 1
        )
        Timber.e("OK - here is some data")
        Timber.e("show: $watchlistShow")
        Timber.e("next episode: $newEpisode")
        database.watchlistShowDao().update(
            watchlistShow.copy(
                currentEpisodeNumber = newEpisode?.number?: -1,
                currentEpisodeName = newEpisode?.name?: "oops",
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    override suspend fun updateSeasonAsWatched(showId: String, season: Int) {
        val watchlistSeason = database.watchlistSeasonDao().watchlistSeason(showId, season)
        database.watchlistSeasonDao().update(
            watchlistSeason.copy(
                completed = true,
                dateCompleted = System.currentTimeMillis(),
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    override suspend fun insertNewWatchlistSeason(showId: String, season: Int, episode: Int) {
        database.watchlistSeasonDao().insert(
            EntityWatchlistSeason.partialFrom(
                showId, season, episode
            )
        )
    }

    override suspend fun updateWatchlistShowEpisodeAndSeason(showId: String, newSeason: Int, newEpisode: Int) {
        val watchlistShow = database.watchlistShowDao().watchlistShow(showId)
        val episode = database.episodeDao().withId(showId, newSeason, newEpisode)
        val season = database.seasonDao().withId(showId, newSeason)
        // todo if null objects are returned then try fetch from netowrk and if failed then you need to handle
        //  consider making room return nullable objects to form handle these null cases
        // todo some shows return seasons which have zero episodes... handle that case (example is Simpsons last two seasons)
        Timber.d("show $watchlistShow")
        Timber.d("episode: $episode")
        Timber.d("season: $season")
        database.watchlistShowDao().update(
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
        val show = database.watchlistShowDao().watchlistShow(showId)
        database.watchlistShowDao().update(
            show.copy(
                upToDate = true,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    override suspend fun currentWatchlistShow(showId: String): EntityWatchlistShow {
        return database.watchlistShowDao().watchlistShow(showId)
    }

    override fun watchlistMovies(filterOption: FilterMovies): Flow<Resource<List<UIModelWatchlisMovie>>> {
        return database.watchlistMovieDao().distinctWithDetailsFlow(filterOption).map {
            if (it.isNotEmpty()) {
                Resource.Success(it.asListUIModelWatchlistMovie())
            } else {
                Resource.Error(Exception("There is no results in the database.watchlistDao()..."))
            }
        }
    }

    override fun watchlistShows(filterOption: FilterShows): Flow<Resource<List<UIModelWatchlistShow>>> {
        return database.watchlistShowDao().distinctWatchlistShowsDetailsFlow(filterOption).map {
            if (it.isNotEmpty()) {
                Resource.Success(it.asListUIModelWatchlistShow())
            } else {
                Resource.Error(Exception("There is no results in the database.watchlistDao()..."))
            }
        }
    }
}

// todo move these extenstion functions out
fun WatchlistShowWithDetails.asUIModelWatchlistShow() = UIModelWatchlistShow(
    id = details.id,
    title = details.title,
    posterPath = details.posterPath,
    currentEpisodeNumber = status.currentEpisodeNumber,
    currentEpisodeName = status.currentEpisodeName,
    currentSeasonNumber = status.currentSeasonNumber,
    episodesInSeason = status.currentSeasonEpisodeTotal,
    started = status.started,
    upToDate = status.upToDate,
    dateAdded = status.dateAdded
)

fun List<WatchlistShowWithDetails>.asListUIModelWatchlistShow(): List<UIModelWatchlistShow> {
    return this.map { it.asUIModelWatchlistShow() }
}

fun WatchlistMovieWithDetails.asUIModelWatchlistMovie() = UIModelWatchlisMovie(
    id = details.id,
    title = details.title,
    overview = details.overview,
    posterPath = details.posterPath,
    watched = status.watched,
    dateAdded = status.dateAdded,
    dateWatched = status.dateWatched,
    lastUpdated = status.dateLastUpdated
)

fun List<WatchlistMovieWithDetails>.asListUIModelWatchlistMovie(): List<UIModelWatchlisMovie> {
    return this.map { it.asUIModelWatchlistMovie() }
}