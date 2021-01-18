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

package com.sunrisekcdeveloper.showtracker.features.discover

import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DiscoveryClient
import com.sunrisekcdeveloper.showtracker.features.discover.client.DiscoveryDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.discover.models.FeaturedMovies
import com.sunrisekcdeveloper.showtracker.models.local.core.MovieEntity
import com.sunrisekcdeveloper.showtracker.models.roomresults.*

class DiscoveryRepository(
    private val local: DiscoveryDao,
    @DiscoveryClient private val remote: DiscoveryDataSourceContract
) : DiscoveryRepositoryContract {

    private var cachedFeaturedList: MutableMap<String, FeaturedList>? = null
    private var cacheIsDirty = true

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
        local.insertMovie(*movie)
    }

    private suspend fun updateLocalCache(data:  MutableMap<String, List<MovieEntity>>) {
        local.updateFeatured(*data.flatMap { entry ->
            entry.value.map {
                saveMovie(it)
                it.asFeaturedEntity(entry.key)
            }
        }.toTypedArray())
    }

    private suspend fun updateCache() {
        val response = remote.fetchFeaturedMovies()
        updateInMemoryCache(response)
        updateLocalCache(response)
    }

    override suspend fun featuredMovies(): Resource<List<FeaturedList>> {
        return if (cachedFeaturedList == null && cacheIsDirty) {
            updateCache()
            Resource.Success(FeaturedMovies.featuredListOf(local.groupedFeatured()))
        } else if (!cacheIsDirty){
            Resource.Success(FeaturedMovies.featuredListOf(local.groupedFeatured()))
        } else{
            Resource.Success(cachedFeaturedList!!.values.toList())
        }
    }
}