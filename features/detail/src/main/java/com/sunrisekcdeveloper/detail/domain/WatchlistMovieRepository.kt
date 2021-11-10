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

package com.sunrisekcdeveloper.detail.domain

import com.sunrisekcdeveloper.cache.FilterMovies
import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.detail.extras.toDomain
import com.sunrisekcdeveloper.detail.extras.toEntity
import com.sunrisekcdeveloper.movie.FullMovie
import com.sunrisekcdeveloper.movie.WatchlistMovie
import com.sunrisekcdeveloper.movie.WatchlistMovieRepositoryContract
import com.sunrisekcdeveloper.movie.valueobjects.MovieFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WatchlistMovieRepository(
    storage: TrackerDatabase
) : WatchlistMovieRepositoryContract {

    private val dao = storage.watchlistMovieDao()

    override suspend fun get(id: String): WatchlistMovie? = dao.withId(id)?.toDomain()

    override suspend fun update(movie: WatchlistMovie) = dao.update(movie.toEntity())

    override suspend fun add(movie: WatchlistMovie) = dao.insert(movie.toEntity())

    override fun distinctFlow(id: String): Flow<WatchlistMovie?> {
        return dao.distinctWatchlistMovieFlow(id).map { it?.toDomain() }
    }

    override fun distinctFlow(filter: MovieFilter): Flow<List<FullMovie>> {
        // temp conversion
        val convertedFilter = when (filter) {
            MovieFilter.NoFilters -> FilterMovies.NoFilters
            MovieFilter.Watched -> FilterMovies.Watched
            MovieFilter.Unwatched -> FilterMovies.Unwatched
            MovieFilter.AddedToday -> FilterMovies.AddedToday
        }
        return dao.distinctWithDetailsFlow(convertedFilter).map { list ->
            list.map { it?.toDomain() }
        }
    }
}