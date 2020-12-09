/*
 * Copyright © 2020. The Android Open Source Project
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

package com.sunrisekcdeveloper.showtracker.remote.source

import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.entities.network.ResponseWatcher
import com.sunrisekcdeveloper.showtracker.ui.moreentities.*
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

// TODO remove repeating headers
interface TraktDataSource : NetworkDataSource {
    /** TODO MISCELLANEOUS */
    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("genres/{type}")
    override suspend fun genres(@Path("type") type: String): List<ResponseGenre>

    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("languages/{type}")
    override suspend fun languages(@Path("type") type: String): List<ResponseLanguage>

    /** TODO MOVIES */
    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/trending")
    override suspend fun trendingMovies(): List<ResponseWatcher>

    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/popular")
    override suspend fun popularMovies(): List<ResponseMovie>

    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/recommended/{period}")
    override suspend fun recommendedMovies(@Path("period") period: String): List<ResponseWrapperUserCount>

    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/played/{period}")
    override suspend fun mostPlayedMovies(@Path("period") period: String): List<ResponseWrapperMostPlayedWatchedCollected>

    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/watched/{period}")
    override suspend fun mostWatchedMovies(@Path("period") period: String): List<ResponseWrapperMostPlayedWatchedCollected>

    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/anticipated")
    override suspend fun mostAnticipated(): List<ResponseWrapperListCount>

    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/boxoffice")
    override suspend fun boxOffice(): List<ResponseWrapperRevenue>

    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/{id}")
    override suspend fun movie(@Path("id") id: String): ResponseMovie

    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/{id}/aliases")
    override suspend fun movieAliases(@Path("id") id: String): List<ResponseTitleAlias>

    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/{id}/releases/{country}")
    override suspend fun movieReleases(@Path("id") id: String, @Path("country") country: String): List<ResponseRelease>

    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/{id}/translations/{language}")
    override suspend fun movieTranslations(@Path("id") id: String, @Path("language") language: String): List<ResponseTranslation>

    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/{id}/people")
    override suspend fun moviePersons(@Path("id") id: String): ResponseCastCrew

    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/{id}/ratings")
    override suspend fun movieRatings(@Path("id") id: String): ResponseRating

    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/{id}/related")
    override suspend fun moviesRelatedTo(@Path("id") id: String): List<ResponseMovie>

    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/{id}/stats")
    override suspend fun movieStats(@Path("id") id: String): ResponseMovieStats
}