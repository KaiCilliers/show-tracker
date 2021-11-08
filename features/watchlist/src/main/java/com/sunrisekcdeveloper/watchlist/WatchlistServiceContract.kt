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

package com.sunrisekcdeveloper.watchlist

import com.sunrisekcdeveloper.watchlist.extras.EnvelopeShowCertification
import com.sunrisekcdeveloper.watchlist.extras.ResponseEpisode
import com.sunrisekcdeveloper.watchlist.extras.ResponseSeason
import com.sunrisekcdeveloper.watchlist.extras.ResponseShowDetail
import retrofit2.Response

interface WatchlistServiceContract {
    suspend fun episodeDetails(
        showId: String, season: Int, episode: Int,
        apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<ResponseEpisode>

    suspend fun season(
        showId: String,
        season: Int,
        apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<ResponseSeason>

    suspend fun showDetails(
        id: String,
        apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<ResponseShowDetail>

    suspend fun showCertifications(
        id: String,
        apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<EnvelopeShowCertification>
}