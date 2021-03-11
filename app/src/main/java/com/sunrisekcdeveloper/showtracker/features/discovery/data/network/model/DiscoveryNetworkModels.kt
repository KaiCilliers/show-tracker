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

package com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model

import com.google.gson.annotations.SerializedName

data class EnvelopePaginatedMovieUpdated(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val media: List<ResponseStandardMediaUpdated.ResponseMovieUpdated>,
    @SerializedName("total_pages") val pages: Int
)
data class EnvelopePaginatedShowUpdated(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val media: List<ResponseStandardMediaUpdated.ResponseShowUpdated>,
    @SerializedName("total_pages") val pages: Int
)
sealed class ResponseStandardMediaUpdated {
    data class ResponseMovieUpdated(
        @SerializedName("id") val id: Long,
        @SerializedName("title") val title: String,
        @SerializedName("overview") val overview: String,
        @SerializedName("poster_path") val posterPath: String?,
        @SerializedName("backdrop_path") val backdropPath: String?,
        @SerializedName("vote_average") val rating: Float,
        @SerializedName("release_date") val releaseDate: String
    ) : ResponseStandardMediaUpdated()

    // todo cross reference TMDB api enpoint documentation of response object possible values
    data class ResponseShowUpdated(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String,
        @SerializedName("overview") val overview: String,
        @SerializedName("poster_path") val posterPath: String?,
        @SerializedName("backdrop_path") val backdropPath: String?,
        @SerializedName("vote_average") val rating: Float,
        @SerializedName("first_air_date") val firstAirDate: String
    ) : ResponseStandardMediaUpdated()
}