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
import com.sunrisekcdeveloper.showtracker.data.local.model.categories.PopularListEntity
import com.sunrisekcdeveloper.showtracker.data.network.NetworkDataSourceContract
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DataSourceTrakt
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.MainRepo
import com.sunrisekcdeveloper.showtracker.model.FeaturedList
import com.sunrisekcdeveloper.showtracker.util.datastate.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val local: MovieDao,
    @DataSourceTrakt private val remote: NetworkDataSourceContract
) : RepositoryContract {

    override fun repoSavingGrace(): Flow<Resource<String>> = flow {
        while(true) {
            emit(Resource.Success("My String"))
            delay(4000)
            emit(Resource.Success("Fetching trending movies from disk"))
            val cache = local.trendingMovies()
            if(cache.isEmpty()) {
                emit(Resource.Success("Cache is empty :("))
                emit(Resource.Success("Making a network call to populate cache"))
                delay(4000)
                val res = remote.fetchTrend()
                when (res) {
                    is Resource.Success -> {
                        emit(Resource.Success("call was successful: ${res.data.size}"))
                        delay(4000)
                        val movies = res.data.map { it.movie!!.asEntity() }
                        val trending = res.data.map { it.asTrendingMovieEntity() }
                        emit(Resource.Success("Mapped response to room entities"))
                        emit(Resource.Success("Going to insert the entities to disk"))
                        delay(4000)
                        local.insertMovie(*movies.toTypedArray())
                        local.insertTrending(*trending.toTypedArray())
                        emit(Resource.Success("Inserted new data"))
                        delay(4000)
                        emit(Resource.Success("GOing to fetch data to display from disk"))
                        val newchace = local.trendingMovies()
                        emit(Resource.Success("Done: $newchace"))
                    }
                    is Resource.Error -> {
                        emit(res)
                        delay(4000)
                    }
                    is Resource.Loading -> {}
                }
            } else {
                emit(Resource.Success("Cache not empty: $cache"))
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun trending() = flow {
        local.trendingMoviesFlow().map {
            FeaturedList(
                "Trending WHOOP",
                it.map { item -> item.movie!!.asDomain() }
            )
        }.collect {
            emit(it)
        }
    }

    override fun popular() = flow {
        local.popularMoviesFlow().map {
            FeaturedList(
                "POPULAR WHOOP",
                it.map { item -> item.movie!!.asDomain() }
            )
        }.collect {
            emit(it)
        }
    }

    override fun box() = flow {
        local.boxOfficeMoviesFlow().map {
            FeaturedList(
                "BOX OFFICE WHOOP",
                it.map { item -> item.movie!!.asDomain() }
            )
        }.collect {
            emit(it)
        }
    }

    override fun movies() = local.allMovies()
    override suspend fun deleteSingleMovie() = local.deleteOneMovie()
    override suspend fun tempUpdateCache() {
        val box = remote.fetchBox()
        val trend = remote.fetchTrend()
        val pop = remote.fetchPop()
        when (box) {
            is Resource.Success -> {
                local.insertMovie(*box.data.map { it.movie.asEntity() }.toTypedArray())
                local.replaceBox(
                    *box.data.map { it.asEntity() }.toTypedArray()
                )
            }
            is Resource.Error -> {
            }
            is Resource.Loading -> {
            }
        }
        when (trend) {
            is Resource.Success -> {
                local.insertMovie(*trend.data.map { it.movie!!.asEntity() }.toTypedArray())
                local.replaceTrending(
                    *trend.data.map { it.asTrendingMovieEntity() }.toTypedArray()
                )
            }
            is Resource.Error -> {
            }
            is Resource.Loading -> {
            }
        }
        when (pop) {
            is Resource.Success -> {
                local.insertMovie(*pop.data.map { it.asEntity() }.toTypedArray())
                local.replacePopular(
                    *pop.data.map { PopularListEntity.from(it) }.toTypedArray()
                )
            }
            is Resource.Error -> {
            }
            is Resource.Loading -> {
            }
        }
    }
}