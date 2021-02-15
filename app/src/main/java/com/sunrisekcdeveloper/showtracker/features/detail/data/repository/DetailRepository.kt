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

package com.sunrisekcdeveloper.showtracker.features.detail.data.repository

import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DetailClient
import com.sunrisekcdeveloper.showtracker.features.detail.data.local.DetailDao
import com.sunrisekcdeveloper.showtracker.features.detail.data.network.DetailDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.repository.DetailRepositoryContract
import com.sunrisekcdeveloper.showtracker.models.DetailedMovie
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie
import kotlinx.coroutines.*

class DetailRepository(
    private val local: DetailDao,
    @DetailClient private val remote: DetailDataSourceContract
) : DetailRepositoryContract {

    private val ioScope = CoroutineScope(Job() + Dispatchers.IO)
    private val cpuScope = CoroutineScope(Job() + Dispatchers.Default)

    // TODO
    //  move to a base class - in a commons package or utils
    private fun update(block: suspend () -> Unit) {
        ioScope.launch {
            block.invoke()
        }
    }

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
}

