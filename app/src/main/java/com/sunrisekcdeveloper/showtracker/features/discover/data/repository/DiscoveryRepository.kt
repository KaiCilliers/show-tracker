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
import com.sunrisekcdeveloper.showtracker.features.discover.data.network.DiscoveryRemoteDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.FeaturedList
import com.sunrisekcdeveloper.showtracker.features.discover.domain.repository.DiscoveryRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.discover.data.local.DiscoveryLocalDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.discover.data.local.model.FeaturedMovies
import com.sunrisekcdeveloper.showtracker.models.local.core.MovieEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class DiscoveryRepository(
    private val local: DiscoveryLocalDataSourceContract,
    @DiscoveryClient private val remote: DiscoveryRemoteDataSourceContract,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : DiscoveryRepositoryContract {

    // In memory data source
    private var cachedFeaturedList: MutableMap<String, FeaturedList>? = null
    // Public to allow edit in testing
    var cacheIsDirty = false

    override suspend fun featuredMovies(): Resource<List<FeaturedList>> {
        setCacheStatus()
        return if (cachedFeaturedList == null && cacheIsDirty) {
            updateCache()
            Resource.Success(FeaturedMovies.featuredListOf(local.featuredMovies()))
        } else if (!cacheIsDirty){
            Resource.Success(FeaturedMovies.featuredListOf(local.featuredMovies()))
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
            local.insertMovie(*movie)
        }
    }

    private suspend fun updateLocalCache(data:  MutableMap<String, List<MovieEntity>>) {
        local.replaceAllFeaturedMovies(*data.flatMap { entry ->
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
        val result = local.featuredMovies()
        val hours = (((System.currentTimeMillis() - result[0].data.updatedAt.time)
        / 1000 ) / 3600)
        Timber.d("hours: $hours")
        if (hours >= 24) {
            cacheIsDirty = true
        }
    }
}