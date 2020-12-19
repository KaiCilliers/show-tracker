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

package com.sunrisekcdeveloper.showtracker.data.local.model.core

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_show")
data class ShowEntity(
    @PrimaryKey
    @ColumnInfo(name = "show_slug")
    val slug: String,
    @ColumnInfo(name = "show_title")
    val title: String,
    @ColumnInfo(name = "show_year")
    val year: String,
    @ColumnInfo(name = "show_overview")
    val overview: String,
    @ColumnInfo(name = "show_first_aired_date")
    val firstAired: String,
    @ColumnInfo(name = "show_usual_air_day")
    val usualAirDay: String,
    @ColumnInfo(name = "show_usual_air_time")
    val usualAirTime: String,
    @ColumnInfo(name = "show_usual_air_timezone")
    val usualAirTimezone: String,
    @ColumnInfo(name = "show_runtime")
    val runtime: Int,
    @ColumnInfo(name = "show_certification")
    val certification: String,
    @ColumnInfo(name = "show_trailer_url")
    val trailerUrl: String?,
    @ColumnInfo(name = "show_homepage_url")
    val homepageUrl: String,
    @ColumnInfo(name = "show_status")
    val status: String,
    @ColumnInfo(name = "show_vote_count")
    val votes: Int,
    @ColumnInfo(name = "show_rating")
    val rating: Int,
    @ColumnInfo(name = "show_comment_count")
    val commentCount: Int,
    @ColumnInfo(name = "show_updated_at_date")
    val updatedAt: String,
    @ColumnInfo(name = "show_aired_episodes")
    val airedEpisodes: Int
)