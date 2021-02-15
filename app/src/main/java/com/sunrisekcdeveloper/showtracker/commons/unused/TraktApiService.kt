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

package com.sunrisekcdeveloper.showtracker.commons.unused

import com.sunrisekcdeveloper.showtracker.BuildConfig
import com.sunrisekcdeveloper.showtracker.models.network.envelopes.EnvelopeViewStats
import com.sunrisekcdeveloper.showtracker.models.network.envelopes.EnvelopeWatchers
import com.sunrisekcdeveloper.showtracker.models.network.*
import com.sunrisekcdeveloper.showtracker.models.network.base.*
import com.sunrisekcdeveloper.showtracker.models.network.envelopes.*
import com.sunrisekcdeveloper.showtracker.models.network.full.ResponseFullMovie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface TraktApiService : ApiServiceContract {
    /** TODO MISCELLANEOUS */

    @GET("genres/{type}")
    override suspend fun genres(@Path("type") type: String): List<ResponseGenre>

    @GET("languages/{type}")
    override suspend fun languages(@Path("type") type: String): List<ResponseLanguage>

    @GET("networks")
    override suspend fun networks(): List<ResponseNetwork>

    @GET("countries/{type}")
    override suspend fun countries(@Path("type") type: String): List<ResponseCountry>

    /** TODO MOVIES */

    @GET("movies/{id}")
    override suspend fun movie(
        @Path("id") id: String,
        @Query("extended") extended: String
    ): Response<ResponseMovie>

    @GET("movies/{id}")
    override suspend fun movieFull(
        @Path("id") id: String,
        @Query("extended") extended: String
    ): Response<ResponseFullMovie>

    @GET("movies/{id}/aliases")
    override suspend fun movieAliases(@Path("id") id: String): List<ResponseTitleAlias>

    @GET("movies/{id}/releases/{country}")
    override suspend fun movieReleases(@Path("id") id: String, @Path("country") country: String): List<ResponseRelease>

    @GET("movies/{id}/translations/{language}")
    override suspend fun movieTranslations(@Path("id") id: String, @Path("language") language: String): List<ResponseTranslation>

    @GET("movies/{id}/people")
    override suspend fun moviePersons(@Path("id") id: String): ResponseCastCrew

    @GET("movies/{id}/ratings")
    override suspend fun movieRatings(@Path("id") id: String): ResponseRating

    @GET("movies/{id}/stats")
    override suspend fun movieStats(@Path("id") id: String): ResponseStats

    /** TODO SHOWS */

    @GET("shows/trending")
    override suspend fun trendingShows(): List<EnvelopeWatchers>

    @GET("shows/popular")
    override suspend fun popularShows(): List<ResponseShow>

    @GET("shows/recommended/{period}")
    override suspend fun recommendedShows(@Path("period") period: String): List<EnvelopeUserCount>

    @GET("shows/played/{period}")
    override suspend fun mostPlayedShows(@Path("period") period: String): List<EnvelopeViewStats>

    @GET("shows/watched/{period}")
    override suspend fun mostWatchedShows(@Path("period") period: String): List<EnvelopeViewStats>

    @GET("shows/anticipated")
    override suspend fun mostAnticipatedShows(): List<EnvelopeListCount>

    @GET("shows/{id}")
    override suspend fun show(@Path("id") id: String): ResponseShow

    @GET("shows/{id}/aliases")
    override suspend fun showTitleAliases(@Path("id") id: String): List<ResponseTitleAlias>

    // Giving a HTTP 500 status code
    @GET("shows/{id}/certifications")
    override suspend fun showCertifications(@Path("id") id: String): List<ResponseCertification>

    @GET("shows/{id}/translations/{language}")
    override suspend fun showTranslations(@Path("id") id: String, @Path("language") language: String): List<ResponseTranslation>

    @GET("shows/{id}/people")
    override suspend fun showPeople(@Path("id") id: String): ResponseCastCrew

    @GET("shows/{id}/ratings")
    override suspend fun showRatings(@Path("id") id: String): ResponseRating

    @GET("shows/{id}/related")
    override suspend fun relatedShows(@Path("id") id: String): List<ResponseShow>

    @GET("shows/{id}/stats")
    override suspend fun showStats(@Path("id") id: String): ResponseStats

    @GET("shows/{id}/next_episode")
    override suspend fun showNextScheduledAirEpisode(@Path("id") id: String): Response<ResponseEpisode>?

    @GET("shows/{id}/last_episode")
    override suspend fun showMostRecentlyAiredEpisode(@Path("id") id: String): ResponseEpisode

    /** TODO SEASON */

    @GET("shows/{id}/seasons/{season}")
    override suspend fun seasonEpisodes(@Path("id") id: String, @Path("season") season: Int): List<ResponseEpisode>

    @GET("shows/{id}/seasons")
    override suspend fun seasonsOfShow(@Path("id") id: String): List<ResponseSeason>

    @GET("shows/{id}/seasons/{season}/people")
    override suspend fun seasonPeople(@Path("id") id: String, @Path("season") season: Int): ResponseCastCrew

    @GET("shows/{id}/seasons/{season}/ratings")
    override suspend fun seasonRatings(@Path("id") id: String, @Path("season") season: Int): ResponseRating

    @GET("shows/{id}/seasons/{season}/stats")
    override suspend fun seasonStats(@Path("id") id: String, @Path("season") season: Int): ResponseStats

    /** TODO EPISODE */

    @GET("shows/{id}/seasons/{season}/episodes/{episode}")
    override suspend fun episode(
        @Path("id") id: String,
        @Path("season") season: Int,
        @Path("episode") episode: Int
    ): ResponseEpisode

    @GET("shows/{id}/seasons/{season}/episodes/{episode}/translations/{language}")
    override suspend fun episodeTranslations(
        @Path("id") id: String,
        @Path("season") season: Int,
        @Path("episode") episode: Int,
        @Path("language") language: String
    ): List<ResponseTranslation>

    @GET("shows/{id}/seasons/{season}/episodes/{episode}/people")
    override suspend fun episodePeople(
        @Path("id") id: String,
        @Path("season") season: Int,
        @Path("episode") episode: Int
    ): ResponseCastCrew

    @GET("shows/{id}/seasons/{season}/episodes/{episode}/ratings")
    override suspend fun episodeRatings(
        @Path("id") id: String,
        @Path("season") season: Int,
        @Path("episode") episode: Int
    ): ResponseRating

    @GET("shows/{id}/seasons/{season}/episodes/{episode}/stats")
    override suspend fun episodeStats(
        @Path("id") id: String,
        @Path("season") season: Int,
        @Path("episode") episode: Int
    ): ResponseStats

    /** TODO PERSON */

    @GET("people/{id}")
    override suspend fun person(@Path("id") id: String): ResponsePerson

    @GET("people/{id}/movies")
    override suspend fun movieCredits(@Path("id") id: String): ResponseCast

    @GET("people/{id}/shows")
    override suspend fun showCredits(@Path("id") id: String): ResponseCast

    /** IMAGES */
    @Headers("Fanart-Api: true")
    @GET("${"BuildConfig.FANART_BASE_URL"}movies/{id}?api_key=${"BuildConfig.FANART_API_KEY"}")
    override suspend fun poster(@Path("id") id: String): Response<ResponseImages>
}