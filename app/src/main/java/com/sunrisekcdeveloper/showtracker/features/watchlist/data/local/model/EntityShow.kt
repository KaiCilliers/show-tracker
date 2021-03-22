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

package com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.CoroutineName

// todo consider removing prefixes

@Entity(tableName = "tbl_show")
data class EntityShow(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "show_id") val id: String,
    @ColumnInfo(name = "show_title") val title: String,
    @ColumnInfo(name = "show_overview") val overview: String,
    @ColumnInfo(name = "show_certification") val certification: String,
    @ColumnInfo(name = "show_poster_path") val posterPath: String,
    @ColumnInfo(name = "show_backdrop_path") val backdropPath: String,
    @ColumnInfo(name = "show_popularity") val popularityValue: Float,
    @ColumnInfo(name = "show_first_air_date") val firstAirDate: String,
    @ColumnInfo(name = "show_rating") val rating: Float,
    @ColumnInfo(name = "show_episode_total") val episodeTotal: Int,
    @ColumnInfo(name = "show_season_total") val seasonTotal: Int,
    @ColumnInfo(name = "show_last_updated") val lastUpdated: Long
)

@Entity(tableName = "tbl_watchlist_show")
data class EntityWatchlistShow(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "watch_show_id") val id: String,
    @ColumnInfo(name = "watch_show_current_episode_num") val currentEpisodeNumber: Int,
    @ColumnInfo(name = "watch_show_current_episode_name") val currentEpisodeName: String,
    @ColumnInfo(name = "watch_show_current_season_num") val currentSeasonNumber: Int,
    @ColumnInfo(name = "watch_show_current_season_episode_total") val currentSeasonEpisodeTotal: Int,
    @ColumnInfo(name = "watch_show_started") val started: Boolean,
    @ColumnInfo(name = "watch_show_up_to_date") val upToDate: Boolean,
    @ColumnInfo(name = "watch_show_deleted") val deleted: Boolean = false,
    @ColumnInfo(name = "watch_show_deleted_date") val dateDeleted: Long = -1L,
    @ColumnInfo(name = "watch_show_date_added") val dateAdded: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "watch_show_last_updated") val lastUpdated: Long = System.currentTimeMillis()
) {
    companion object {
        fun partialFrom(
            id: String,
            episodeNumber: Int,
            episodeName: String,
            seasonNumber: Int,
            seasonEpisodeTotal: Int
        ) = EntityWatchlistShow(
            id = id,
            currentEpisodeNumber = episodeNumber,
            currentEpisodeName = episodeName,
            currentSeasonNumber = seasonNumber,
            currentSeasonEpisodeTotal = seasonEpisodeTotal,
            started = true,
            upToDate = false
        )
        fun upToDateEntryFrom(
            id: String,
            episodeNumber: Int,
            episodeName: String,
            seasonNumber: Int,
            seasonEpisodeTotal: Int
        ) = EntityWatchlistShow(
            id = id,
            currentEpisodeNumber = episodeNumber,
            currentEpisodeName = episodeName,
            currentSeasonNumber = seasonNumber,
            currentSeasonEpisodeTotal = seasonEpisodeTotal,
            started = true,
            upToDate = true
        )

        // todo i dont want to track shows without episode and season data
        fun freshBareEntryFrom(id: String) = EntityWatchlistShow(
            id = id,
            currentEpisodeNumber = -1,
            currentEpisodeName = "",
            currentSeasonEpisodeTotal = -1,
            currentSeasonNumber = -1,
            started = false,
            upToDate = false,
            dateAdded = System.currentTimeMillis(),
            lastUpdated = System.currentTimeMillis()
        )
    }
}

@Entity(
    tableName = "tbl_season",
    primaryKeys = ["season_show_id", "season_id"]
)
data class EntitySeason(
    @ColumnInfo(name = "season_show_id") val showId: String,
    @ColumnInfo(name = "season_id") val id: Int,
    @ColumnInfo(name = "season_number") val number: Int,
    @ColumnInfo(name = "season_name") val name: String,
    @ColumnInfo(name = "season_overview") val overview: String,
    @ColumnInfo(name = "season_poster_path") val posterPath: String,
    @ColumnInfo(name = "season_air_date") val airDate: Long,
    @ColumnInfo(name = "season_episode_total") val episodeTotal: Int,
    @ColumnInfo(name = "season_last_updated") val lastUpdated: Long
)

@Entity(
    tableName = "tbl_episode",
    primaryKeys = ["episode_show_id", "episode_season_number", "episode_number"]
)
data class EntityEpisode( // todo consider renaming of table columns without prefix
    @ColumnInfo(name = "episode_show_id") val showId: String,
    @ColumnInfo(name = "episode_season_number") val seasonNumber: Int,
    @ColumnInfo(name = "episode_number") val number: Int,
    @ColumnInfo(name = "episode_name") val name: String,
    @ColumnInfo(name = "episode_air_date") val airDate: Long,
    @ColumnInfo(name = "episode_overview") val overview: String,
    @ColumnInfo(name = "episode_still_path") val stillPath: String,
    @ColumnInfo(name = "episode_last_updated") val lastUpdated: Long // todo last_updated fields should omit prefix as it is not related to the entity, but rather the record
)

