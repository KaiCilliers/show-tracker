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

package com.sunrisekcdeveloper.showtracker.features.discover.data.network

import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.models.local.core.MovieEntity
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseImages
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.models.network.envelopes.*

interface DiscoveryRemoteDataSourceContract {
    suspend fun fetchTrend(): Resource<List<EnvelopeWatchers>>
    suspend fun fetchPop(): Resource<List<ResponseMovie>>
    suspend fun fetchMostWatched(): Resource<List<EnvelopeViewStats>>

    suspend fun fetchFeaturedMovies(): MutableMap<String, List<MovieEntity>>

    suspend fun poster(id: String): Resource<ResponseImages>

    class Fake() : DiscoveryRemoteDataSourceContract {

        var happyPath = true

        override suspend fun fetchTrend(): Resource<List<EnvelopeWatchers>> = if (happyPath) {
            Resource.Success(EnvelopeWatchers.createEnvelopeWatchers(10))
        } else {
            Resource.Error("unhappy path")
        }

        override suspend fun fetchPop(): Resource<List<ResponseMovie>> = if (happyPath) {
            Resource.Success(ResponseMovie.createResponseMovies(10))
        } else {
            Resource.Error("unhappy path")
        }

        override suspend fun fetchMostWatched(): Resource<List<EnvelopeViewStats>> =
            if (happyPath) {
                Resource.Success(EnvelopeViewStats.createEnvelopeViewStats(10))
            } else {
                Resource.Error("unhappy path")
            }

        override suspend fun fetchFeaturedMovies(): MutableMap<String, List<MovieEntity>> =
            if (happyPath) {
                mutableMapOf(
                    "Top Secret" to MovieEntity.createMovieEntities(10),
                    "Vicious" to MovieEntity.createMovieEntities(10),
                    "Requires IQ lower than 40" to MovieEntity.createMovieEntities(10),
                    "Oldies" to MovieEntity.createMovieEntities(10)
                )
            } else {
                emptyMap<String, List<MovieEntity>>().toMutableMap()
            }

        override suspend fun poster(id: String): Resource<ResponseImages> = if (happyPath) {
            Resource.Success(ResponseImages.createResponseImages(1)[0])
        } else {
            Resource.Error("unhappy path")
        }
    }
}