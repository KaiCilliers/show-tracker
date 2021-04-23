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

package com.sunrisekcdeveloper.showtracker.features.search.domain.model

import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType

data class UIModelSearch(
    val id: String,
    val title: String,
    val posterPath: String,
    val mediaType: MediaType,
    val rating: Float,
    val popularity: Float,
    val ratingVotes: Int
) {
    companion object {
        fun create(amount: Int): List<UIModelSearch> {
            val list = mutableListOf<UIModelSearch>()
            repeat(amount) {
                list.add(
                    UIModelSearch(
                        id = "id$it",
                        title = "title$it",
                        posterPath = "posterPath$it",
                        mediaType = MediaType.movie(),
                        rating = 1f,
                        popularity = 1f,
                        ratingVotes = 1
                    )
                )
            }
            return list.toList()
        }
    }
}

