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

package com.sunrisekcdeveloper.showtracker.features.discover

import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie

interface DiscoveryRepositoryContract {
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