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

package com.sunrisekcdeveloper.showtracker.features.discover.domain.model

import com.google.gson.annotations.SerializedName

// TODO all fields can be null - thus provide default values
// todo rename
//data class ResponseMovieTMDB(
//    @SerializedName("id") val id: Long,
//    @SerializedName("title") val title: String,
//    @SerializedName("overview") val overview: String,
//    @SerializedName("poster_path") val posterPath: String,
//    @SerializedName("backdrop_path") val backdropPath: String,
//    @SerializedName("vote_average") val rating: Float,
//    @SerializedName("release_date") val releaseDate: String
//)
//data class ResponseShowTMDB(
//    @SerializedName("id") val id: Long?,
//    @SerializedName("name") val name: String?,
//    @SerializedName("overview") val overview: String?,
//    @SerializedName("poster_path") val posterPath: String?,
//    @SerializedName("backdrop_path") val backdropPath: String?,
//    @SerializedName("vote_average") val rating: Float?,
//    @SerializedName("first_air_date") val firstAirDate: String?
//)

sealed class ResponseStandardMedia {
    data class ResponseMovie(
        @SerializedName("id") val id: Long,
        @SerializedName("title") val title: String,
        @SerializedName("overview") val overview: String,
        @SerializedName("poster_path") val posterPath: String,
        @SerializedName("backdrop_path") val backdropPath: String,
        @SerializedName("vote_average") val rating: Float,
        @SerializedName("release_date") val releaseDate: String
    ) : ResponseStandardMedia()

    // todo cross reference TMDB api enpoint documentation of response object possible values
    data class ResponseShow(
        @SerializedName("id") val id: Long?,
        @SerializedName("name") val name: String?,
        @SerializedName("overview") val overview: String?,
        @SerializedName("poster_path") val posterPath: String?,
        @SerializedName("backdrop_path") val backdropPath: String?,
        @SerializedName("vote_average") val rating: Float?,
        @SerializedName("first_air_date") val firstAirDate: String?
    ) : ResponseStandardMedia()
}