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

package com.sunrisekcdeveloper.showtracker.features.discover.domain.repository

import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.FeaturedList
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie
import kotlinx.coroutines.flow.Flow

interface DiscoveryRepositoryContract {

    suspend fun featuredMovies(): Resource<List<FeaturedList>>
    suspend fun featuredMoviesFlow(): Flow<Resource<List<FeaturedList>>>

    class Fake(): DiscoveryRepositoryContract {

        val happyPath = true

        private fun createMovie(amount: Int): List<Movie> {
            val movies = mutableListOf<Movie>()
            var count = 0
            repeat(amount) {
                movies.add(Movie(
                    title = "title$count",
                    slug = "slug$count",
                    posterUrl = "posterUrl$count"
                ))
                count++
            }
            return movies
        }

        override suspend fun featuredMovies(): Resource<List<FeaturedList>> {
            return if (happyPath) {
                Resource.Success(listOf(
                    FeaturedList("Group One", createMovie(10)),
                    FeaturedList("Group Two", createMovie(10)),
                    FeaturedList("Group Three", createMovie(10))
                ))
            } else {
                Resource.Error("Unhappy path")
            }
        }

        override suspend fun featuredMoviesFlow(): Flow<Resource<List<FeaturedList>>> {
            TODO("Not yet implemented")
        }
    }
}