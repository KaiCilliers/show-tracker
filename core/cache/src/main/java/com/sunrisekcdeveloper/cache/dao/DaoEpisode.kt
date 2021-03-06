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

package com.sunrisekcdeveloper.cache.dao

import androidx.room.Dao
import androidx.room.Query
import com.sunrisekcdeveloper.cache.models.EntityEpisode

@Dao
interface DaoEpisode : DaoBase<EntityEpisode> {

    @Query("""
         SELECT * FROM tbl_episode WHERE episode_show_id = :showId
            AND episode_season_number = :season ORDER BY episode_number ASC LIMIT 1 
    """)
    suspend fun firstInSeason(showId: String, season: Int): EntityEpisode

    @Query("SELECT * FROM tbl_episode WHERE episode_show_id = :showId AND episode_season_number = :season AND episode_number = :episode")
    suspend fun withId(showId: String, season: Int, episode: Int): EntityEpisode

    @Query("SELECT * FROM tbl_episode WHERE episode_show_id = :showId AND episode_season_number = :season")
    suspend fun allInSeason(showId: String, season: Int): List<EntityEpisode>

    @Query(
        """
           SELECT * FROM tbl_episode WHERE episode_show_id = :showId
            AND episode_season_number = :season ORDER BY episode_number DESC LIMIT 1 
        """
    )
    suspend fun lastInSeason(showId: String, season: Int): EntityEpisode
}