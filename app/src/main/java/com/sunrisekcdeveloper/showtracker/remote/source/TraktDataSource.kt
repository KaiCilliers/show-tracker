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
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseShow
import com.sunrisekcdeveloper.showtracker.ui.moreentities.*
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.Path

// TODO remove repeating headers
interface TraktDataSource : NetworkDataSource {
    /** TODO MISCELLANEOUS */
    @GET("genres/{type}")
    override suspend fun genres(@Path("type") type: String): List<ResponseGenre>

    @GET("languages/{type}")
    override suspend fun languages(@Path("type") type: String): List<ResponseLanguage>

    @GET("networks")
    override suspend fun networks(): List<ResponseNetwork>

    /** TODO MOVIES */
    @GET("movies/trending")
    override suspend fun trendingMovies(): List<ResponseWatcher>

    @GET("movies/popular")
    override suspend fun popularMovies(): List<ResponseMovie>

    @GET("movies/recommended/{period}")
    override suspend fun recommendedMovies(@Path("period") period: String): List<ResponseWrapperUserCount>

    @GET("movies/played/{period}")
    override suspend fun mostPlayedMovies(@Path("period") period: String): List<ResponseWrapperMostPlayedWatchedCollected>

    @GET("movies/watched/{period}")
    override suspend fun mostWatchedMovies(@Path("period") period: String): List<ResponseWrapperMostPlayedWatchedCollected>

    @GET("movies/anticipated")
    override suspend fun mostAnticipated(): List<ResponseWrapperListCount>

    @GET("movies/boxoffice")
    override suspend fun boxOffice(): List<ResponseWrapperRevenue>

    @GET("movies/{id}")
    override suspend fun movie(@Path("id") id: String): ResponseMovie

    @GET("movies/{id}/aliases")
    override suspend fun movieAliases(@Path("id") id: String): List<ResponseTitleAlias>

    @GET("movies/{id}/releases/{country}")
    override suspend fun movieReleases(@Path("id") id: String, @Path("country") country: String): List<ResponseRelease>

    @GET("movies/{id}/translations/{language}")
    override suspend fun movieTranslations(@Path("id") id: String, @Path("language") language: String): List<ResponseTranslation>

    @GET("movies/{id}/people")
    override suspend fun moviePersons(@Path("id") id: String): ResponseCastCrewPerson

    @GET("movies/{id}/ratings")
    override suspend fun movieRatings(@Path("id") id: String): ResponseRating

    @GET("movies/{id}/related")
    override suspend fun moviesRelatedTo(@Path("id") id: String): List<ResponseMovie>

    @GET("movies/{id}/stats")
    override suspend fun movieStats(@Path("id") id: String): ResponseMovieStats

    /** TODO SHOWS */

    // TODO OI! START BY OVERRIDING AND MANUALLY testing each show endpoint
    //  move on to season and episode endpoints and manually test them
    //  structure these endpoints and refactor the response objects
    @GET("shows/trending")
    override suspend fun trendingShows(): List<ResponseWatchersShow>

    @GET("shows/popular")
    override suspend fun popularShows(): List<ResponseShow>

    @GET("shows/recommended/{period}")
    override suspend fun recommendedShows(@Path("period") period: String): List<ResponseShowUserCount>

    @GET("shows/played/{period}")
    override suspend fun mostPlayedShows(@Path("period") period: String): List<ResponseWrapperMostPlayedWatchedCollectedShow>

    @GET("shows/watched/{period}")
    override suspend fun mostWatchedShows(@Path("period") period: String): List<ResponseWrapperMostPlayedWatchedCollectedShow>

    @GET("shows/anticipated")
    override suspend fun mostAnticipatedShows(): List<ResponseWrapperListCountShow>

    @GET("shows/{id}")
    override suspend fun show(@Path("id") id: String): ResponseShow

    @GET("shows/{id}/aliases")
    override suspend fun showTitleAliases(@Path("id") id: String): List<ResponseTitleAlias>

    // Giving a HTTP 500 status code
    @GET("shows/{id}/certifications")
    override suspend fun showCertifications(@Path("id") id: String): List<ResponseCertification>

    @GET("shows/{id}/translations/{language}")
    override suspend fun showTranslations(@Path("id") id: String, @Path("language") language: String): List<ResponseTranslation>

    /** TODO PERSON */

    @GET("people/{id}")
    override suspend fun person(@Path("id") id: String): ResponsePerson

    @GET("people/{id}/movies")
    override suspend fun movieCredits(@Path("id") id: String): ResponseCastCrewMovie

    @GET("people/{id}/shows")
    override suspend fun showCredits(@Path("id") id: String): ResponseCastCrewShow
}