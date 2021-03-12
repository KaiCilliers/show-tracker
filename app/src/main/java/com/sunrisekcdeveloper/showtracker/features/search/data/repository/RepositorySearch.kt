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

package com.sunrisekcdeveloper.showtracker.features.search.data.repository

import com.sunrisekcdeveloper.showtracker.common.NetworkResult
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.common.util.asUIModelSearch
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.SourceSearch
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.ResponseStandardMedia
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.search.data.network.RemoteDataSourceSearchContract
import com.sunrisekcdeveloper.showtracker.features.search.domain.domain.UIModelSearch
import com.sunrisekcdeveloper.showtracker.features.search.domain.repository.RepositorySearchContract
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

class RepositorySearch(
    @SourceSearch private val remote: RemoteDataSourceSearchContract,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RepositorySearchContract {

    override suspend fun searchMediaByTitle(
        page: Int,
        query: String
    ): Flow<Resource<List<UIModelSearch>>> {
        val movieFlow = flow { emit(remote.moviesByTitle(query, page)) }.flowOn(dispatcher)
        val showFlow = flow { emit(remote.showsByTitle(query, page)) }.flowOn(dispatcher)

        return combine(movieFlow, showFlow) { movieResponse, showResponse ->
            val result = mutableListOf<UIModelSearch>()

            when (movieResponse) {
                is NetworkResult.Success -> {
                    result.addAll(movieResponse.data.media.asUIModelSearch())
                }
                is NetworkResult.Error -> {
                    Timber.d("Error - movie search call was not successful: ${movieResponse.message}")
                }
            }
            when (showResponse) {
                is NetworkResult.Success -> {
                    result.addAll(showResponse.data.media.asUIModelSearchh())
                }
                is NetworkResult.Error -> {
                    Timber.d("Error - show search call was not successful: ${showResponse.message}")
                }
            }
            Resource.Success(result.toList())
        }
    }
}

// todo did not move to extensions file due to not having a solution
//  to the function naming conflicts...
fun ResponseStandardMedia.ResponseMovie.asUIModelSearch() = UIModelSearch(
    id = "$id",
    title = title,
    mediaType = MediaType.Movie,
    posterPath = posterPath ?: ""
)
fun List<ResponseStandardMedia.ResponseMovie>.asUIModelSearch() = this.map { it.asUIModelSearch() }
fun List<ResponseStandardMedia.ResponseShow>.asUIModelSearchh() = this.map { it.asUIModelSearch() }
fun ResponseStandardMedia.ResponseShow.asUIModelSearch() = UIModelSearch(
    id = "$id",
    title = name,
    mediaType = MediaType.Show,
    posterPath = posterPath ?: ""
)