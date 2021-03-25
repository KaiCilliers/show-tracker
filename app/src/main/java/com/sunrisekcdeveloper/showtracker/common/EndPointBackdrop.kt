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

enum class EndPointBackdrop(private val endpoint: String) {
    Small("/w300"),
    Standard("/w780"),
    Large("/w1280"),
    Original("/original");

    fun urlFromResource(backdropPath: String): String {
        return baseUrl + endpoint + backdropPath
    }

    private companion object {
        // todo save this url somewhere like with api keys
        //  access via BuildConfig
        private const val baseUrl = "https://images.tmdb.org/t/p"
    }
}
