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
import com.sunrisekcdeveloper.showtracker.common.base.DaoBase
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntitySeason

@Dao
interface DaoSeason : DaoBase<EntitySeason> {

    @Query("SELECT * FROM tbl_season WHERE season_show_id = :showId AND season_number = :season")
    suspend fun withId(showId: String, season: Int): EntitySeason

    @Query(
        """
        SELECT * FROM tbl_season WHERE season_show_id = :showId
        AND season_number != 0 ORDER BY season_number
    """
    )
    suspend fun allFromShow(showId: String): List<EntitySeason>

    @Query(
        """
        SELECT * FROM tbl_season WHERE season_show_id = :showId ORDER BY season_number DESC LIMIT 1
    """
    )
    suspend fun lastInShow(showId: String): EntitySeason
}