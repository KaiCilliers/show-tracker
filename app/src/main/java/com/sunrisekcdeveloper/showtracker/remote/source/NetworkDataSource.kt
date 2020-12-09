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

interface NetworkDataSource {
    /** TODO MISCELLANEOUS */
    /**
     * Genres all possible genres that can be attached to a show or movie
     *
     * @param type possible values are "movies","shows"
     * @return
     */
    suspend fun genres(type: String): List<ResponseGenre>

    /**
     * Languages all possible languages that a show or movie can be filtered by
     *
     * @param type Possible values: movies, shows
     * @return
     */
    suspend fun languages(type: String): List<ResponseLanguage>

    /**
     * Networks that a show could have originally aired from
     *
     * @return
     */
    suspend fun networks(): List<ResponseNetwork>

    /** TODO MOVIES */
    /**
     * Trending movies all movies currently being watched sorted with most
     * watched returned first
     * Supports Pagination, Extended, Filter
     *
     * @return
     */
    suspend fun trendingMovies(): List<ResponseWatcher>

    /**
     * Popular movies popularity calculated using rating percentage and the number
     * of ratings
     * Supports Pagination, Extended, Filter
     *
     * @return
     */
    suspend fun popularMovies(): List<ResponseMovie>

    /**
     * Recommended movies in specified time period
     * Supports Pagination, Extended, Filter
     *
     * @param period Optional.
     *      Possible values:  daily , weekly , monthly , yearly , all
     *      Default value: weekly
     * @return
     */
    suspend fun recommendedMovies(period: String = "weekly"): List<ResponseWrapperUserCount>

    /**
     * Most played movies in specified time period (single account can watch multiple times)
     * Supports Pagination, Extended, Filter
     *
     * @param period Optional.
     *      Possible values:  daily , weekly , monthly , yearly , all
     *      Default value: weekly
     * @return
     */
    suspend fun mostPlayedMovies(period: String = "weekly"): List<ResponseWrapperMostPlayedWatchedCollected>

    /**
     * Most watched movies in specified time period (unique watches)
     * Supports Pagination, Extended, Filter
     *
     * @param period Optional.
     *      Possible values:  daily , weekly , monthly , yearly , all
     *      Default value: weekly
     * @return
     */
    // TODO you can create less methods in Repo that takes params which will decide which of
    //  these methods to call (like it'll choose between most played and most watched)
    suspend fun mostWatchedMovies(period: String): List<ResponseWrapperMostPlayedWatchedCollected>

    /**
     * Most anticipated based on number of lists movie appears in
     * Supports Pagination, Extended, Filter
     *
     * @return
     */
    suspend fun mostAnticipated(): List<ResponseWrapperListCount>

    /**
     * Box office based on top 10 grossing movies in U.S. box office the weekend past. Updates
     * every Monday morning
     * Supports Extended
     *
     * @return
     */
    suspend fun boxOffice(): List<ResponseWrapperRevenue>

    /**
     * Movie with minimal details
     * Supports Extended Info
     *
     * @param id
     * @return
     */
    suspend fun movie(id: String): ResponseMovie

    /**
     * Movie aliases all title aliases for a movie including countries where name is differenr
     *
     * @param id
     * @return
     */
    suspend fun movieAliases(id: String): List<ResponseTitleAlias>

    /**
     * Movie releases including where and how it was released
     *
     * @param id
     * @param country Optional two character country code
     * @return
     */
    // TODO note that empty string represent an omission of the path variable, thus making it
    //  an optional field
    suspend fun movieReleases(id: String, country: String = ""): List<ResponseRelease>

    /**
     * Movie translations including language and translated values for title, tagline, and
     * overview
     *
     * @param id
     * @param language Optional two character language code
     * @return
     */
    suspend fun movieTranslations(id: String, language: String = ""): List<ResponseTranslation>

    /**
     * Movie persons returns all cast and crew for a movie
     * Supports Extended Info
     *
     * @param id
     * @return
     */
    suspend fun moviePersons(id: String): ResponseCastCrewPerson

    /**
     * Movie ratings
     *
     * @param id
     * @return
     */
    suspend fun movieRatings(id: String): ResponseRating

    /**
     * Movies related to a specific movie
     * Supports Pagination, Extended Info
     *
     * @param id
     * @return
     */
    suspend fun moviesRelatedTo(id: String): List<ResponseMovie>

    /**
     * Movie stats
     *
     * @param id
     * @return
     */
    suspend fun movieStats(id: String): ResponseMovieStats

    /** TODO SHOWS */


    /** TODO PEOPLE */
    /**
     * Person
     * Supports Extended Info
     *
     * @param id
     * @return
     */
    suspend fun person(id: String): ResponsePerson

    /**
     * Movie credits all movies where person is either part the cast or the crew
     * Supports Extended Info
     *
     * @param id
     * @return
     */
    suspend fun movieCredits(id: String): ResponseCastCrewMovie

    /**
     * Show credits all shows where person is either part of the cast or the crew
     * Supports Extended Info
     *
     * @param id
     * @return
     */
    suspend fun showCredits(id: String): ResponseCastCrewShow
}