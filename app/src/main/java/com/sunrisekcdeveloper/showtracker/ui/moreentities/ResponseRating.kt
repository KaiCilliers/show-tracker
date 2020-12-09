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

data class ResponseRating(
    @Json(name = "rating") private val rating: Float,
    @Json(name = "votes") private val votes: Int,
    @Json(name = "distribution") private val distribution: ResponseDistributionNumbers
)

data class ResponseDistributionNumbers(
    @Json(name = "1") private val one: String,
    @Json(name = "2") private val two: String,
    @Json(name = "3") private val three: String,
    @Json(name = "4") private val four: String,
    @Json(name = "5") private val five: String,
    @Json(name = "6") private val six: String,
    @Json(name = "7") private val seven: String,
    @Json(name = "8") private val eight: String,
    @Json(name = "9") private val nine: String,
    @Json(name = "10") private val ten: String
)