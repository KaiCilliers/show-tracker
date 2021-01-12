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

package com.sunrisekcdeveloper.showtracker.models.local.support

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_rating")
data class RatingEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rating_id")
    val id: Long = 0L,
    @ColumnInfo(name = "rating_value")
    val value: Float,
    @ColumnInfo(name = "rating_vote_count")
    val votes: Int,
    @ColumnInfo(name = "rating_one_count")
    val ones: Int,
    @ColumnInfo(name = "rating_two_count")
    val twos: Int,
    @ColumnInfo(name = "rating_three_count")
    val threes: Int,
    @ColumnInfo(name = "rating_four_count")
    val fours: Int,
    @ColumnInfo(name = "rating_five_count")
    val fives: Int,
    @ColumnInfo(name = "rating_six_count")
    val sixes: Int,
    @ColumnInfo(name = "rating_seven_count")
    val sevens: Int,
    @ColumnInfo(name = "rating_eight_count")
    val eights: Int,
    @ColumnInfo(name = "rating_nine_count")
    val nines: Int,
    @ColumnInfo(name = "rating_ten_count")
    val tens: Int
)
