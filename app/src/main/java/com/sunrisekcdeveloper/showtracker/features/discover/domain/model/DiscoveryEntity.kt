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

package com.sunrisekcdeveloper.showtracker.features.discover.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.WatchListType

@Entity(tableName = "tbl_discovery_popular")
data class DiscoveryPopularEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val overview: String,
    @ColumnInfo(name = "poster_path")
    val posterPath: String,
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String,
    val rating: Float,
    @ColumnInfo(name = "release_date")
    val releaseDate: String,
    @ColumnInfo(name = "media_type")
    val mediaType: MediaType
)
@Entity(tableName = "tbl_discovery_top_rated")
data class DiscoveryTopRatedEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val overview: String,
    @ColumnInfo(name = "poster_path")
    val posterPath: String,
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String,
    val rating: Float,
    @ColumnInfo(name = "release_date")
    val releaseDate: String,
    @ColumnInfo(name = "media_type")
    val mediaType: MediaType
)
@Entity(tableName = "tbl_discovery_upcoming")
data class DiscoveryUpcomingEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val overview: String,
    @ColumnInfo(name = "poster_path")
    val posterPath: String,
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String,
    val rating: Float,
    @ColumnInfo(name = "release_date")
    val releaseDate: String,
    @ColumnInfo(name = "media_type")
    val mediaType: MediaType
)
