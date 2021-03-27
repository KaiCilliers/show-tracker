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

import androidx.room.Query
import androidx.room.Transaction
import com.sunrisekcdeveloper.showtracker.common.dao.combined.WatchlistMovieWithDetails
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.SortMovies
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.repository.WatchlistMovieDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

abstract class DaoWatchlistMovie : DaoBase<EntityWatchlistMovie> {
    /**
     * Unwatched movies
     *
     * Returns all watchlisted movies with their details
     *
     * @return List of WatchlistMovieWithDetails
     */
    @Transaction
    @Query("SELECT * FROM tbl_watchlist_movie WHERE watch_movie_deleted = 0 AND watch_movie_watched = 0")
    abstract suspend fun unwatchedMovies(): List<WatchlistMovieWithDetails>

    /**
     * Distinct watchlist movies details flow
     *
     * Return a sorted flow of watchlisted movies
     *
     * @param sortBy : SortMovies sealed class
     * @return sorted Flow emitting lists of WatchlistMovieDetails
     */
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

    /**
     * Private watchlist movies with details flow
     *
     * Return a Flow of all watchlisted movies with their details. Internal use only and is
     * accompanied with a public accessor method
     *
     * @return Flow emitting lists of WatchlistMovieWithDetails
     */
    @Transaction
    @Query("SELECT * FROM tbl_watchlist_movie WHERE watch_movie_deleted = 0")
    protected abstract fun privateWatchlistMoviesWithDetailsFlow(): Flow<List<WatchlistMovieDetails>>

    /**
     * Watchlist movie exist
     *
     * @param id : String
     * @return Boolean that indicates if record exists with matching ID
     */
    @Query("SELECT EXISTS(SELECT * FROM tbl_watchlist_movie WHERE watch_movie_id = :id)")
    abstract suspend fun watchlistMovieExist(id: String): Boolean

    /**
     * Private remove movie from watchlist
     *
     * Update record by marking it as deleted by updated the deleted flag and date deleted
     *
     * @param id : String
     * @param timeStamp : Long
     */
    @Deprecated("use simple @Update function (even internally in open function to update last_updated field)")
    @Query("UPDATE tbl_watchlist_movie SET watch_movie_deleted = 1, watch_movie_deleted_date = :timeStamp WHERE watch_movie_id = :id")
    protected abstract suspend fun privateRemoveMovieFromWatchlist(id: String, timeStamp: Long)

    /**
     * Watchlist movie flow
     *
     * Internal use only
     *
     * @param id : String
     * @return Flow emitting EntityWatchlistMovie
     */
    @Query("SELECT * FROM tbl_watchlist_movie WHERE watch_movie_id = :id")
    protected abstract fun watchlistMovieFlow(id: String): Flow<EntityWatchlistMovie?>

    /**
     * Distinct watchlist movie flow
     *
     * Return Flow to receive latest record that matches ID provided
     *
     * @param id : String
     * @return Flow emitting distinct EntityWatchlistMovie
     */
    open fun distinctWatchlistMovieFlow(id: String): Flow<EntityWatchlistMovie?> = watchlistMovieFlow(id).distinctUntilChanged()

    @Deprecated("Use basic @Update function")
    @Query("UPDATE tbl_watchlist_movie SET watch_movie_watched = 1  WHERE watch_movie_id = :id")
    protected abstract suspend fun privateSetMovieAsWatched(id: String)

    @Deprecated("Use basic @Update function")
    @Query("UPDATE tbl_watchlist_movie SET watch_movie_watched = 0  WHERE watch_movie_id = :id")
    protected abstract suspend fun privateSetMovieAsNotWatched(id: String)

    @Deprecated("Use basic @Update function")
    @Query("UPDATE tbl_watchlist_movie SET watch_movie_deleted = 0 WHERE watch_movie_id = :id")
    abstract suspend fun markWatchlistMovieAsNotDeleted(id: String)

    @Deprecated("Use basic @Update function")
    @Query("UPDATE tbl_watchlist_movie SET watch_movie_last_updated = :timeStamp WHERE watch_movie_id = :id")
    abstract suspend fun updateWatchlistMovieUpdatedAt(id: String, timeStamp: Long)

    /**
     * Set movie as watched
     *
     * @param id : String
     */
    @Transaction
    open suspend fun setMovieAsWatched(id: String) {
        privateSetMovieAsWatched(id)
        updateWatchlistMovieUpdatedAt(id, System.currentTimeMillis())
    }

    /**
     * Set movie as not watched
     *
     * @param id : String
     */
    @Transaction
    open suspend fun setMovieAsNotWatched(id: String) {
        privateSetMovieAsNotWatched(id)
        updateWatchlistMovieUpdatedAt(id, System.currentTimeMillis())
    }

    /**
     * Remove movie from watchlist
     *
     * @param id : String
     */
    @Transaction
    open suspend fun removeMovieFromWatchlist(id: String) {
        privateRemoveMovieFromWatchlist(id, System.currentTimeMillis())
        updateWatchlistMovieUpdatedAt(id, System.currentTimeMillis())
    }
}