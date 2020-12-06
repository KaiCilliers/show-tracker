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

package com.sunrisekcdeveloper.showtracker.entities.network.base

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseIdentifiers

/**
 * Response Show represents a network object containing information on a single show
 *
 * @property title of the show
 * @property year of the show's release date
 * @property identifier is a collection of identifiers of the season that can be used with other
 * APIs
 */
data class ResponseShow(
    @Json(name = "title") val title: String,
    @Json(name = "year") val year: Int,
    @Json(name = "ids") val identifier: ResponseIdentifiers
)
