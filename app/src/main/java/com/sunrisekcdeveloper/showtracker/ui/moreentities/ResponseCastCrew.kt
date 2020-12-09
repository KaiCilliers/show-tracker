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

data class ResponseCastCrew(
    @Json(name = "cast") private val cast: List<ResponseCast>,
    @Json(name = "crew") private val crew: ResponseCrew
)

data class ResponseCast(
    @Json(name = "characters") val characters: List<String>,
    @Json(name = "person") val person: ResponsePerson
)

data class ResponsePerson(
    @Json(name = "name") private val name: String,
    @Json(name = "ids") private val identifiers: ResponseIdentifiers
)

data class ResponseCrew(
    @Json(name = "production") private val production: List<ResponseCrewSection>?,
    @Json(name = "art") private val art: List<ResponseCrewSection>?,
    @Json(name = "crew") private val crew: List<ResponseCrewSection>?,
    @Json(name = "costume & makeup") private val costumeMakeUp: List<ResponseCrewSection>?,
    @Json(name = "directing") private val directing: List<ResponseCrewSection>?,
    @Json(name = "writing") private val writing: List<ResponseCrewSection>?,
    @Json(name = "sound") private val sound: List<ResponseCrewSection>?,
    @Json(name = "camera") private val camera: List<ResponseCrewSection>?,
    @Json(name = "visual effects") private val visualEffects: List<ResponseCrewSection>?,
    @Json(name = "lighting") private val lighting: List<ResponseCrewSection>?,
    @Json(name = "editing") private val editing: List<ResponseCrewSection>?
)

data class ResponseCrewSection(
    @Json(name = "jobs") private val jobs: List<String>,
    @Json(name = "person") private val person: ResponsePerson
)