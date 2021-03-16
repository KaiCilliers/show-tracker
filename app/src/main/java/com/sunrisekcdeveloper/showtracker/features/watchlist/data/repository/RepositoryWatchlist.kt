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
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityShow
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistShow
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository.RepositoryWatchlistContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.UIModelWatchlisMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.UIModelWatchlistShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepositoryWatchlist(
    private val local: DaoWatchlist
) : RepositoryWatchlistContract {
    override fun watchlistMovies(): Flow<Resource<List<UIModelWatchlisMovie>>> {
        return local.distinctWatchlistMoviesDetailsFlow().map {
            if (it.isNotEmpty()) {
                Resource.Success(it.asListUIModelWatchlistMovie())
            } else {
                Resource.Error("There is no results in the database...")
            }
        }
    }

    override fun watchlistShows(): Flow<Resource<List<UIModelWatchlistShow>>> {
        return local.distinctWatchlistShowsDetailsFlow().map {
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
    episodesInSeason = -1,
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
    overview =  details.overview,
    posterPath = details.posterPath,
    watched = watchlist.watched,
    dateAdded = watchlist.dateAdded,
    dateWatched = watchlist.dateWatched,
    lastUpdated = watchlist.dateLastUpdated
)

fun List<WatchlistMovieDetails>.asListUIModelWatchlistMovie() : List<UIModelWatchlisMovie> {
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