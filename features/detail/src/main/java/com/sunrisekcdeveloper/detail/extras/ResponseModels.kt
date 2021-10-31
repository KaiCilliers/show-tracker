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

package com.sunrisekcdeveloper.detail.extras

import com.google.gson.annotations.SerializedName

data class ResponseShowDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val rating: Float,
    @SerializedName("first_air_date") val firstAirYear: String,
    @SerializedName("number_of_episodes") val episodeCount: Int,
    @SerializedName("number_of_seasons") val seasonCount: Int,
    @SerializedName("popularity") val popularityValue: Float
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
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("popularity") val popularityValue: Float,
    @SerializedName("vote_average") val rating: Float,
)

data class EnvelopeMovieReleaseDates(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<ResponseMovieReleaseDates>
)

data class ResponseMovieReleaseDates(
    @SerializedName("iso_3166_1") val iso: String,
    @SerializedName("release_dates") val releaseDates: List<ResponseCertificationAndReleaseDate>
)

data class ResponseCertificationAndReleaseDate(
    @SerializedName("certification") val certification: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("type") val releaseType: Int
)