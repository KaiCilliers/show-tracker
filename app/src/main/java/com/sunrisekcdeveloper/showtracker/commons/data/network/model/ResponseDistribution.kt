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

package com.sunrisekcdeveloper.showtracker.commons.data.network.model

import com.squareup.moshi.Json

data class ResponseDistribution(
    @Json(name = "1") val one: String,
    @Json(name = "2") val two: String,
    @Json(name = "3") val three: String,
    @Json(name = "4") val four: String,
    @Json(name = "5") val five: String,
    @Json(name = "6") val six: String,
    @Json(name = "7") val seven: String,
    @Json(name = "8") val eight: String,
    @Json(name = "9") val nine: String,
    @Json(name = "10") val ten: String
)