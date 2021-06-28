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

package com.sunrisekcdeveloper.progress.data.network

import com.sunrisekcdeveloper.progress.BuildConfig
import com.sunrisekcdeveloper.progress.data.model.ResponseSeasonDetailWithEpisodes
import com.sunrisekcdeveloper.progress.data.model.ResponseShowDetailWithSeasons
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

interface ServiceProgressContract {
    suspend fun showWithSeasons(
        id: String,
        apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<ResponseShowDetailWithSeasons>

    suspend fun seasonsWithEpisodes(
        showId: String,
        seasonNumber: Int,
        apiKey: String = BuildConfig.TMDB_API_KEY
    ) : Response<ResponseSeasonDetailWithEpisodes>

    class Fake() : ServiceProgressContract {
        var expectException = false
        override suspend fun showWithSeasons(
            id: String,
            apiKey: String
        ): Response<ResponseShowDetailWithSeasons> {
            return if (expectException) Response.error(0, "Fake - Could not fetch Show (id=$id)".toResponseBody())
            else Response.success(ResponseShowDetailWithSeasons.single())
        }

        override suspend fun seasonsWithEpisodes(
            showId: String,
            seasonNumber: Int,
            apiKey: String
        ): Response<ResponseSeasonDetailWithEpisodes> {
            return if (expectException) Response.error(0, "Fake - Could not fetch season (number=$seasonNumber) from Show (id=$showId)".toResponseBody())
            else Response.success(ResponseSeasonDetailWithEpisodes.single())
        }
    }
}