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
    /**
     * Season
     *
     * Find and return season with matching show ID and season number
     *
     * @param showId : String
     * @param season : Int
     * @return
     */
    @Query("SELECT * FROM tbl_season WHERE season_show_id = :showId AND season_number = :season")
    abstract suspend fun season(showId: String, season: Int): EntitySeason

    /**
     * Seasons of show
     *
     * Excludes special special episodes located in season 0 of a show
     *
     * @param showId : String
     * @return List of EntitySeason
     */
    @Query(
        """
        SELECT * FROM tbl_season WHERE season_show_id = :showId
        AND season_number != 0 ORDER BY season_number
    """
    )
    abstract suspend fun seasonsOfShow(showId: String): List<EntitySeason>

    /**
     * Last season of show
     *
     * @param showId : String
     * @return EntitySeason
     */
    @Query(
        """
        SELECT * FROM tbl_season WHERE season_show_id = :showId ORDER BY season_number DESC LIMIT 1
    """
    )
    abstract suspend fun lastSeasonOfShow(showId: String): EntitySeason
}