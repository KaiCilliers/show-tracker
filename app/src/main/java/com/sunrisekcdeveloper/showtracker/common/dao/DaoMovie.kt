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
import com.sunrisekcdeveloper.showtracker.common.dao.combined.WatchlistMovieWithDetails
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.SortMovies
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.repository.WatchlistMovieDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Dao
abstract class DaoMovie : DaoBase<EntityMovie> {

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

    /**
     * Movie detail
     *
     * Internal use only
     *
     * @param id : String
     * @return Flow emitting EntityMovie
     */
    @Query("SELECT * FROM tbl_movie WHERE movie_id = :id")
    protected abstract fun movieDetail(id: String): Flow<EntityMovie?>

    /**
     * Distinct movie detail flow
     *
     * Return Flow to receive latest record that matches ID provided
     *
     * @param id : String
     * @return Flow emitting distinct EntityMovie
     */
    open fun distinctMovieDetailFlow(id: String): Flow<EntityMovie?> = movieDetail(id).distinctUntilChanged()
}