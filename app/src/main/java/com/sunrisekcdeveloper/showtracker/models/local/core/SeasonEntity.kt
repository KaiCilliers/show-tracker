/*
 * Copyright © 2020. The Android Open Source Project
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

package com.sunrisekcdeveloper.showtracker.models.local.core

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "tbl_season",
    primaryKeys = ["season_trakt_id", "season_tvdb_id", "season_tmdb_id"]
)
data class SeasonEntity(
    @ColumnInfo(name = "season_trakt_id")
    val traktId: String,
    @ColumnInfo(name = "season_tvdb_id")
    val tvdbId: String,
    @ColumnInfo(name = "season_tmdb_id")
    val tmdbId: String,
    @ColumnInfo(name = "season_number")
    val number: Int,
    @ColumnInfo(name = "season_rating")
    val rating: Int,
    @ColumnInfo(name = "season_vote_count")
    val votes: Int,
    @ColumnInfo(name = "season_episode_count")
    val episodeCount: Int,
    @ColumnInfo(name = "season_aired_episode_count")
    val airedEpisodes: Int,
    @ColumnInfo(name = "season_title")
    val title: String,
    @ColumnInfo(name = "season_overview")
    val overview: String?,
    @ColumnInfo(name = "season_first_aired_date")
    val firstAired: String,
    @ColumnInfo(name = "season_updated_at_date")
    val updatedAt: String
)
