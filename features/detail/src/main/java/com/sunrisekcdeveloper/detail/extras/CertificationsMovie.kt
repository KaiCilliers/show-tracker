/*
 * Copyright © 2021. The Android Open Source Project
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

package com.sunrisekcdeveloper.detail.extras

class CertificationsMovie(private val data: List<ResponseMovieReleaseDates>, private val type: ReleaseDateType) :
    CertificationsContract {
    override fun from(iso: String): String {
        if (data.isEmpty()) return noneAvailable
        val releases = data.find { it.iso == iso } ?: data[0]
        if (releases.releaseDates.isEmpty()) return noneAvailable
        val certification = releases.releaseDates.find { it.releaseType == type() }?.certification
            ?: releases.releaseDates[0].certification
        return if (certification != "") certification
        else noneAvailable
    }
    private fun type() = when(type) {
        ReleaseDateType.Premiere -> { 1 }
        ReleaseDateType.TheatricalLimited -> { 2 }
        ReleaseDateType.Theatrical -> { 3 }
        ReleaseDateType.Digital -> { 4 }
        ReleaseDateType.Physical -> { 5 }
        ReleaseDateType.TV -> { 6 }
    }
}