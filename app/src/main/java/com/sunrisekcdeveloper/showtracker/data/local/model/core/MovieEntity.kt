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
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_table")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val id: String,
    val title: String,
    val year: Int,
    val tagline: String,
    val overview: String,
    val released: String,
    val runtime: Int,
    val country: String,
    val trailerUrl: String,
    val homepageUrl: String,
    val status: String,
    val rating: Float,
    val votes: Int,
    val commentCount: Int,
    val updatedAt: String,
    val language: String,
    val translationsList: List<String>,
    val genres: List<String>,
    val certification: String
)

