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

data class ResponseCrew(
    @Json(name = "production") val production: List<ResponseCrewSection>?,
    @Json(name = "art") val art: List<ResponseCrewSection>?,
    @Json(name = "crew") val crew: List<ResponseCrewSection>?,
    @Json(name = "costume & makeup") val costumeMakeUp: List<ResponseCrewSection>?,
    @Json(name = "directing") val directing: List<ResponseCrewSection>?,
    @Json(name = "writing") val writing: List<ResponseCrewSection>?,
    @Json(name = "sound") val sound: List<ResponseCrewSection>?,
    @Json(name = "camera") val camera: List<ResponseCrewSection>?,
    @Json(name = "visual effects") val visualEffects: List<ResponseCrewSection>?,
    @Json(name = "lighting") val lighting: List<ResponseCrewSection>?,
    @Json(name = "editing") val editing: List<ResponseCrewSection>?
)