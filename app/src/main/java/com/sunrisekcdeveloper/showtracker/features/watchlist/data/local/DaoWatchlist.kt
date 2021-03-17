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

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.repository.WatchlistMovieDetails
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.repository.WatchlistShowDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class DaoWatchlist {

    @Transaction
    @Query("SELECT * FROM tbl_watchlist_movie")
    protected abstract fun privateWatchlistMoviesWithDetailsFlow(): Flow<List<WatchlistMovieDetails>>

    fun distinctWatchlistMoviesDetailsFlow() = privateWatchlistMoviesWithDetailsFlow().distinctUntilChanged()

    @Transaction // todo check that all such transactions are marked as Transaction (with return type objecct with @Relation tag)
    @Query("SELECT * FROM tbl_watchlist_show")
    protected abstract fun privateWatchlistShowsWithDetailsFlow(): Flow<List<WatchlistShowDetails>>

    fun distinctWatchlistShowsDetailsFlow() = privateWatchlistShowsWithDetailsFlow().distinctUntilChanged()
}