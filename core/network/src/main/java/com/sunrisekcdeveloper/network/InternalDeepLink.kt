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

package com.sunrisekcdeveloper.network

// TODO [E07-002] [Move-navigation-logic-to-own-module] consider placing navigation related items into a separate module if navigation is still done this way
object InternalDeepLink {

    const val DOMAIN = "showtracker://"

    fun moduleDetailMovie(id: String, movieTitle: String, posterPath: String): String {
        return "${DOMAIN}detail/movie_detail?id=${id}?movieTitle=${movieTitle}?posterPath=${posterPath}"
    }

    fun moduleDetailShow(id: String, showTitle: String, posterPath: String): String {
        return "${DOMAIN}detail/show_detail?id=${id}?showTitle=${showTitle}?posterPath=${posterPath}"
    }

    fun moduleWatchlist(showId: String = "none"): String {
        return "${DOMAIN}watchlist?showId=$showId"
    }

    fun moduleProgress(showId: String, showTitle: String): String {
        return "${DOMAIN}progress?id=$showId?title=$showTitle"
    }

    fun moduleSearch(): String {
        return "${DOMAIN}search"
    }
}