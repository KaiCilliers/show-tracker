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

package com.sunrisekcdeveloper.showtracker.commons.data.local.model.categories

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sunrisekcdeveloper.showtracker.commons.data.network.model.envelopes.EnvelopeViewStats

@Entity(tableName = "tbl_most_played")
data class MostPlayedListEntity(
    @PrimaryKey
    @ColumnInfo(name = "fk_played_media_slug")
    val mediaSlug: String,
    @ColumnInfo(name = "played_watcher_count")
    val watchers: Int,
    @ColumnInfo(name = "played_play_count")
    val plays: Int,
    @ColumnInfo(name = "played_collected_count")
    val collectedCount: Int
) {
    companion object {
        fun from(item: EnvelopeViewStats): MostPlayedListEntity {
            return MostPlayedListEntity(
                mediaSlug = item.movie!!.identifiers.slug,
                watchers = item.watchers,
                plays = item.playCount,
                collectedCount = item.collectedCount
            )
        }
    }
}