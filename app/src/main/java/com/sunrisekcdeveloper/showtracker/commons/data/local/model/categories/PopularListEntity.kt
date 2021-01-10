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
import com.sunrisekcdeveloper.showtracker.commons.data.network.model.base.ResponseMovie

@Entity(tableName = "tbl_popular")
data class PopularListEntity(
    @PrimaryKey
    @ColumnInfo(name = "fk_popular_media_slug")
    val mediaSlug: String
) {
    companion object {
        fun from(movie: ResponseMovie): PopularListEntity {
            return PopularListEntity(mediaSlug = movie.identifiers.slug)
        }
    }
}