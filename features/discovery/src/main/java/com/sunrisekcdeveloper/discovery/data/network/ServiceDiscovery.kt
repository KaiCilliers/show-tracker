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

package com.sunrisekcdeveloper.discovery.data.network

import com.sunrisekcdeveloper.discovery.data.network.model.EnvelopePaginatedMovies
import com.sunrisekcdeveloper.discovery.data.network.model.EnvelopePaginatedShows
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceDiscovery : ServiceDiscoveryContract {
    @GET("movie/popular")
    override suspend fun popularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<EnvelopePaginatedMovies>

    @GET("movie/top_rated")
    override suspend fun topRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<EnvelopePaginatedMovies>

    @GET("movie/upcoming")
    override suspend fun upcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<EnvelopePaginatedMovies>

    @GET("tv/popular")
    override suspend fun popularShows(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<EnvelopePaginatedShows>

    @GET("tv/top_rated")
    override suspend fun topRatedShows(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<EnvelopePaginatedShows>

    @GET("tv/airing_today")
    override suspend fun airingTodayShows(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<EnvelopePaginatedShows>

    data class TestJtoK(
        val page: Int,
        val results: List<Result>,
        val total_pages: Int,
        val total_results: Int
    )

    data class Deets(
        val adult: Boolean,
        val backdrop_path: String,
        val belongs_to_collection: BelongsToCollection,
        val budget: Int,
        val genres: List<Genre>,
        val homepage: String,
        val id: Int,
        val imdb_id: String,
        val original_language: String,
        val original_title: String,
        val overview: String,
        val popularity: Double,
        val poster_path: String,
        val production_companies: List<ProductionCompany>,
        val production_countries: List<ProductionCountry>,
        val release_date: String,
        val revenue: Int,
        val runtime: Int,
        val spoken_languages: List<SpokenLanguage>,
        val status: String,
        val tagline: String,
        val title: String,
        val video: Boolean,
        val vote_average: Double,
        val vote_count: Int
    )

    data class BelongsToCollection(
        val backdrop_path: Any,
        val id: Int,
        val name: String,
        val poster_path: String
    )

    data class Genre(
        val id: Int,
        val name: String
    )

    data class ProductionCompany(
        val id: Int,
        val logo_path: String,
        val name: String,
        val origin_country: String
    )

    data class ProductionCountry(
        val iso_3166_1: String,
        val name: String
    )

    data class SpokenLanguage(
        val english_name: String,
        val iso_639_1: String,
        val name: String
    )

    data class Result(
        val adult: Boolean,
        val backdrop_path: String,
        val genre_ids: List<Int>,
        val id: Int,
        val original_language: String,
        val original_title: String,
        val overview: String,
        val popularity: Double,
        val poster_path: String,
        val release_date: String,
        val title: String,
        val video: Boolean,
        val vote_average: Int,
        val vote_count: Int
    )
}