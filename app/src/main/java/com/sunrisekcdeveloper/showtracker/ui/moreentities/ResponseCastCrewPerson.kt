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
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseIdentifiers
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseShow

// ========================================================================

data class ResponseCastCrewPerson(
    @Json(name = "cast") val cast: List<ResponseCastAndPerson>,
    @Json(name = "crew") val crew: ResponseCrewPerson
)
data class ResponseCastCrewMovie(
    @Json(name = "cast") val cast: List<ResponseCastAndMovie>?, // person can be in either role or both
    @Json(name = "crew") val crew: ResponseCrewMovie?
)
data class ResponseCastCrewShow(
    @Json(name = "cast") val cast: List<ResponseCastAndShow>,
    @Json(name = "crew") val crew: ResponseCrewShow
)

// ========================================================================

data class ResponseCastAndPerson(
    @Json(name = "characters") val characters: List<String>,
    @Json(name = "person") val person: ResponsePerson
)
data class ResponseCastAndMovie(
    @Json(name = "characters") val characters: List<String>,
    @Json(name = "movie") val movie: ResponseMovie
)
data class ResponseCastAndShow(
    @Json(name = "characters") val characters: List<String>,
    @Json(name = "episode_count") val episodeAmount: Int,
    @Json(name = "series_regular") val seriesRegular: Boolean,
    @Json(name = "show") val show: ResponseShow
)

// ========================================================================

data class ResponsePerson(
    @Json(name = "name") val name: String,
    @Json(name = "ids") val identifiers: ResponseIdentifiers
)

// ========================================================================

data class ResponseCrewPerson(
    @Json(name = "production") val production: List<ResponseCrewSectionPerson>?,
    @Json(name = "art") val art: List<ResponseCrewSectionPerson>?,
    @Json(name = "crew") val crew: List<ResponseCrewSectionPerson>?,
    @Json(name = "costume & makeup") val costumeMakeUp: List<ResponseCrewSectionPerson>?,
    @Json(name = "directing") val directing: List<ResponseCrewSectionPerson>?,
    @Json(name = "writing") val writing: List<ResponseCrewSectionPerson>?,
    @Json(name = "sound") val sound: List<ResponseCrewSectionPerson>?,
    @Json(name = "camera") val camera: List<ResponseCrewSectionPerson>?,
    @Json(name = "visual effects") val visualEffects: List<ResponseCrewSectionPerson>?,
    @Json(name = "lighting") val lighting: List<ResponseCrewSectionPerson>?,
    @Json(name = "editing") val editing: List<ResponseCrewSectionPerson>?
)
data class ResponseCrewMovie(
    @Json(name = "production") val production: List<ResponseCrewSectionMovie>?,
    @Json(name = "art") val art: List<ResponseCrewSectionMovie>?,
    @Json(name = "crew") val crew: List<ResponseCrewSectionMovie>?,
    @Json(name = "costume & makeup") val costumeMakeUp: List<ResponseCrewSectionMovie>?,
    @Json(name = "directing") val directing: List<ResponseCrewSectionMovie>?,
    @Json(name = "writing") val writing: List<ResponseCrewSectionMovie>?,
    @Json(name = "sound") val sound: List<ResponseCrewSectionMovie>?,
    @Json(name = "camera") val camera: List<ResponseCrewSectionMovie>?,
    @Json(name = "visual effects") val visualEffects: List<ResponseCrewSectionMovie>?,
    @Json(name = "lighting") val lighting: List<ResponseCrewSectionMovie>?,
    @Json(name = "editing") val editing: List<ResponseCrewSectionMovie>?
)
data class ResponseCrewShow(
    @Json(name = "production") val production: List<ResponseCrewSectionShow>?,
    @Json(name = "art") val art: List<ResponseCrewSectionShow>?,
    @Json(name = "crew") val crew: List<ResponseCrewSectionShow>?,
    @Json(name = "costume & makeup") val costumeMakeUp: List<ResponseCrewSectionShow>?,
    @Json(name = "directing") val directing: List<ResponseCrewSectionShow>?,
    @Json(name = "writing") val writing: List<ResponseCrewSectionShow>?,
    @Json(name = "sound") val sound: List<ResponseCrewSectionShow>?,
    @Json(name = "camera") val camera: List<ResponseCrewSectionShow>?,
    @Json(name = "visual effects") val visualEffects: List<ResponseCrewSectionShow>?,
    @Json(name = "lighting") val lighting: List<ResponseCrewSectionShow>?,
    @Json(name = "editing") val editing: List<ResponseCrewSectionShow>?
)

// ========================================================================

data class ResponseCrewSectionPerson(
    @Json(name = "jobs") val jobs: List<String>,
    @Json(name = "person") val person: ResponsePerson
)
data class ResponseCrewSectionMovie(
    @Json(name = "jobs") val jobs: List<String>,
    @Json(name = "movie") val movie: ResponseMovie
)
data class ResponseCrewSectionShow(
    @Json(name = "jobs") val jobs: List<String>,
    @Json(name = "show") val show: ResponseShow
)

// ========================================================================