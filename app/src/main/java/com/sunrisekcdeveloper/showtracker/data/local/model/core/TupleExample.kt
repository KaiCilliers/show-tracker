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
import androidx.room.Query

// Example of extracting only select fields from table
data class TupleExample(
    @ColumnInfo(name = "title") val movie_title: String,
    @ColumnInfo(name = "year") val movie_year: Int,
    @ColumnInfo(name = "tagline") val movie_tagline: String,
    @ColumnInfo(name = "overview")val movie_overview: String
)

// Example on how to fetch the tuple data
//@Query("""
//    SELECT title, year, tagline, overview
//    FROM movies_table
//""")
//fun getSpecificInformationForUi(): List<TupleExample>

// Example with multiple tables
//interface UserBookDao {
//    @Query(
//        "SELECT user.name AS userName, book.name AS bookName " +
//                "FROM user, book " +
//                "WHERE user.id = book.user_id"
//    )
//    fun loadUserAndBookNames(): LiveData<List<UserBook>>
//
//    // You can also define this class in a separate file.
//    data class UserBook(val userName: String?, val bookName: String?)
//}
