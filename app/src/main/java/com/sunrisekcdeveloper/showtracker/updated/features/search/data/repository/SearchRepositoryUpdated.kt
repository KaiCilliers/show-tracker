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

package com.sunrisekcdeveloper.showtracker.updated.features.search.data.repository

import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.SearchClientUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.network.NetworkResult
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.network.model.ResponseStandardMediaUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.repository.asDiscoveryUIModel
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.domain.model.DiscoveryUIModel
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.domain.model.ListType
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.domain.model.MediaTypeUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.search.data.network.SearchRemoteDataSourceContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.search.data.network.SearchServiceContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.search.domain.repository.SearchRepositoryContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.search.presentation.SearchUIModel

class SearchRepositoryUpdated(
    @SearchClientUpdated private val remote: SearchRemoteDataSourceContractUpdated
) : SearchRepositoryContractUpdated {
    override suspend fun moviesByTitle(page: Int, query: String): Resource<List<SearchUIModel>> {
        return when (val response = remote.moviesByTitle(query, page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asSearchUIModel() })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.message)
            }
        }
    }

    override suspend fun showsByTitle(page: Int, query: String): Resource<List<SearchUIModel>> {
        return when (val response = remote.showsByTitle(query, page)) {
            is NetworkResult.Success -> {
                Resource.Success(response.data.media.map { it.asSearchUIModel() })
            }
            is NetworkResult.Error -> {
                Resource.Error(response.message)
            }
        }
    }
}

fun ResponseStandardMediaUpdated.ResponseMovieUpdated.asSearchUIModel() = SearchUIModel(
    id = "$id",
    title = title,
    mediaType = MediaTypeUpdated.Movie,
    posterPath = posterPath ?: ""
)
fun ResponseStandardMediaUpdated.ResponseShowUpdated.asSearchUIModel() = SearchUIModel(
    id = "$id",
    title = name,
    mediaType = MediaTypeUpdated.Show,
    posterPath = posterPath ?: ""
)