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

import com.sunrisekcdeveloper.showtracker.data.local.MovieDao
import com.sunrisekcdeveloper.showtracker.data.local.model.categories.*
import com.sunrisekcdeveloper.showtracker.data.network.NetworkDataSourceContract
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DataSourceTrakt
import com.sunrisekcdeveloper.showtracker.model.Movie
import com.sunrisekcdeveloper.showtracker.util.datastate.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val local: MovieDao,
    @DataSourceTrakt private val remote: NetworkDataSourceContract
) : RepositoryContract {

    private val ioScope = CoroutineScope(Job() + Dispatchers.IO)
    private val cpuScope = CoroutineScope(Job() + Dispatchers.Default)


    override suspend fun trendingMovie(): List<Movie> {
        val result = withContext(ioScope.coroutineContext) { local.trendingMovies() }
        return withContext(cpuScope.coroutineContext) {
            result.map { item ->
                item.movie?.asDomain()!!
            }
        }
    }

    override suspend fun popularMovie(): List<Movie> {
        return withContext(cpuScope.coroutineContext) {
            withContext(ioScope.coroutineContext) {
                local.popularMovies()
            }.map { it.movie?.asDomain()!! }
        }
    }

    override suspend fun boxofficeMovie(): List<Movie> {
        return withContext(cpuScope.coroutineContext) {
            withContext(ioScope.coroutineContext) {
                local.boxOfficeMovies()
            }.map { it.movie?.asDomain()!! }
        }
    }

    override suspend fun mostPlayedMovie(): List<Movie> = withContext(cpuScope.coroutineContext) {
        withContext(ioScope.coroutineContext) {
            local.mostPlayedMovies()
        }.map { it.movie?.asDomain()!! }
    }

    override suspend fun mostWatchedMovie(): List<Movie> = withContext(cpuScope.coroutineContext) {
        withContext(ioScope.coroutineContext) {
            local.mostWatchedMovies()
        }.map { it.movie?.asDomain()!! }
    }

    override suspend fun mostAnticipatedMovie(): List<Movie> = withContext(cpuScope.coroutineContext) {
        withContext(ioScope.coroutineContext) {
            local.mostAnticipatedMovies()
        }.map { it.movie?.asDomain()!! }
    }

    override suspend fun recommendedMovie(): List<Movie> = withContext(cpuScope.coroutineContext) {
        withContext(ioScope.coroutineContext) {
            local.recommended()
        }.map { it.movie?.asDomain()!! }
    }

    // https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
    suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> T
    ): Resource<T> =
        withContext(dispatcher) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> Resource.Error("")
                    is HttpException -> {
                        Resource.Error(
                            "code = throwable.code()"
                        )
                    }
                    else -> {
                        Resource.Error("null, null")
                    }
                }
            }
        }

//    suspend fun foo() = withContext(ioScope.coroutineContext) {
//        remote.fetchTrend()
//    }

    override suspend fun updateTrending() {
        val response = remote.fetchTrend()
        if (response is Resource.Success) {
            val exists = local.fetchTrending()
            local.insertMovie(*response.data.map { it.movie!!.asEntity() }.toTypedArray())
            if (exists.size == 10) {
                local.updateTrending(*response.data.map { it.asTrendingMovieEntity() }
                    .toTypedArray())
            } else {
                local.replaceTrending(*response.data.map { it.asTrendingMovieEntity() }
                    .toTypedArray())
            }
        }
    }

    override suspend fun updateBox() {
        val response = remote.fetchBox()
        if (response is Resource.Success) {
            val exists = local.fetchBox()
            local.insertMovie(*response.data.map { it.movie.asEntity() }.toTypedArray())
            if (exists.size == 10) {
                local.updateBox(*response.data.map { it.asEntity() }.toTypedArray())
            } else {
                local.replaceBox(*response.data.map { it.asEntity() }.toTypedArray())
            }
        }
    }

    override suspend fun updatePopular() {
        val response = remote.fetchPop()
        if (response is Resource.Success) {
            val exists = local.fetchPopular()
            local.insertMovie(*response.data.map { it.asEntity() }.toTypedArray())
            if (exists.size == 10) {
                val s = local.updatePopular(*response.data.map { PopularListEntity.from(it) }
                    .toTypedArray())
            } else {
                local.replacePopular(*response.data.map { PopularListEntity.from(it) }
                    .toTypedArray())
            }
        }
    }

    override suspend fun updateMostPlayed() {
        val response = remote.fetchMostPlayed()
        if (response is Resource.Success) {
            val exists = local.fetchMostPlayed()
            local.insertMovie(*response.data.map { it.movie!!.asEntity() }.toTypedArray())
            if (exists.size == 10) {
                local.updateMostPlayed(*response.data.map { MostPlayedListEntity.from(it) }
                    .toTypedArray())
            } else {
                local.replaceMostPlayed(*response.data.map { MostPlayedListEntity.from(it) }
                    .toTypedArray())
            }
        }
    }

    override suspend fun updateMostWatched() {
        val response = remote.fetchMostWatched()
        if (response is Resource.Success) {
            val exists = local.fetchMostWatched()
            local.insertMovie(*response.data.map { it.movie!!.asEntity() }.toTypedArray())
            if (exists.size == 10) {
                local.updateMostWatched(*response.data.map { MostWatchedListEntity.from(it) }
                    .toTypedArray())
            } else {
                local.replaceMostWatched(*response.data.map { MostWatchedListEntity.from(it) }
                    .toTypedArray())
            }
        }
    }

    override suspend fun updateAnticipated() {
        val response = remote.fetchAnticipated()
        // val response = safeApiCall { remote.fetchAnticipated() }
        if (response is Resource.Success) {
            val exists = local.fetchAnticipated()
            local.insertMovie(*response.data.map { it.movie!!.asEntity() }.toTypedArray())
            if (exists.size == 10) {
                local.updateAnticipated(*response.data.map { AnticipatedListEntity.from(it) }
                    .toTypedArray())
            } else {
                local.replaceAnticipated(*response.data.map { AnticipatedListEntity.from(it) }
                    .toTypedArray())
            }
        }
    }

    override suspend fun updateRecommended() {
        val response = remote.fetchRecommended()
        if (response is Resource.Success) {
            val exists = local.fetchRecommended()
            local.insertMovie(*response.data.map { it.movie!!.asEntity() }.toTypedArray())
            if (exists.size == 10) {
                local.updateRecommended(*response.data.map { RecommendedListEntity.from(it) }
                    .toTypedArray())
            } else {
                local.replaceRecommended(*response.data.map { RecommendedListEntity.from(it) }
                    .toTypedArray())
            }
        }
    }
}