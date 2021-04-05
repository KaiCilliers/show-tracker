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

package com.sunrisekcdeveloper.showtracker.common.util

import com.sunrisekcdeveloper.showtracker.BuildConfig

class EndPointBackDropOriginal(private val backdropPath: String) : EndpointImageContract {
    override fun url(): String {
        return "$baseUrl/original$backdropPath"
    }
}

class EndpointBackdropLarge(private val backdropPath: String) : EndpointImageContract {
    override fun url(): String {
        return "$baseUrl/w1280$backdropPath"
    }
}

class EndpointBackdropStandard(private val backdropPath: String) : EndpointImageContract {
    override fun url(): String {
        return "$baseUrl/w780$backdropPath"
    }
}

class EndpointBackdropSmall(private val backdropPath: String) : EndpointImageContract {
    override fun url(): String {
        return "$baseUrl/w300$backdropPath"
    }
}

interface EndpointImageContract {
    val baseUrl: String
        get() = BuildConfig.TMDB_POSTER_BASE_URL
    fun url(): String
}