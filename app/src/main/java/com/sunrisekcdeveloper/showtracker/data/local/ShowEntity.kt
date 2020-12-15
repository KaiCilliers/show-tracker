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

package com.sunrisekcdeveloper.showtracker.data.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shows_table")
data class ShowEntity(
    @PrimaryKey
    @ColumnInfo(name = "show_id")
    val id: String,
    val title: String,
    val overview: String,
    @ColumnInfo(name = "first_aired")
    val firstAired: String,
    @Embedded
    @ColumnInfo(name = "airs")
    val airtime: AirTime,
    val runtime: Int,
    val certification: String,
    val network: String,
    val country: String,
    @ColumnInfo(name = "trailer_url")
    val trailerUrl: String,
    @ColumnInfo(name = "homepage_url")
    val homepageUrl: String,
    val status: String,
    val rating: Float,
    val votes: Int,
    @ColumnInfo(name = "comment_count")
    val commentCount: Int,
    @ColumnInfo(name = "updated_at")
    val updatedAt: String,
    val language: String,
    val translations: ArrayList<String>,
    val genres: ArrayList<String>,
    @ColumnInfo(name = "aired_episodes")
    val airedEpisodes: Int
)
data class AirTime(
    val day: String,
    val time: String,
    @ColumnInfo(name = "time_zone")
    val timezone: String
)