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

package com.sunrisekcdeveloper.showtracker.features.watchlist.data.local

import androidx.room.*
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.*
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.repository.WatchlistMovieDetails
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.repository.WatchlistShowDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Deprecated("Extracted all methods to new table specific Dao's")
@Dao
abstract class DaoWatchlist {

    @Query("SELECT * FROM tbl_show WHERE show_id = :showId")
    abstract suspend fun show(showId: String): EntityShow

    @Query("SELECT * FROM tbl_season WHERE season_show_id = :showId AND season_number = :season")
    abstract suspend fun season(showId: String, season: Int): EntitySeason

    @Query("SELECT * FROM tbl_episode WHERE episode_show_id = :showId AND episode_season_number = :season AND episode_number = :episode")
    abstract suspend fun episode(showId: String, season: Int, episode: Int): EntityEpisode?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWatchlistSeason(season: EntityWatchlistSeason)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWatchlistEpisode(episode: EntityWatchlistEpisode)

    // todo consider simply fetching records and using kotlin #copy method and then insert the records again
    //     like these below
    @Update
    abstract suspend fun updateWatchlistEpisode(episode: EntityWatchlistEpisode)

    @Update
    abstract suspend fun updateWatchlistSeason(season: EntityWatchlistSeason)

    @Update
    abstract suspend fun updateWatchlistShow(show: EntityWatchlistShow)

    @Query(
        """
        SELECT * FROM tbl_watchlist_show WHERE watch_show_id = :showId
    """
    )
    abstract suspend fun watchlistShow(showId: String): EntityWatchlistShow

    @Query(
        """
        SELECT * FROM tbl_watchlist_season WHERE watch_season_show_id = :showId
        AND watch_season_number = :season
    """
    )
    abstract suspend fun watchlistSeason(showId: String, season: Int): EntityWatchlistSeason

    @Query(
        """
        SELECT * FROM tbl_watchlist_episode WHERE watch_episode_show_id = :showId
        AND watch_episode_season_number = :season
        AND watch_episode_episode_number = :episode 
    """
    )
    abstract suspend fun watchlistEpisode(
        showId: String,
        episode: Int,
        season: Int
    ): EntityWatchlistEpisode

    @Transaction
    @Query("SELECT * FROM tbl_watchlist_movie WHERE watch_movie_deleted = 0")
    protected abstract fun privateWatchlistMoviesWithDetailsFlow(): Flow<List<WatchlistMovieDetails>>

    open fun distinctWatchlistMoviesDetailsFlow(sortBy: SortMovies): Flow<List<WatchlistMovieDetails>> =
        privateWatchlistMoviesWithDetailsFlow().map { list ->
            when (sortBy) {
                SortMovies.ByTitle -> {
                    list.sortedBy { it.details.title }
                }
                SortMovies.ByRecentlyAdded -> {
                    list.sortedWith(compareByDescending<WatchlistMovieDetails> {
                        it.watchlist.dateAdded
                    }.thenBy {
                        it.details.title
                    })
                }
                SortMovies.ByWatched -> {
                    list.sortedWith(compareByDescending<WatchlistMovieDetails> {
                        it.watchlist.watched
                    }.thenBy {
                        it.details.title
                    })
                }
            }
        }.distinctUntilChanged()

    @Transaction
    @Query("SELECT * FROM tbl_watchlist_show WHERE watch_show_deleted = 0")
    protected abstract fun privateWatchlistShowsWithDetailsFlow(): Flow<List<WatchlistShowDetails>>

    open fun distinctWatchlistShowsDetailsFlow(sortShows: SortShows) =
        privateWatchlistShowsWithDetailsFlow().map { list ->
            when (sortShows) {
                SortShows.ByTitle -> {
                    list.sortedBy { it.details.title }
                }
                SortShows.ByEpisodesLeftInSeason -> {
                    list.sortedWith(compareBy<WatchlistShowDetails>
                    { it.watchlist.currentSeasonEpisodeTotal - it.watchlist.currentEpisodeNumber }
                        .thenBy { it.details.title }
                    )
                }
                SortShows.ByRecentlyWatched -> {
                    list.sortedWith(compareByDescending<WatchlistShowDetails>
                    { it.watchlist.lastUpdated }.thenBy { it.details.title }
                    )
                }
                SortShows.ByRecentlyAdded -> {
                    list.sortedWith(compareByDescending<WatchlistShowDetails>
                    { it.watchlist.dateAdded }.thenBy { it.details.title }
                    )
                }
                SortShows.ByNotStarted -> {
                    list.sortedWith(compareBy<WatchlistShowDetails>
                    { it.watchlist.started }.thenBy { it.details.title }
                    )
                }
            }
        }.distinctUntilChanged()
}