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

package com.sunrisekcdeveloper.showtracker.features.detail.data.network

import com.sunrisekcdeveloper.showtracker.features.detail.data.model.EnvelopeMovieReleaseDates
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.EnvelopeShowCertification
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseMovieDetail
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseShowDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceDetail : ServiceDetailContract {
    @GET("movie/{id}")
    override suspend fun movieDetails(
        @Path("id") id: String,
        @Query("api_key") apiKey: String
    ): Response<ResponseMovieDetail>

    @GET("movie/{id}/release_dates")
    override suspend fun movieCertifications(
        @Path("id") id: String,
        @Query("api_key") apiKey: String
    ): Response<EnvelopeMovieReleaseDates>

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