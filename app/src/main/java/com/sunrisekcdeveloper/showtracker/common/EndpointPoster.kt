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

package com.sunrisekcdeveloper.showtracker.common

// See more here https://www.journaldev.com/18688/kotlin-enum-class
enum class EndpointPoster(private val endpoint: String) {

    // values based on TMDB available poster sizes
    Tiny("/w92"),
    Small("/w154"),
    Medium("/w185"),
    Standard("/w342"),
    Big("/w500"),
    Large("/w780"),
    Original("/original");

    fun urlWithResource(posterPath: String): String {
        return baseUrl + endpoint + posterPath
    }

    private companion object {
        private const val baseUrl = "https://image.tmdb.org/t/p"
    }
}