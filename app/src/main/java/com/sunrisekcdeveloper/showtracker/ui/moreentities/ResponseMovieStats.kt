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

package com.sunrisekcdeveloper.showtracker.ui.moreentities

import com.squareup.moshi.Json

data class ResponseMovieStats(
    @Json(name = "watchers") private val watchers: Int,
    @Json(name = "plays") private val plays: Int,
    @Json(name = "collectors") private val collectors: Int,
    @Json(name = "comments") private val comments: Int,
    @Json(name = "lists") private val lists: Int,
    @Json(name = "votes") private val votes: Int,
    @Json(name = "recommended") private val recommended: Int
)