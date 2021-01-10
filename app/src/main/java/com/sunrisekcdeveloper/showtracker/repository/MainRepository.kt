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
import com.sunrisekcdeveloper.showtracker.model.DetailedMovie
import com.sunrisekcdeveloper.showtracker.features.discover.models.Movie
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

    /**
     * TODO
     *  create two flows
     *  one to make a call for movies
     *  one to make a call for movie posters
     *  then zip them together
     *  You can even do it with Room calls
     *  https://blog.mindorks.com/kotlin-flow-zip-operator-parallel-multiple-network-calls
     */

    private val ioScope = CoroutineScope(Job() + Dispatchers.IO)
    private val cpuScope = CoroutineScope(Job() + Dispatchers.Default)

    override suspend fun relatedMovies(slug: String): List<Movie> {
        val response = withContext(ioScope.coroutineContext) { remote.relatedMovies(slug) }
        var list: List<Movie> = listOf()
        if (response is Resource.Success) {
            val res = withContext(cpuScope.coroutineContext) {
                return@withContext response.data.map {
                    val movie = it.asDomain()
                    update { local.insertMovie(it.asEntity()) }
                    withContext(ioScope.coroutineContext) {
                        val poster = remote.poster("${it.identifiers.tmdb}")
                        if (poster is Resource.Success) {
                            movie.posterUrl = poster.data.posters?.get(0)?.url ?: ""
                        }
                    }
                    movie
                }
            }
            list = res
        }
        return list
    }

    override suspend fun movieDetails(slug: String, extended: String): DetailedMovie {
        val response = withContext(ioScope.coroutineContext) { remote.detailedMovie(slug, extended) }
        var entity: DetailedMovie = DetailedMovie(
            Movie("", ""), "", "", "", "", "", ""
        )
        if (response is Resource.Success) {
            entity = response.data.asDomain()
            val poster = remote.poster("${response.data.identifiers.tmdb}")
            if (poster is Resource.Success) {
                entity.basics.posterUrl = poster.data.posters?.get(0)?.url ?: ""
            }
        }
        return entity
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

    private fun update(block: suspend () -> Unit) {
        ioScope.launch {
            block.invoke()
        }
    }
}