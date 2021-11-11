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

package com.sunrisekcdeveloper.search.domain

import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.movie.Movie
import com.sunrisekcdeveloper.movie.MovieRepositoryContract
import com.sunrisekcdeveloper.search.extras.toMovieDomain
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    database: TrackerDatabase
) : MovieRepositoryContract {

    private val dao = database.watchlistMovieDao()

    override suspend fun get(id: String): Movie? {
        TODO("Not yet implemented")
    }

    override suspend fun add(movie: Movie) {
        TODO("Not yet implemented")
    }

    override suspend fun sync(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun distinctFlow(id: String): Flow<Movie?> {
        TODO("Not yet implemented")
    }

    override suspend fun unwatched(): List<Movie> {
        return  dao.unwatched().map { it.toMovieDomain() }
    }
}