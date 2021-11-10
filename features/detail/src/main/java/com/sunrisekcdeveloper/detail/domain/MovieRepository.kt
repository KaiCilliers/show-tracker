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

import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.detail.DetailRemoteDataSourceContract
import com.sunrisekcdeveloper.detail.extras.*
import com.sunrisekcdeveloper.movie.*
import com.sunrisekcdeveloper.network.NetworkResult
import kotlinx.coroutines.flow.*

class MovieRepository(
    private val remote: DetailRemoteDataSourceContract,
    database: TrackerDatabase
) : MovieRepositoryContract {
    private val movieDao = database.movieDao()
    private val watchlistMovieDao = database.watchlistMovieDao()

    override suspend fun get(id: String): Movie? {
        if (!movieDao.exist(id)) {
            sync(id)
        }
        return movieDao.withId(id)?.toDomain()
    }

    override suspend fun add(movie: Movie) = movieDao.insert(movie.toEntity())

    override suspend fun sync(id: String) {
        val certification = remote.movieReleaseDates(id)
        remote.movieDetails(id).apply {
            if (this is NetworkResult.Success && certification is NetworkResult.Success) {
                add(this.data.asEntityMovie(
                    CertificationsContract.Smart(
                        CertificationsMovie(
                            certification.data.results,
                            ReleaseDateType.Theatrical
                        )
                    ).fromUS()
                ).toDomain())
            }
        }
    }

    override suspend fun distinctFlow(id: String): Flow<Movie?> = movieDao.distinctMovieFlow(id).map { it?.toDomain() }

    override suspend fun unwatched(): List<Movie> {
        return watchlistMovieDao.unwatched().map { it.toMovieDomain() }
    }
}

