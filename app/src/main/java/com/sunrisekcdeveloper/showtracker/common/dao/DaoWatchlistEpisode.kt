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
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistEpisode

@Dao
interface DaoWatchlistEpisode : DaoBase<EntityWatchlistEpisode> {
    /**
     * Watchlist episode
     *
     * Find and return watchlisted show's episode with matching show ID, season number, and
     * episode number
     *
     * @param showId : String
     * @param episode : Int
     * @param season : Int
     * @return
     */
    @Query(
        """
        SELECT * FROM tbl_watchlist_episode WHERE watch_episode_show_id = :showId
        AND watch_episode_season_number = :season
        AND watch_episode_episode_number = :episode 
    """
    )
    abstract suspend fun watchlistEpisode(
        showId: String,
        episode: Int,
        season: Int
    ): EntityWatchlistEpisode
}