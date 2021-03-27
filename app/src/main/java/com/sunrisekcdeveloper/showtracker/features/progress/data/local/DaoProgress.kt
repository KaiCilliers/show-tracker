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

package com.sunrisekcdeveloper.showtracker.features.progress.data.local

import androidx.room.*
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.*

@Deprecated("Extracted all methods to new table specific Dao's")
@Dao
abstract class DaoProgress {

    @Transaction
    @Deprecated("Utilize database.witTransaction to do this")
    open suspend fun setShowProgress(
        show: EntityWatchlistShow,
        season: EntityWatchlistSeason,
        episode: EntityWatchlistEpisode
    ) {
        insertWatchlistEpisode(episode)
        insertWatchlistSeason(season)
        insertWatchlistShow(show)
    }

    @Query("SELECT * FROM tbl_season WHERE season_show_id = :showId AND season_number = :season")
    abstract suspend fun season(showId: String, season: Int): EntitySeason

    @Query("SELECT * FROM tbl_episode WHERE episode_show_id = :showId AND episode_season_number = :season AND episode_number = :episode")
    abstract suspend fun episode(showId: String, season: Int, episode: Int): EntityEpisode

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWatchlistEpisode(episode: EntityWatchlistEpisode)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWatchlistSeason(season: EntityWatchlistSeason)

    // todo notify user about this... idk even if this usecase will occur
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWatchlistShow(show: EntityWatchlistShow)

    // exclude special episodes stored in seasons marked 0
    @Query(
        """
        SELECT * FROM tbl_season WHERE season_show_id = :showId
        AND season_number != 0 ORDER BY season_number
    """
    )
    abstract suspend fun seasonsOfShow(showId: String): List<EntitySeason>

    @Query(
        """
        SELECT * FROM tbl_season WHERE season_show_id = :showId ORDER BY season_number DESC LIMIT 1
    """
    )
    abstract suspend fun lastSeasonOfShow(showId: String): EntitySeason

    @Query(
        """
           SELECT * FROM tbl_episode WHERE episode_show_id = :showId
            AND episode_season_number = :season ORDER BY episode_number DESC LIMIT 1 
        """
    )
    abstract suspend fun lastEpisodeOfShow(showId: String, season: Int): EntityEpisode

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addSeason(season: EntitySeason)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addEpisode(episode: EntityEpisode)
}