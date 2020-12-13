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

package com.sunrisekcdeveloper.showtracker.entities.network

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponsePerson
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseShow

data class ResponseCast(
    @Json(name = "characters") val characters: List<String>?,
    @Json(name = "episode_count") val episodeCount: Int?,
    @Json(name = "series_regular") val seriesRegular: Boolean?,
    @Json(name = "person") val person: ResponsePerson?,
    @Json(name = "show") val show: ResponseShow?,
    @Json(name = "movie") val movie: ResponseMovie?
)