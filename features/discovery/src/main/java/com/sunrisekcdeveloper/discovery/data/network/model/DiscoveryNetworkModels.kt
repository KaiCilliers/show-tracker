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

package com.sunrisekcdeveloper.discovery.data.network.model

import com.google.gson.annotations.SerializedName

data class EnvelopePaginatedMovies(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val media: List<ResponseStandardMedia.ResponseMovie>,
    @SerializedName("total_pages") val pages: Int
) {
    companion object {
        fun create(amount: Int): List<EnvelopePaginatedMovies> {
            val list = mutableListOf<EnvelopePaginatedMovies>()
            repeat(amount) {
                list.add(
                    EnvelopePaginatedMovies(
                        page = 1,
                        media = ResponseStandardMedia.createMovies(20),
                        pages = 1
                    )
                )
            }
            return list.toList()
        }
        fun single() = create(1)[0]
    }
}

data class EnvelopePaginatedShows(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val media: List<ResponseStandardMedia.ResponseShow>,
    @SerializedName("total_pages") val pages: Int
) {
    companion object {
        fun create(amount: Int): List<EnvelopePaginatedShows> {
            val list = mutableListOf<EnvelopePaginatedShows>()
            repeat(amount) {
                list.add(
                    EnvelopePaginatedShows(
                        page = 1,
                        pages = 1,
                        media = ResponseStandardMedia.createShows(amount)
                    )
                )
            }
            return list.toList()
        }
        fun single() = create(1)[0]
    }
}

sealed class ResponseStandardMedia {
    data class ResponseMovie(
        @SerializedName("id") val id: Long,
        @SerializedName("title") val title: String,
        @SerializedName("overview") val overview: String,
        @SerializedName("poster_path") val posterPath: String?,
        @SerializedName("backdrop_path") val backdropPath: String?,
        @SerializedName("vote_average") val rating: Float,
        @SerializedName("vote_count") val voteCount: Int,
        @SerializedName("popularity") val popularity: Float,
        @SerializedName("release_date") val releaseDate: String
    ) : ResponseStandardMedia()

    data class ResponseShow(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String,
        @SerializedName("overview") val overview: String,
        @SerializedName("poster_path") val posterPath: String?,
        @SerializedName("backdrop_path") val backdropPath: String?,
        @SerializedName("vote_average") val rating: Float,
        @SerializedName("vote_count") val voteCount: Int,
        @SerializedName("popularity") val popularity: Float,
        @SerializedName("first_air_date") val firstAirDate: String
    ) : ResponseStandardMedia()

    companion object {
        fun createMovies(amount: Int): List<ResponseMovie> {
            val list = mutableListOf<ResponseMovie>()
            repeat(amount) {
                list.add(
                    ResponseMovie(
                        id = it.toLong(),
                        title = "title$it",
                        overview = "overview$it",
                        posterPath = "posterPath$it",
                        backdropPath = "backdropPath$it",
                        rating = 1f,
                        voteCount = 1,
                        popularity = 1f,
                        releaseDate = "releaseDate$it"
                    )
                )
            }
            return list.toList()
        }
        fun createShows(amount: Int): List<ResponseShow> {
            val list = mutableListOf<ResponseShow>()
            repeat(amount) {
                list.add(
                    ResponseShow(
                        id = it.toLong(),
                        name = "name$it",
                        overview = "overview$it",
                        posterPath = "posterPath$it",
                        backdropPath = "backdropPath$it",
                        rating = 1f,
                        voteCount = 1,
                        popularity = 1f,
                        firstAirDate = "firstAirDate$it"
                    )
                )
            }
            return list.toList()
        }
    }
}