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

package com.sunrisekcdeveloper.cache

import androidx.room.Entity

@Entity(
    tableName = "tbl_paging_discovery",
    primaryKeys = ["id", "listType"]
)
data class UIModelDiscovery(
    val id: String,
    val mediaType: MediaType,
    val mediaTitle: String,
    // Added list type to place all discovery lists in a
    // single table and have multiple DAO functions which
    // can use this field to determine what list the item
    // belongs to. Marked as PK due to the possibility of
    // the same media being present in multiple lists
    val listType: ListType,
    val posterPath: String
) {
    companion object {
        fun create(amount: Int): List<UIModelDiscovery> {
            val list = mutableListOf<UIModelDiscovery>()
            repeat(amount) {
                list.add(
                    UIModelDiscovery(
                        id = "id$it",
                        mediaType = MediaType.movie(),
                        mediaTitle = "title$it",
                        listType = ListType.noList(),
                        posterPath = "posterPath$it"
                    )
                )
            }
            return list.toList()
        }
    }
}