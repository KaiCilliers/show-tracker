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
    tableName = "tbl_episode",
    primaryKeys = [
        "episode_trakt_id", "episode_tvdb_id",
        "episode_imdb_id", "episode_tmdb_id"
    ]
)
data class EpisodeEntity(
    @ColumnInfo(name = "episode_season")
    val season: Int,
    @ColumnInfo(name = "episode_number")
    val number: Int,
    @ColumnInfo(name = "episode_title")
    val title: String,
    @ColumnInfo(name = "episode_overview")
    val overview: String,
    @ColumnInfo(name = "episode_first_aired_date")
    val firstAired: String,
    @ColumnInfo(name = "episode_updated_at_date")
    val updatedAt: String,
    @ColumnInfo(name = "episode_rating")
    val rating: Int,
    @ColumnInfo(name = "episode_vote_count")
    val votes: Int,
    @ColumnInfo(name = "episode_comment_count")
    val comments: Int,
    @ColumnInfo(name = "episode_runtime")
    val runtime: Int,
    @ColumnInfo(name = "episode_trakt_id")
    val traktId: String,
    @ColumnInfo(name = "episode_tvdb_id")
    val tvdbId: String,
    @ColumnInfo(name = "episode_imdb_id")
    val imdbId: String,
    @ColumnInfo(name = "episode_tmdb_id")
    val tmdbId: String
)