@Entity(
    tableName = "tbl_watchlist_season",
    primaryKeys = ["watch_season_show_id", "watch_season_number"]
)
data class EntityWatchlistSeason(
    @ColumnInfo(name = "watch_season_show_id") val showId: String,
    @ColumnInfo(name = "watch_season_number") val number: Int,
    @ColumnInfo(name = "watch_season_date_started") val dateStarted: Long,
    @ColumnInfo(name = "watch_season_completed") val completed: Boolean,
    @ColumnInfo(name = "watch_season_date_completed") val dateCompleted: Long,
    @ColumnInfo(name = "watch_season_current_episode") val currentEpisode: Int,
    @ColumnInfo(name = "watch_season_started_tracking_season") val startedTrackingSeason: Boolean,
    @ColumnInfo(name = "watch_season_finished_before_tracking") val finishedBeforeTracking: Boolean,
    @ColumnInfo(name = "watch_season_last_updated") val lastUpdated: Long
) {
    companion object {
        fun partialFrom(showId: String, seasonNumber: Int, currentEpisode: Int) = EntityWatchlistSeason(
            showId = showId,
            number = seasonNumber,
            dateStarted = System.currentTimeMillis(),
            dateCompleted = -1L,
            completed = false,
            currentEpisode = currentEpisode,
            startedTrackingSeason = true,
            finishedBeforeTracking = false,
            lastUpdated = System.currentTimeMillis()
        )
        fun upToDateSeasonFrom(showId: String, seasonNumber: Int, currentEpisode: Int) = EntityWatchlistSeason(
            showId = showId,
            number = seasonNumber,
            dateStarted = System.currentTimeMillis(),
            dateCompleted = System.currentTimeMillis(),
            completed = true,
            currentEpisode = currentEpisode,
            startedTrackingSeason = false,
            finishedBeforeTracking = true,
            lastUpdated = System.currentTimeMillis()
        )
        fun freshCompleted(showId: String, seasonNumber: Int) = EntityWatchlistSeason(
            showId = showId,
            number = seasonNumber,
            dateStarted = System.currentTimeMillis(),
            dateCompleted = System.currentTimeMillis(),
            completed = true,
            currentEpisode = -1,
            startedTrackingSeason = false,
            finishedBeforeTracking = true,
            lastUpdated = System.currentTimeMillis()
        )
    }
}

@Entity(
    tableName = "tbl_watchlist_episode",
    primaryKeys = ["watch_episode_show_id", "watch_episode_episode_number"]
)
data class EntityWatchlistEpisode(
    @ColumnInfo(name = "watch_episode_show_id") val showId: String,
    @ColumnInfo(name = "watch_episode_episode_number") val episodeNumber: Int,
    @ColumnInfo(name = "watch_episode_season_number") val seasonNumber: Int,
    @ColumnInfo(name = "watch_episode_watched") val watched: Boolean,
    @ColumnInfo(name = "watch_episode_initial_set_progress_batch") val initialSetProgressBatch: Boolean, // todo consider removing this flag cuase i am not craeting records for previous episode in initial progress
    @ColumnInfo(name = "watch_episode_via_up_to_date_action") val viaUpToDateAction: Boolean,
    @ColumnInfo(name = "watch_episode_date_watched") val dateWatched: Long,
    @ColumnInfo(name = "watch_episode_on_episode_since_date") val onEpisodeSinceDate: Long,
    @ColumnInfo(name = "watch_episode_last_updated") val lastUpdated: Long
) {
    companion object {
        fun notWatchedFrom(
            showId: String,
            seasonNumber: Int,
            episodeNumber: Int
        ) = EntityWatchlistEpisode(
            showId = showId,
            episodeNumber = episodeNumber,
            seasonNumber = seasonNumber,
            watched = false,
            initialSetProgressBatch = true, // todo consider removing field
            viaUpToDateAction = false, // todo consider removing field
            dateWatched = -1L,
            onEpisodeSinceDate = System.currentTimeMillis(),
            lastUpdated = System.currentTimeMillis()
        )
        fun completedFrom(
            showId: String,
            seasonNumber: Int,
            episodeNumber: Int
        ) = EntityWatchlistEpisode(
            showId = showId,
            episodeNumber = episodeNumber,
            seasonNumber = seasonNumber,
            watched = true,
            initialSetProgressBatch = true, // todo consider removing field
            viaUpToDateAction = true,
            dateWatched = System.currentTimeMillis(),
            onEpisodeSinceDate = System.currentTimeMillis(),
            lastUpdated = System.currentTimeMillis()
        )

    }
}

@Entity(tableName = "tbl_watchlist_batch")
data class EntityWatchlistBatch(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "batch_id") val id: Int,
    @ColumnInfo(name = "batch_show_id") val showId: Int,
    @ColumnInfo(name = "batch_start_season") val startSeason: Int,
    @ColumnInfo(name = "batch_end_season") val endSeason: Int,
    @ColumnInfo(name = "batch_start_episode") val startEpisode: Int,
    @ColumnInfo(name = "batch_end_episode") val endEpisode: Int,
    @ColumnInfo(name = "batch_resulted_in_up_to_date_status") val resultedInUpToDateStatus: Boolean
)