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

/**
 * Response Identifiers represents a network object containing IDs that can be used to reference
 * the same object / entity by different APIs. Not all objects / entities have all the ID types,
 * thus some are nullable
 *
 * @property trakt represents the ID used to reference the object / entity using the Trakt API
 * @property slug not 100% clear what slug is used for
 * @property imdb represents the ID used to reference the object / entity using the IMDB API
 * @property tmdb represents the ID used to reference the object / entity using the TMDB API
 * @property tvdb represents the ID used to reference the object / entity using the TVDB API
 * @constructor Create empty Response identifiers
 */
class ResponseIdentifiers(
    @Json(name = "trakt") val trakt: Int,
    @Json(name = "slug") val slug: String,
    @Json(name = "imdb") val imdb: String?,
    @Json(name = "tmdb") val tmdb: Int,
    @Json(name = "tvdb") val tvdb: Int?,
)

/**
 * Response identifiers no slug predicting using slugs a lot in project and I want to avoid
 * null checks - only seasons and episodes have no slugs
 *
 * @property trakt
 * @property imdb
 * @property tmdb
 * @property tvdb
 */
class ResponseIdentifiersNoSlug(
    @Json(name = "trakt") val trakt: Int,
    @Json(name = "imdb") val imdb: String?,
    @Json(name = "tmdb") val tmdb: Int?,
    @Json(name = "tvdb") val tvdb: Int?,
)