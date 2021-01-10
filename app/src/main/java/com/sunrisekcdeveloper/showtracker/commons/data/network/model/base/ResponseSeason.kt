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

package com.sunrisekcdeveloper.showtracker.commons.data.network.model.base

import com.squareup.moshi.Json

/**
 * Response Season represents a network object containing information on a single season of a show
 *
 * @property number is the season number of a show
 * @property identifiers is a collection of identifiers of the season that can be used with other
 * APIs
 */
data class ResponseSeason(
    @Json(name = "number") val number: Int,
    @Json(name = "ids") val identifiers: ResponseIdentifiersNoSlug
)