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

package com.sunrisekcdeveloper.showtracker.commons.data.network

import com.sunrisekcdeveloper.showtracker.commons.data.network.model.*
import com.sunrisekcdeveloper.showtracker.commons.data.network.model.base.*
import com.sunrisekcdeveloper.showtracker.commons.data.network.model.envelopes.*
import com.sunrisekcdeveloper.showtracker.commons.data.network.model.full.ResponseFullMovie
import retrofit2.Response

interface ApiServiceContract {
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

    /**
     * Countries all possible countries with unique codes
     *
     * @param type Possible values: movies, shows
     * @return
     */
    suspend fun countries(type: String): List<ResponseCountry>

    /** TODO MOVIES */

    /**
     * Movie with minimal details
     * Supports Extended Info
     *
     * TODO update documentation
     *
     * @param id
     * @return
     */
    suspend fun movie(id: String, extended: String = ""): Response<ResponseMovie>
    suspend fun movieFull(id: String, extended: String = ""): Response<ResponseFullMovie>

    /**
     * Movie aliases all title aliases for a movie including countries where name is different
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
    suspend fun moviePersons(id: String): ResponseCastCrew

    /**
     * Movie ratings
     *
     * @param id
     * @return
     */
    suspend fun movieRatings(id: String): ResponseRating

    /**
     * Movie stats
     *
     * @param id
     * @return
     */
    suspend fun movieStats(id: String): ResponseStats

    /** TODO SHOWS */

    /**
     * Trending shows all shows being watched right now with the shows with the
     * most users returned first
     * Supports Pagination, Extended Info, Filters
     *
     * @return
     */
    suspend fun trendingShows(): List<EnvelopeWatchers>

    /**
     * Popular shows where popularity is calculated using rating percentage and the
     * number of ratings
     * Supports Pagination, Extended Info, Filters
     *
     * @return
     */
    suspend fun popularShows(): List<ResponseShow>

    /**
     * Recommended shows in specified time period
     * Supports Pagination, Extended, Filter
     *
     * @param period Optional.
     *      Possible values:  daily , weekly , monthly , yearly , all
     *      Default value: weekly
     * @return
     */
    suspend fun recommendedShows(period: String = "weekly"): List<EnvelopeUserCount>

    /**
     * Most played shows in specified time period (single account can watch multiple times)
     * Supports Pagination, Extended, Filter
     *
     * @param period Optional.
     *      Possible values:  daily , weekly , monthly , yearly , all
     *      Default value: weekly
     * @return
     */
    suspend fun mostPlayedShows(period: String = "weekly"): List<EnvelopeViewStats>

    /**
     * Most watched movies in specified time period (unique watches)
     * Supports Pagination, Extended, Filter
     *
     * @param period Optional.
     *      Possible values:  daily , weekly , monthly , yearly , all
     *      Default value: weekly
     * @return
     */
    suspend fun mostWatchedShows(period: String = "weekly"): List<EnvelopeViewStats>

    /**
     * Most anticipated shows based on number of lists a show appears on
     * Supports Pagination, Extended, Filter
     *
     * @return
     */
    suspend fun mostAnticipatedShows(): List<EnvelopeListCount>

    /**
     * Show
     * Supports Extended Info
     *
     * @param id
     * @return
     */
    suspend fun show(id: String): ResponseShow

    /**
     * Show title aliases all including countries where name is different
     *
     * @param id
     * @return
     */
    suspend fun showTitleAliases(id: String): List<ResponseTitleAlias>

    /**
     * Show certifications all of a show including the country
     *
     * @param id
     * @return
     */
    // TODO add movie certifications
    suspend fun showCertifications(id: String): List<ResponseCertification>

    /**
     * Show translations including language and translated values for title, and
     * overview
     *
     * @param id
     * @param language Optional two character language code
     * @return
     */
    // TODO might need different return obj due to missing tagline in json
    suspend fun showTranslations(id: String, language: String = ""): List<ResponseTranslation>

    /**
     * Show people all cast and crew for a show, including the amount of episodes
     * they appeared in
     * Supports Extended Info
     *
     * @param id
     * @return
     */
    suspend fun showPeople(id: String): ResponseCastCrew

    /**
     * Show ratings
     *
     * @param id
     * @return
     */
    suspend fun showRatings(id: String): ResponseRating

    /**
     * Related shows
     * Supports Pagination, Extended Info
     *
     * @param id
     * @return
     */
    suspend fun relatedShows(id: String): List<ResponseShow>

    /**
     * Show stats
     *
     * @param id
     * @return
     */
    suspend fun showStats(id: String): ResponseStats

    /**
     * Show next scheduled air episode
     * Supports Extended Info
     *
     * @param id
     * @return
     */
    suspend fun showNextScheduledAirEpisode(id: String): Response<ResponseEpisode>? // null if there is none

    /**
     * Show most recently aired episode
     * Supports Extended Info
     *
     * @param id
     * @return
     */
    suspend fun showMostRecentlyAiredEpisode(id: String): ResponseEpisode

    /** TODO SEASON */

    /**
     * Season episodes
     * Supports Extended Info
     *
     * @param id
     * @param season
     * @return
     */
    suspend fun seasonEpisodes(id: String, season: Int): List<ResponseEpisode>

    /**
     * Seasons of show
     * Supports Extended Info
     *
     * @param id
     * @return
     */
    suspend fun seasonsOfShow(id: String): List<ResponseSeason>

    /**
     * Season people
     * Supports Extended Info
     *
     * @param id
     * @param season
     * @return
     */
    suspend fun seasonPeople(id: String, season: Int): ResponseCastCrew

    /**
     * Season ratings
     *
     * @param id
     * @param season
     * @return
     */
    suspend fun seasonRatings(id: String, season: Int): ResponseRating

    /**
     * Season stats
     *
     * @param id
     * @param season
     * @return
     */
    suspend fun seasonStats(id: String, season: Int): ResponseStats

    /** TODO EPISODE */

    /**
     * Episode
     *
     * @param id
     * @param season
     * @param episode
     * @return
     */
    suspend fun episode(id: String, season: Int, episode: Int): ResponseEpisode

    /**
     * Episode translations
     *
     * @param id
     * @param season
     * @param episode
     * @param language Optional two character language code
     * @return
     */
    suspend fun episodeTranslations(
        id: String,
        season: Int,
        episode: Int,
        language: String = ""
    ): List<ResponseTranslation>

    /**
     * Episode people
     * Supports Extended Info
     *
     * @param id
     * @param season
     * @param episode
     * @return
     */
    suspend fun episodePeople(id: String, season: Int, episode: Int): ResponseCastCrew

    /**
     * Episode ratings
     *
     * @param id
     * @param season
     * @param episode
     * @return
     */
    suspend fun episodeRatings(id: String, season: Int, episode: Int): ResponseRating

    /**
     * Episode stats
     *
     * @param id
     * @param season
     * @param episode
     * @return
     */
    suspend fun episodeStats(id: String, season: Int, episode: Int): ResponseStats

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
    suspend fun movieCredits(id: String): ResponseCast

    /**
     * Show credits all shows where person is either part of the cast or the crew
     * Supports Extended Info
     *
     * @param id
     * @return
     */
    suspend fun showCredits(id: String): ResponseCast

    /** IMAGES */
    suspend fun poster(id: String): Response<ResponseImages>
}