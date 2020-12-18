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

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sunrisekcdeveloper.showtracker.data.local.model.categories.PopularListEntity
import com.sunrisekcdeveloper.showtracker.data.local.model.categories.RecommendedListEntity
import com.sunrisekcdeveloper.showtracker.data.local.model.categories.TrendingListEntity
import com.sunrisekcdeveloper.showtracker.data.local.model.core.MovieEntity

@Database(
    entities = [
        MovieEntity::class, TrendingListEntity::class,
        PopularListEntity::class, RecommendedListEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class TrackerDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

}