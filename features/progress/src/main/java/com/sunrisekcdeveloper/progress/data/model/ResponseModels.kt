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

package com.sunrisekcdeveloper.progress.data.model

import com.google.gson.annotations.SerializedName

// todo break up into separate files
data class ResponseSeasonDetailWithEpisodes(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("season_number") val number: Int,
    @SerializedName("air_date") val dateAired: String?,
    @SerializedName("episodes") val episodes: List<ResponseEpisode>
) {
    companion object {
        fun create(amount: Int): List<ResponseSeasonDetailWithEpisodes> {
            val list = mutableListOf<ResponseSeasonDetailWithEpisodes>()
            repeat(amount) {
                list.add(
                    ResponseSeasonDetailWithEpisodes(
                        id = it,
                        name = "name$it",
                        overview = "overview$it",
                        posterPath = "posterPath$it",
                        number = 1,
                        dateAired = "dateAired$it",
                        episodes = ResponseEpisode.create(20)
                    )
                )
            }
            return list.toList()
        }
        fun single() = create(1)[0]
    }
}

// todo roughly made
data class ResponseEpisode(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("episode_number") val number: Int,
    @SerializedName("air_date") val dateAired: String?,
    @SerializedName("season_number") val seasonNumber: Int,
    @SerializedName("still_path") val stillPath: String?,
    @SerializedName("vote_average") val rating: Float,
    @SerializedName("vote_count") val voteCount: Int
) {
    companion object {
        fun create(amount: Int): List<ResponseEpisode> {
            val list = mutableListOf<ResponseEpisode>()
            repeat(amount) {
                list.add(
                    ResponseEpisode(
                        id = it,
                        name = "name$it",
                        overview = "overview$it",
                        number = it,
                        dateAired = "dateAired$it",
                        seasonNumber = 1,
                        stillPath = "stillPath$it",
                        rating = 1f,
                        voteCount = 1
                    )
                )
            }
            return list.toList()
        }
    }
}

// todo this should not be a separate class
data class ResponseShowDetailWithSeasons(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val rating: Float,
    @SerializedName("first_air_date") val firstAirYear: String,
    @SerializedName("number_of_episodes") val episodeCount: Int,
    @SerializedName("number_of_seasons") val seasonCount: Int,
    @SerializedName("popularity") val popularityValue: Float,
    @SerializedName("seasons") val seasons: List<ResponseSeason>
) {
    companion object {
        fun create(amount: Int): List<ResponseShowDetailWithSeasons> {
            val list = mutableListOf<ResponseShowDetailWithSeasons>()
            repeat(amount) {
                list.add(
                    ResponseShowDetailWithSeasons(
                        id = it,
                        name = "name$it",
                        overview = "overview$it",
                        backdropPath = "backdropPath$it",
                        posterPath = "posterPath$it",
                        rating = 1f,
                        firstAirYear = "firstAirYear$it",
                        episodeCount = 1,
                        seasonCount = 1,
                        popularityValue = 1f,
                        seasons = ResponseSeason.create(20)
                    )
                )
            }
            return list.toList()
        }

        fun single() = create(1)[0]
    }
}

data class ResponseSeason(
    @SerializedName("id") val id: Int,
    @SerializedName("season_number") val number: Int,
    @SerializedName("name") val name: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("episode_count") val episodeCount: Int,
    @SerializedName("air_date") val dateAired: String
) {
    companion object {
        fun create(amount: Int): List<ResponseSeason> {
            val list = mutableListOf<ResponseSeason>()
            repeat(amount) {
                list.add(
                    ResponseSeason(
                        id = it,
                        number = it,
                        name = "name$it",
                        overview = "overview$it",
                        posterPath = "posterPath$it",
                        episodeCount = 1,
                        dateAired = "dateAired$it"
                    )
                )
            }
            return list.toList()
        }
    }
}