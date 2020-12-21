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

import com.sunrisekcdeveloper.showtracker.data.local.model.categories.TrendingListEntity
import com.sunrisekcdeveloper.showtracker.data.local.model.core.MovieEntity
import com.sunrisekcdeveloper.showtracker.model.FeaturedList
import com.sunrisekcdeveloper.showtracker.model.Movie
import com.sunrisekcdeveloper.showtracker.util.datastate.Resource
import kotlinx.coroutines.flow.Flow

interface RepositoryContract {
    fun trendingMoviesFlow(): Flow<List<Movie>>
    fun popularMoviesFlow(): Flow<List<Movie>>
    fun boxofficeMoviesFlow(): Flow<List<Movie>>
    fun mostPlayedMoviesFlow(): Flow<List<Movie>>
    fun mostWatchedMoviesFlow(): Flow<List<Movie>>
    fun mostAnticipatedMoviesFlow(): Flow<List<Movie>>
    fun recommendedMoviesFlow(): Flow<List<Movie>>

    suspend fun trendingMovie(): List<Movie>
    suspend fun popularMovie(): List<Movie>
    suspend fun boxofficeMovie(): List<Movie>
    suspend fun mostPlayedMovie(): List<Movie>
    suspend fun mostWatchedMovie(): List<Movie>
    suspend fun mostAnticipatedMovie(): List<Movie>
    suspend fun recommendedMovie(): List<Movie>

    suspend fun updateTrending()
    suspend fun updateBox()
    suspend fun updatePopular()
    suspend fun updateMostPlayed()
    suspend fun updateMostWatched()
    suspend fun updateAnticipated()
    suspend fun updateRecommended()
}