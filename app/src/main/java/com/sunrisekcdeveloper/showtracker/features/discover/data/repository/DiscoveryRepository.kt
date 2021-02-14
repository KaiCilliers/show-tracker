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

import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DiscoveryClient
import com.sunrisekcdeveloper.showtracker.features.discover.data.local.DiscoveryDao
import com.sunrisekcdeveloper.showtracker.features.discover.data.network.DiscoveryRemoteDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.FeaturedList
import com.sunrisekcdeveloper.showtracker.features.discover.domain.repository.DiscoveryRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.discover.data.local.model.FeaturedMovies
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class DiscoveryRepository(
    private val dao: DiscoveryDao,
    @DiscoveryClient private val remote: DiscoveryRemoteDataSourceContract,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : DiscoveryRepositoryContract {

    override suspend fun featuredMoviesFlow(): Flow<Resource<List<FeaturedList>>> = responseFlow(
        databaseQuery = { FeaturedMovies.featuredListOf(dao.groupedFeatured()) },
        networkCall = { remote.fetchFeaturedMoviesResource() },
        persistData = {
            dao.updateFeatured(*it.flatMap { entry ->
                entry.value.map { movieEntity ->
                    scope.launch { dao.insertMovie(movieEntity) }
                    movieEntity.asFeaturedEntity(entry.key)
                }
            }.toTypedArray())
        }
    )

    private fun <R,E> responseFlow(
        databaseQuery: suspend () -> E,
        networkCall: suspend () -> Resource<R>,
        persistData: suspend (R) -> Unit
    ) : Flow<Resource<E>> {
        return flow {
            emit(Resource.Loading)
            val cache = databaseQuery()
            emit(Resource.Success(cache))

            when(val response = networkCall()) {
                is Resource.Success -> {
                    persistData(response.data)
                }
                is Resource.Error -> {
                    emit(Resource.Error("Error when making network call: ${response.message}"))
                }
                else -> {
                    emit(Resource.Error("Unknown error occurred when making network call"))
                }
            }
        }
    }
}