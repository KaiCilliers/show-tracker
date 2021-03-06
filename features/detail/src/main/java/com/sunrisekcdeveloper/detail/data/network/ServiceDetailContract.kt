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

package com.sunrisekcdeveloper.detail.data.network

import com.sunrisekcdeveloper.detail.BuildConfig
import com.sunrisekcdeveloper.detail.data.model.*
import retrofit2.Response

interface ServiceDetailContract {
    suspend fun movieDetails(
        id: String,
        apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<ResponseMovieDetail>

    suspend fun movieCertifications(
        id: String,
        apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<EnvelopeMovieReleaseDates>

    suspend fun showDetails(
        id: String,
        apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<ResponseShowDetail>

    suspend fun showCertifications(
        id: String,
        apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<EnvelopeShowCertification>

    class Fake() : ServiceDetailContract {
        override suspend fun movieDetails(
            id: String,
            apiKey: String
        ): Response<ResponseMovieDetail> {
            return Response.success(
                ResponseMovieDetail(
                    id = id.toInt(),
                    title = "a",
                    overview = "a",
                    backdropPath = "a",
                    posterPath = "a",
                    releaseDate = "a",
                    runtime = 1,
                    popularityValue = 1F,
                    rating = 1F
                )
            )
        }

        override suspend fun movieCertifications(
            id: String,
            apiKey: String
        ): Response<EnvelopeMovieReleaseDates> {
            return Response.success(
                EnvelopeMovieReleaseDates(
                    id = id.toInt(),
                    results = listOf(
                        ResponseMovieReleaseDates(
                            iso = "a",
                            releaseDates = listOf(
                                ResponseCertificationAndReleaseDate(
                                    certification = "a",
                                    releaseDate = "a",
                                    releaseType = 1
                                ),
                                ResponseCertificationAndReleaseDate(
                                    certification = "b",
                                    releaseDate = "b",
                                    releaseType = 2
                                )
                            )
                        ),
                        ResponseMovieReleaseDates(
                            iso = "b",
                            releaseDates = listOf(
                                ResponseCertificationAndReleaseDate(
                                    certification = "c",
                                    releaseDate = "c",
                                    releaseType = 1
                                )
                            )
                        )
                    )
                )
            )
        }

        override suspend fun showDetails(id: String, apiKey: String): Response<ResponseShowDetail> {
            return Response.success(
                ResponseShowDetail(
                    id = id.toInt(),
                    name = "a",
                    overview = "a",
                    backdropPath = "a",
                    posterPath = "a",
                    rating = 1f,
                    firstAirYear = "a",
                    episodeCount = 1,
                    seasonCount = 1,
                    popularityValue = 1f
                )
            )
        }

        override suspend fun showCertifications(
            id: String,
            apiKey: String
        ): Response<EnvelopeShowCertification> {
            return Response.success(
                EnvelopeShowCertification(
                    id = id.toInt(),
                    results = listOf(
                        ResponseShowCertification(
                            iso = "a",
                            certification = "a"
                        ),
                        ResponseShowCertification(
                            iso = "b",
                            certification = "b"
                        )
                    )
                )
            )
        }
    }
}