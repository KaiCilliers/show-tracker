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
import com.sunrisekcdeveloper.cache.FilterMovies
import com.sunrisekcdeveloper.cache.models.EntityWatchlistMovie
import com.sunrisekcdeveloper.showtracker.common.base.DaoBase
import com.sunrisekcdeveloper.showtracker.common.dao.relations.WatchlistMovieWithDetails
import com.sunrisekcdeveloper.showtracker.common.util.isDateSame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.*

@Dao
abstract class DaoWatchlistMovie : DaoBase<EntityWatchlistMovie> {

    @Query("SELECT * FROM tbl_watchlist_movie WHERE watch_movie_id = :id")
    abstract suspend fun withId(id: String): EntityWatchlistMovie

    @Transaction
    @Query("SELECT * FROM tbl_watchlist_movie WHERE watch_movie_deleted = 0 AND watch_movie_watched = 0")
    abstract suspend fun unwatched(): List<WatchlistMovieWithDetails>

    open fun distinctWithDetailsFlow(filterOption: FilterMovies): Flow<List<WatchlistMovieWithDetails>> =
        privateWithDetailsFlow().map { list ->
            when (filterOption) {
                FilterMovies.NoFilters -> {
                    list
                }
                FilterMovies.Watched -> {
                    list.filter { it.status.watched }
                }
                FilterMovies.Unwatched -> {
                    list.filter { !it.status.watched }
                }
                FilterMovies.AddedToday -> {
                    val now = Calendar.getInstance()
                    now.timeInMillis = System.currentTimeMillis()
                    list.filter {
                        val dateAdded = Calendar.getInstance()
                        dateAdded.timeInMillis = it.status.dateAdded
                        isDateSame(
                            now,
                            dateAdded
                        )
                    }
                }
            }
        }.map { list -> list.sortedBy { it.details.title } }.distinctUntilChanged()

    @Query("SELECT EXISTS(SELECT * FROM tbl_watchlist_movie WHERE watch_movie_id = :id)")
    abstract suspend fun exist(id: String): Boolean

    open fun distinctWatchlistMovieFlow(id: String): Flow<EntityWatchlistMovie?> = watchlistMovieFlow(id).distinctUntilChanged()

    @Query("SELECT * FROM tbl_watchlist_movie WHERE watch_movie_id = :id")
    protected abstract fun watchlistMovieFlow(id: String): Flow<EntityWatchlistMovie?>

    @Transaction
    @Query("SELECT * FROM tbl_watchlist_movie WHERE watch_movie_deleted = 0")
    protected abstract fun privateWithDetailsFlow(): Flow<List<WatchlistMovieWithDetails>>

}