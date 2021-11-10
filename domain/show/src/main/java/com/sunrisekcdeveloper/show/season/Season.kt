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

package com.sunrisekcdeveloper.show.season

data class Season(
    val identification: Identification,
    val images: ImageUrl,
    val stats: Stats,
    val meta: Meta
)
data class Identification(
    val showId: String,
    val id: String,
    val name: String,
    val overview: String
)
data class ImageUrl(
    val posterPath: String
)
data class Stats(
    val number: Int,
    val airDate: String,
    val episodeTotal: Int
)
data class Meta(
    val lastUpdated: Long = System.currentTimeMillis()
)
