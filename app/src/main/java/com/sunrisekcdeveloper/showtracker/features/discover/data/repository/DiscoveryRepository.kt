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

package com.sunrisekcdeveloper.showtracker.features.discover.data.repository

import com.sunrisekcdeveloper.showtracker.commons.util.asFeaturedMovieEntityExtension
import com.sunrisekcdeveloper.showtracker.commons.util.asMovieEntity
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.commons.util.toFeaturedList
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DiscoveryClient
import com.sunrisekcdeveloper.showtracker.features.discover.data.local.DiscoveryDao
import com.sunrisekcdeveloper.showtracker.features.discover.data.network.DiscoveryRemoteDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.FeaturedList
import com.sunrisekcdeveloper.showtracker.features.discover.domain.repository.DiscoveryRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.discover.data.local.model.FeaturedMovies
import com.sunrisekcdeveloper.showtracker.models.local.core.MovieEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber

class DiscoveryRepository(
    private val dao: DiscoveryDao,
    @DiscoveryClient private val remote: DiscoveryRemoteDataSourceContract,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : DiscoveryRepositoryContract {

    override suspend fun featuredMoviesFlow(): Flow<Resource<List<FeaturedList>>> = responseFlow(
        databaseQuery = { dao.groupedFeaturedFlow().map { it.toFeaturedList() } },
        networkCall = { remote.fetchFeaturedMoviesResource() },
        persistData = {
            scope.launch {
                dao.updateFeatured(*it.flatMap { entry ->
                    entry.value.map { responseMovie ->
                        launch { dao.insertMovie(responseMovie.asMovieEntity()) }
                        responseMovie.asFeaturedMovieEntityExtension(entry.key)
                    }
                }.toTypedArray())
            }
//            it.flatMap {
//                it.value.map {
//                    scope.launch { dao.insertMovie(it.asMovieEntity()) }
//                    scope.launch { dao.updateFeatured(it.asFeaturedMovieEntityExtension()) }
//                }
//            }
        }
    )

    fun <ResponseModel, EntityModel> responseFlow(
        databaseQuery: suspend () -> Flow<EntityModel>,
        networkCall: suspend () -> Resource<ResponseModel>,
        persistData: suspend (ResponseModel) -> Unit) : Flow<Resource<EntityModel>> {
        return flow {
            // Loading state
            Timber.d("LOADING EMIT")
            emit(Resource.Loading)
            // Fetch local data
            Timber.d("Database query")
            val localdata = databaseQuery().map { Resource.Success(it) }

            Timber.d("network call")
            val response = networkCall()

            when(response) {
                is Resource.Success -> {
                    Timber.d("persisted network data to database")
                    persistData(response.data)
                }
                is Resource.Error -> {
                    Timber.d("ERROR")
                    emit(Resource.Error(response.message))
                    Timber.d("Erorr emission")
                    emitAll(localdata)
                }
                else -> {
                    Timber.d("Other erorr")
                    emit(Resource.Error("Something went wrong, please try again later"))
                    Timber.d("Other eror emission")
                    emitAll(localdata)
                }
            }
            Timber.d("Database emission")
            emitAll(localdata)
        }
    }

    // In memory data source
    private var cachedFeaturedList: MutableMap<String, FeaturedList>? = null
    // Public to allow edit in testing
    var cacheIsDirty = false

    override suspend fun featuredMovies(): Resource<List<FeaturedList>> {
        setCacheStatus()
        return if (cachedFeaturedList == null && cacheIsDirty) {
            updateCache()
            Resource.Success(FeaturedMovies.featuredListOf(dao.groupedFeatured()))
        } else if (!cacheIsDirty){
            Resource.Success(FeaturedMovies.featuredListOf(dao.groupedFeatured()))
        } else {
            Resource.Success(cachedFeaturedList!!.values.toList())
        }
    }

    private fun updateInMemoryCache(data:  MutableMap<String, List<MovieEntity>>) {
        val featuredMovies = data.mapValues { entry ->
            FeaturedList(
                heading = entry.key,
                results = entry.value.map { it.asDomain() }
            )
        }
        cachedFeaturedList = featuredMovies.toMutableMap()
    }

    private suspend fun saveMovie(vararg movie: MovieEntity) {
        scope.launch {
            dao.insertMovie(*movie)
        }
    }

    private suspend fun updateLocalCache(data:  MutableMap<String, List<MovieEntity>>) {
        dao.updateFeatured(*data.flatMap { entry ->
            entry.value.map {
                saveMovie(it)
                it.asFeaturedEntity(entry.key)
            }
        }.toTypedArray())
    }

    private suspend fun updateCache() {
        // TODO: 18-01-2021 remove the poster calls in the movieEntityOf call - move it somewhere else :/
        val response = remote.fetchFeaturedMovies()
        updateInMemoryCache(response)
        updateLocalCache(response)
        cacheIsDirty = false
    }

    private suspend fun setCacheStatus() {
        val result = dao.groupedFeatured()
        val hours = (((System.currentTimeMillis() - result[0].data.updatedAt.time)
        / 1000 ) / 3600)
        Timber.d("hours: $hours")
        if (hours >= 0) {
            cacheIsDirty = true
        }
    }
}