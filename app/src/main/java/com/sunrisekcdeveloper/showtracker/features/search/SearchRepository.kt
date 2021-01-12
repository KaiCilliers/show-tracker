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

package com.sunrisekcdeveloper.showtracker.features.search

import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.SearchClient
import com.sunrisekcdeveloper.showtracker.features.search.client.SearchDataSourceContract
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie
import kotlinx.coroutines.*

class SearchRepository(
    private val local: SearchDao,
    @SearchClient private val remote: SearchDataSourceContract
) : SearchRepositoryContract {

    private val ioScope = CoroutineScope(Job() + Dispatchers.IO)
    private val cpuScope = CoroutineScope(Job() + Dispatchers.Default)

    // TODO
    //  move to a base class - in a commons package or utils
    private fun update(block: suspend () -> Unit) {
        ioScope.launch {
            block.invoke()
        }
    }

    override suspend fun search(query: String): List<Movie> {
        var list = listOf<Movie>()
        if (query.isNotBlank()) {
            val response = withContext(ioScope.coroutineContext) {
                remote.search("movie", query, "title")
            }
            if (response is Resource.Success) {
                val res = withContext(cpuScope.coroutineContext) {
                    return@withContext response.data.map {
                        update { it.movie.asEntity() }
                        val movie = it.movie.asDomain()
                        withContext(ioScope.coroutineContext) {
                            val poster = remote.poster("${it.movie.identifiers.tmdb}")
                            if (poster is Resource.Success) {
                                movie.posterUrl = poster.data.posters?.get(0)?.url ?: ""
                            }
                        }
                        movie
                    }
                }
                list = res
            }
        }
        return list
    }
}

