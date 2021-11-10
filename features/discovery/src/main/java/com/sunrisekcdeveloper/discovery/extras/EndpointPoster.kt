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

package com.sunrisekcdeveloper.discovery.extras

class EndpointPosterTiny(private val posterPath: String) : EndpointImageContract {
    override fun url(): String {
        return "$baseUrl/w92$posterPath"
    }
}
class EndpointPosterSmall(private val posterPath: String) : EndpointImageContract {
    override fun url(): String {
        return "$baseUrl/w154$posterPath"
    }
}
class EndpointPosterMedium(private val posterPath: String) : EndpointImageContract {
    override fun url(): String {
        return "$baseUrl/w185$posterPath"
    }
}
class EndpointPosterStandard(private val posterPath: String) : EndpointImageContract {
    override fun url(): String {
        return "$baseUrl/w342$posterPath"
    }
}
class EndpointPosterBig(private val posterPath: String) : EndpointImageContract {
    override fun url(): String {
        return "$baseUrl/w500$posterPath"
    }
}
class EndpointPosterLarge(private val posterPath: String) : EndpointImageContract {
    override fun url(): String {
        return "$baseUrl/w780$posterPath"
    }
}
class EndpointPosterOriginal(private val posterPath: String) : EndpointImageContract {
    override fun url(): String {
        return "$baseUrl/original$posterPath"
    }
}

interface EndpointImageContract {
    val baseUrl: String
        get() = "https://images.tmdb.org/t/p"
    fun url(): String
}