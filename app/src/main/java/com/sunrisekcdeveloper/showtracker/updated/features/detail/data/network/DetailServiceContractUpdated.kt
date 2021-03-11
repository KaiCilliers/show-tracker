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

package com.sunrisekcdeveloper.showtracker.updated.features.detail.data.network

import com.google.gson.annotations.SerializedName
import com.sunrisekcdeveloper.showtracker.BuildConfig
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.network.model.EnvelopePaginatedMovieUpdated
import retrofit2.Response

interface DetailServiceContractUpdated {
    suspend fun movieDetail(
        id: String,
        apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<ResponseMovieDetail>

    suspend fun movieCertification(
        id: String,
        apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<EnvelopeMovieReleaseDates>

    suspend fun showDetail(
        id: String,
        apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<ResponseShowDetail>

    suspend fun showCertification(
        id: String,
        apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<EnvelopeShowCertification>

}

data class ResponseShowDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("first_air_date") val firstAirYear: String,
    @SerializedName("number_of_seasons") val seasonTotal: Int
)

data class EnvelopeShowCertification(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<ResponseShowCertification>
)

data class ResponseShowCertification(
    @SerializedName("iso_3166_1") val iso: String,
    @SerializedName("rating") val certification: String
)

data class ResponseMovieDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("runtime") val runtime: Int?
)

data class EnvelopeMovieReleaseDates(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<ResponseMovieReleaseDates>
)

data class ResponseMovieReleaseDates(
    @SerializedName("iso_3166_1") val iso: String,
    @SerializedName("release_dates") val releaseDates: List<ResponseCertificationAndReleaseDate>
)

// todo get the rating based on the type (it seems type 2)
data class ResponseCertificationAndReleaseDate(
    @SerializedName("certification") val certification: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("type") val releaseType: Int, // todo limited values - sealed class
)