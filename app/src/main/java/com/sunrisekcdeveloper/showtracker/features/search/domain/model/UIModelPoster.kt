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

import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ListType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType

data class UIModelPoster(
    val id: String,
    val title: String,
    val posterPath: String,
    val backdropPath: String,
    val type: MediaType,
    val listType: ListType
) {
    companion object {
        fun create(amount: Int): List<UIModelPoster> {
            val list = mutableListOf<UIModelPoster>()
            repeat(amount) {
                list.add(
                    UIModelPoster(
                        id = "id$it",
                        title = "title$it",
                        posterPath = "posterPath$it",
                        backdropPath = "backdropPath$it",
                        type = MediaType.movie(),
                        listType = ListType.noList()
                    )
                )
            }
            return list.toList()
        }
    }
}