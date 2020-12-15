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

package com.sunrisekcdeveloper.showtracker.repository

import com.sunrisekcdeveloper.showtracker.data.network.NetworkDataSource
import com.sunrisekcdeveloper.showtracker.data.network.model.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.data.network.model.envelopes.*
import com.sunrisekcdeveloper.showtracker.model.FeaturedList
import com.sunrisekcdeveloper.showtracker.util.datastate.Resource
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.lang.Exception

class MainRepository(
    private val networkSource: NetworkDataSource
) : RepositoryContract {
    override suspend fun trendingMovies(): List<EnvelopeWatchers> = networkSource.trendingMovies()

    override suspend fun popularMovies(): List<ResponseMovie> = networkSource.popularMovies()

//    override suspend fun recommendedMovies(period: String): List<EnvelopeUserCount> =
//        networkSource.recommendedMovies(period)
//
//    override suspend fun mostPlayedMovies(period: String): List<EnvelopeViewStats> =
//        networkSource.mostPlayedMovies(period)
//
//    override suspend fun mostWatchedMovies(period: String): List<EnvelopeViewStats> =
//        networkSource.mostWatchedMovies(period)
//
//    override suspend fun mostAnticipatedMovies(): List<EnvelopeListCount> =
//        networkSource.mostAnticipated()
//
//    override suspend fun boxOffice(): List<EnvelopeRevenue> =
//        networkSource.boxOffice()

    suspend fun test() = flow {
        emit(Resource.Loading)
        try {
            val res = networkSource.test()
            if (res.isSuccessful) {
                emit(Resource.Success(res.body() ?: emptyList()))
            } else {
                emit(Resource.Error(
                    Exception("${res.errorBody()}"), res.message()
                ))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e, "${e.message}"))
        }
    }

    override suspend fun featuredMovies(): List<FeaturedList> {
        val trending = networkSource.trendingMovies().map { it.movie!! }.map { it.asDomain() }
        val popular = networkSource.popularMovies().map { it.asDomain() }
        val recommended = networkSource.recommendedMovies().map { it.movie!! }.map { it.asDomain() }
        val mostPlayed = networkSource.mostPlayedMovies().map { it.movie!! }.map { it.asDomain() }
        val mostWatched = networkSource.mostWatchedMovies().map { it.movie!! }.map { it.asDomain() }
        val mostAnticipated = networkSource.mostAnticipated().map { it.movie!! }.map { it.asDomain() }
        val boxOffice = networkSource.boxOffice().map { it.movie }.map { it.asDomain() }

        return listOf(
            FeaturedList(
                heading = "Trending Movies",
                results = trending),
            FeaturedList(
                heading = "Popular Movies",
                results = popular),
            FeaturedList(
                heading = "Recommended Movies",
                results = recommended),
            FeaturedList(
                heading = "Most Played Movies",
                results = mostPlayed),
            FeaturedList(
                heading = "Most Watched Movies",
                results = mostWatched),
            FeaturedList(
                heading = "Anticipated Movies",
                results = mostAnticipated),
            FeaturedList(
                heading = "Box Office Movies",
                results = boxOffice)
        )
    }
}