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
import com.sunrisekcdeveloper.cache.FilterShows
import com.sunrisekcdeveloper.cache.models.EntityWatchlistShow
import com.sunrisekcdeveloper.showtracker.common.base.DaoBase
import com.sunrisekcdeveloper.showtracker.common.dao.relations.WatchlistShowWithDetails
import com.sunrisekcdeveloper.showtracker.common.util.isDateSame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.*

@Dao
abstract class DaoWatchlistShow : DaoBase<EntityWatchlistShow> {

    @Query("SELECT EXISTS(SELECT * FROM tbl_watchlist_show WHERE watch_show_id = :id)")
    abstract suspend fun exist(id: String): Boolean

    // todo rename to notStarted
    @Transaction
    @Query("SELECT * FROM tbl_watchlist_show WHERE watch_show_deleted = 0 AND watch_show_started = 0")
    abstract suspend fun unwatched(): List<WatchlistShowWithDetails>

    @Query("SELECT * FROM tbl_watchlist_show WHERE watch_show_id = :showId")
    abstract suspend fun withId(showId: String): EntityWatchlistShow

    // TODO placing this filter logic in DAO is questionable
    open fun distinctWithDetailsFlow(sortShows: FilterShows): Flow<List<WatchlistShowWithDetails>> =
        privateWithDetailsFlow().map { list ->
            when (sortShows) {
                FilterShows.NoFilters -> {
                    list
                }
                FilterShows.AddedToday -> {
                    val now = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
                    list.filter {
                        val added = Calendar.getInstance().apply { timeInMillis = it.status.dateAdded }
                        isDateSame(now, added)
                    }
                }
                FilterShows.WatchedToday -> {
                    val now = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
                    list.filter {
                        val lastUpdated = Calendar.getInstance().apply { timeInMillis = it.status.lastUpdated }
                        isDateSame(now, lastUpdated)
                    }
                }
                FilterShows.Started -> {
                    list.filter { it.status.started && !it.status.upToDate}
                }
                FilterShows.NotStarted -> {
                    list.filter { !it.status.started }
                }
            }
        }.map { it.sortedBy { it.details.title } }.distinctUntilChanged()

    open fun distinctWatchlistShowFlow(id: String): Flow<EntityWatchlistShow?> = watchlistShowFlow(id).distinctUntilChanged()

    @Query("SELECT * FROM tbl_watchlist_show WHERE watch_show_id = :id")
    protected abstract fun watchlistShowFlow(id: String): Flow<EntityWatchlistShow?>

    @Transaction
    @Query("SELECT * FROM tbl_watchlist_show WHERE watch_show_deleted = 0")
    protected abstract fun privateWithDetailsFlow(): Flow<List<WatchlistShowWithDetails>>
}