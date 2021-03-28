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

package com.sunrisekcdeveloper.showtracker.common.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.sunrisekcdeveloper.showtracker.common.base.DaoBase
import com.sunrisekcdeveloper.showtracker.common.dao.combined.WatchlistShowWithDetails
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.SortShows
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistShow
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.repository.WatchlistShowDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Dao
abstract class DaoWatchlistShow : DaoBase<EntityWatchlistShow> {
    /**
     * Unwatched shows
     *
     * Watchlist shows with their details
     *
     * @return List of WatchlistShowWithDetails
     */
    @Transaction
    @Query("SELECT * FROM tbl_watchlist_show WHERE watch_show_deleted = 0 AND watch_show_started = 0")
    abstract suspend fun unwatchedShows(): List<WatchlistShowWithDetails>

    /**
     * Watchlist show
     *
     * Find and return watchlisted show with matching show ID
     *
     * @param showId : String
     * @return
     */
    @Query("SELECT * FROM tbl_watchlist_show WHERE watch_show_id = :showId")
    abstract suspend fun watchlistShow(showId: String): EntityWatchlistShow

    /**
     * Private watchlist shows with details flow
     *
     * Return a Flow of all watchlisted shows with their details. Internal use only and is
     * accompanied with a public accessor method
     *
     * @return Flow emitting lists of WatchlistShowWithDetails
     */
    @Transaction
    @Query("SELECT * FROM tbl_watchlist_show WHERE watch_show_deleted = 0")
    protected abstract fun privateWatchlistShowsWithDetailsFlow(): Flow<List<WatchlistShowDetails>>

    /**
     * Distinct watchlist shows details flow
     *
     * Return a sorted flow of watchlisted shows
     *
     * @param sortShows : SortShows sealed class
     * @return sorted Flow emitting lists of WatchlistShowDetails
     */
    open fun distinctWatchlistShowsDetailsFlow(sortShows: SortShows): Flow<List<WatchlistShowDetails>> =
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

    /**
     * Watchlist show exist
     *
     * @param id : String
     * @return Boolean that indicates if record exists with matching ID
     */
    @Query("SELECT EXISTS(SELECT * FROM tbl_watchlist_show WHERE watch_show_id = :id)")
    abstract suspend fun watchlistShowExist(id: String): Boolean

    /**
     * Watchlist show flow
     *
     * Internal use only
     *
     * @param id : String
     * @return Flow emitting EntityWatchlistShow
     */
    @Query("SELECT * FROM tbl_watchlist_show WHERE watch_show_id = :id")
    protected abstract fun watchlistShowFlow(id: String): Flow<EntityWatchlistShow?>

    /**
     * Distinct watchlist show flow
     *
     * Return a Flow to receive the latest record that matches ID provided
     *
     * @param id : String
     * @return Flow emitting EntityWatchlistShow
     */
    open fun distinctWatchlistShowFlow(id: String): Flow<EntityWatchlistShow?> = watchlistShowFlow(id).distinctUntilChanged()

    @Deprecated("Use basic @Update function")
    @Query("UPDATE tbl_watchlist_show SET watch_show_deleted = 0 WHERE watch_show_id = :id")
    abstract suspend fun markWatchlistShowAsNotDeleted(id: String)

    @Deprecated("Use basic @Update function")
    @Query("UPDATE tbl_watchlist_show SET watch_show_deleted = 1, watch_show_deleted_date = :timeStamp WHERE watch_show_id = :id ")
    protected abstract suspend fun privateRemoveShowFromWatchlist(id: String, timeStamp: Long)

    @Deprecated("Use basic @Update function")
    @Query("UPDATE tbl_watchlist_show SET watch_show_last_updated = :timeStamp WHERE watch_show_id = :id")
    abstract suspend fun updateWatchlistShowUpdatedAt(id: String, timeStamp: Long)

    /**
     * Remove show from watchlist
     *
     * @param id : String
     */
    @Transaction
    open suspend fun removeShowFromWatchlist(id: String) {
        privateRemoveShowFromWatchlist(id, System.currentTimeMillis())
        updateWatchlistShowUpdatedAt(id, System.currentTimeMillis())
    }
}