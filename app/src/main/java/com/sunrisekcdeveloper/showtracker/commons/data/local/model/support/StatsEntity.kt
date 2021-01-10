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

package com.sunrisekcdeveloper.showtracker.commons.data.local.model.support

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO default value of -1 instead of making fields nullable?
@Entity(tableName = "tbl_stats")
data class StatsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "stats_id")
    val id: Long = 0L,
    @ColumnInfo(name = "stats_watcher_count")
    val watchers: Int,
    @ColumnInfo(name = "stats_play_count")
    val plays: Int,
    @ColumnInfo(name = "stats_comment_count")
    val comments: Int,
    @ColumnInfo(name = "stats_list_count")
    val lists: Int,
    @ColumnInfo(name = "stats_vote_count")
    val votes: Int,
    @ColumnInfo(name = "stats_recommended_count")
    val recommends: Int
)
