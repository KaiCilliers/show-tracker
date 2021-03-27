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
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class DaoShow : DaoBase<EntityShow> {

    /**
     * Show
     *
     * Find and return a show with matching ID
     *
     * @param showId: String
     * @return
     */
    @Query("SELECT * FROM tbl_show WHERE show_id = :showId")
    abstract suspend fun show(showId: String): EntityShow

    /**
     * Show details
     *
     * Internal use only
     *
     * @param id : String
     * @return Flow emitting shows with matching ID
     */
    @Query("SELECT * FROM tbl_show WHERE show_id = :id")
    protected abstract fun showDetails(id: String): Flow<EntityShow?>

    /**
     * Distinct show detail flow
     *
     * Return a flow to always have the latest record with that matches ID provided
     *
     * @param id : String
     * @return Flow emitting distinct shows with matching ID
     */
    open fun distinctShowDetailFlow(id: String): Flow<EntityShow?> = showDetails(id).distinctUntilChanged()
}