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

package com.sunrisekcdeveloper.showtracker.features.search.domain.usecase

import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.RepoSearch
import com.sunrisekcdeveloper.showtracker.features.search.application.SearchMediaByTitleUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.search.domain.domain.UIModelSearch
import com.sunrisekcdeveloper.showtracker.features.search.domain.repository.RepositorySearchContract
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class SearchMediaByTitleUseCase(
    @RepoSearch private val searchRepo: RepositorySearchContract,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SearchMediaByTitleUseCaseContract {
    override suspend fun invoke(page: Int, query: String): Resource<List<UIModelSearch>> {
        return withContext(dispatcher) {
            val searchMoviesCall = async { searchRepo.moviesByTitle(page, query) }
            val searchShowsCall = async { searchRepo.showsByTitle(page, query) }

            val searchMoviesResponse = searchMoviesCall.await()
            val searchShowsResponse = searchShowsCall.await()

            val results = if (searchMoviesResponse is Resource.Success && searchShowsResponse is Resource.Success) {
                searchMoviesResponse.data.union(searchShowsResponse.data).toList()
            } else if (searchMoviesResponse is Resource.Success) {
                searchMoviesResponse.data
            } else if (searchShowsResponse is Resource.Success) {
                searchShowsResponse.data
            } else {
                listOf<UIModelSearch>()
            }
            Resource.Success(results)
        }
    }
}