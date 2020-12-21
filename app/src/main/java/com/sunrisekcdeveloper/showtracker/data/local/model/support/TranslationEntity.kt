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

package com.sunrisekcdeveloper.showtracker.data.local.model.support

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_translation")
data class TranslationEntity(
       @PrimaryKey(autoGenerate = true)
       @ColumnInfo(name = "translation_id")
       val id: Long = 0L,
       @ColumnInfo(name = "fk_translation_media_slug")
       val mediaSlug: String, // FK
       @ColumnInfo(name = "fk_translation_language_code")
       val language: String, // FK LanguageEntity
       @ColumnInfo(name = "translation_title")
       val title: String,
       @ColumnInfo(name = "translation_overview")
       val overview: String,
       @ColumnInfo(name = "translation_tagline")
       val tagline: String
)