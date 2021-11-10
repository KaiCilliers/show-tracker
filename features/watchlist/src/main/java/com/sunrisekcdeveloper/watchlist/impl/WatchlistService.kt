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

package com.sunrisekcdeveloper.watchlist.impl

import com.sunrisekcdeveloper.watchlist.WatchlistServiceContract
import com.sunrisekcdeveloper.watchlist.extras.EnvelopeShowCertification
import com.sunrisekcdeveloper.watchlist.extras.ResponseEpisode
import com.sunrisekcdeveloper.watchlist.extras.ResponseSeason
import com.sunrisekcdeveloper.watchlist.extras.ResponseShowDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WatchlistService : WatchlistServiceContract {
    @GET("/tv/{tv_id}/season/{season_number}/episode/{episode_number}")
    override suspend fun episodeDetails(
        @Path("tv_id") showId: String,
        @Path("season_number") season: Int,
        @Path("episode_number") episode: Int,
        @Query("api_key") apiKey: String
    ): Response<ResponseEpisode>

    @GET("/tv/{tv_id}/season/{season_number}")
    override suspend fun season(
        @Path("tv_id") showId: String,
        @Path("season_number") season: Int,
        @Query("api_key") apiKey: String
    ): Response<ResponseSeason> {
        TODO("Not yet implemented")
    }

    @GET("tv/{id}")
    override suspend fun showDetails(
        @Path("id") id: String,
        @Query("api_key") apiKey: String
    ): Response<ResponseShowDetail>

    @GET("tv/{id}/content_ratings")
    override suspend fun showCertifications(
        @Path("id" )id: String,
        @Query("api_key") apiKey: String
    ): Response<EnvelopeShowCertification>
}