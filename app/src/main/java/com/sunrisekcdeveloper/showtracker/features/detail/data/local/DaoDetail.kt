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

package com.sunrisekcdeveloper.showtracker.features.detail.data.local

import androidx.room.*
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityShow
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class DaoDetail {

    @Query("SELECT EXISTS(SELECT * FROM tbl_watchlist_movie WHERE watch_movie_id = :id)")
    abstract suspend fun watchlistMovieExist(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insertWatchlistMovie(entity: EntityWatchlistMovie)

    @Query("DELETE FROM tbl_watchlist_movie WHERE watch_movie_id = :id")
    protected abstract suspend fun privateRemoveMovieFromWatchlist(id: String)

    // todo transaction
    @Query("DELETE FROM tbl_watchlist_show WHERE watch_show_id = :id")
    abstract suspend fun removeShowFromWatchlist(id: String)

    @Insert
    abstract suspend fun addShowToWatchlist(entity: EntityWatchlistShow)

    @Query("SELECT * FROM tbl_show WHERE show_id = :id")
    protected abstract fun showDetails(id: String): Flow<EntityShow?>

    fun distinctShowDetailFlow(id: String) = showDetails(id).distinctUntilChanged()

    @Query("SELECT * FROM tbl_watchlist_show WHERE watch_show_id = :id")
    protected abstract fun watchlistShowFlow(id: String): Flow<EntityWatchlistShow?>

    fun distinctWatchlistShowFlow(id: String) = watchlistShowFlow(id).distinctUntilChanged()

    @Query("SELECT * FROM tbl_movie WHERE movie_id = :id")
    protected abstract fun movieDetail(id: String): Flow<EntityMovie?>

    fun distinctMovieDetailFlow(id: String) = movieDetail(id).distinctUntilChanged()

    @Query("SELECT * FROM tbl_watchlist_movie WHERE watch_movie_id = :id")
    protected abstract fun watchlistMovieFlow(id: String): Flow<EntityWatchlistMovie?>

    fun distinctWatchlistMovieFlow(id: String) = watchlistMovieFlow(id).distinctUntilChanged()

    @Query("UPDATE tbl_watchlist_movie SET watch_movie_watched = 1  WHERE watch_movie_id = :id")
    protected abstract suspend fun privateSetMovieAsWatched(id: String)

    @Query("UPDATE tbl_watchlist_movie SET watch_movie_watched = 0  WHERE watch_movie_id = :id")
    protected abstract suspend fun privateSetMovieAsNotWatched(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addMovieToWatchlist(entity: EntityWatchlistMovie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addMovieDetails(entity: EntityMovie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addShowDetails(entity: EntityShow)

    @Update
    abstract suspend fun updateMovieStatus(entity: EntityWatchlistMovie)

    @Query("UPDATE tbl_watchlist_movie SET watch_movie_last_updated = :timeStamp WHERE watch_movie_id = :id")
    abstract suspend fun updateWatchlistMovieUpdatedAt(id: String, timeStamp: Long)

    @Transaction
    open suspend fun setMovieAsWatched(id: String) {
        privateSetMovieAsWatched(id)
        updateWatchlistMovieUpdatedAt(id, System.currentTimeMillis())
    }
    @Transaction
    open suspend fun setMovieAsNotWatched(id: String) {
        privateSetMovieAsNotWatched(id)
        updateWatchlistMovieUpdatedAt(id, System.currentTimeMillis())
    }

    // todo dont remove from watchlist, but add field indicating removed
    //  so as to keep record for a while. You can clean up database of
    //  these records later using work manager
    @Transaction
    open suspend fun removeMovieFromWatchlist(id: String) {
        privateRemoveMovieFromWatchlist(id)
        updateWatchlistMovieUpdatedAt(id, System.currentTimeMillis())
    }


}