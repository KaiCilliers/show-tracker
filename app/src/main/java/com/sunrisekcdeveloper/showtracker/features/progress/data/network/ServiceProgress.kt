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

package com.sunrisekcdeveloper.showtracker.features.progress.data.network

import com.sunrisekcdeveloper.showtracker.common.NetworkResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceProgress : ServiceProgressContract {
    @GET("tv/{id}")
    override suspend fun showWithSeasons(
        @Path("id") id: String,
        @Query("api_key") apiKey: String
    ): Response<ResponseShowDetailWithSeasons>

    @GET("tv/{showId}/season/{seasonNumber}")
    override suspend fun seasonsWithEpisodes(
        @Path("showId") showId: String,
        @Path("seasonNumber") seasonNumber: Int,
        @Query("api_key") apiKey: String
    ): Response<ResponseSeasonDetailWithEpisodes>
}