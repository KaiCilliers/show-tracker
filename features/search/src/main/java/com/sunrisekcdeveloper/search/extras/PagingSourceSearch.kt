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

package com.sunrisekcdeveloper.search.extras

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sunrisekcdeveloper.common.timber
import com.sunrisekcdeveloper.network.NetworkResult
import com.sunrisekcdeveloper.search.SearchRemoteDataSourceContract
import com.sunrisekcdeveloper.search.extras.model.UIModelSearch

class PagingSourceSearch(
    private val remote: SearchRemoteDataSourceContract,
    private val query: String
) : PagingSource<Int, UIModelSearch>() {

    private val log by timber()

    companion object {
        private const val SEARCH_STARTING_PAGE_INDEX = 1
    }

    @ExperimentalPagingApi
    override fun getRefreshKey(state: PagingState<Int, UIModelSearch>): Int? {
        return SEARCH_STARTING_PAGE_INDEX
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UIModelSearch> {

        // On first load key will be null
        val position = params.key ?: SEARCH_STARTING_PAGE_INDEX

        val movieResponse = remote.moviesByTitle(query, position)
        val showResponse = remote.showsByTitle(query, position)

        val result = mutableListOf<UIModelSearch>()
        val nextKey: Int?
        val prevKey: Int?

        if (movieResponse is NetworkResult.Error && showResponse is NetworkResult.Error) {
            return LoadResult.Error(Exception("${movieResponse.exception}  AND ${showResponse.exception}"))
        }

        when (movieResponse) {
            is NetworkResult.Success -> {
                result.addAll(movieResponse.data.media.map { it.asUIModelSearch() })
            }
            is NetworkResult.Error -> {
                // todo dont swallow exceptions
                log.e("Error - movie search call was not successful: ${movieResponse.exception}")
            }
        }
        when (showResponse) {
            is NetworkResult.Success -> {
                result.addAll(showResponse.data.media.map { it.asUIModelSearch() })
            }
            is NetworkResult.Error -> {
                log.e("Error - show search call was not successful: ${showResponse.exception}")
            }
        }

        val organisedList = SortedList(
            FilteredList(
                DefaultList(
                    result
                ),
                predicate = { it.popularity > 10 }),
            comparator = compareByDescending<UIModelSearch> { it.ratingVotes }.thenByDescending { it.rating }
                .thenByDescending { it.popularity }
        ).list()

        nextKey = when {
            // some business logic to improve search experience
            organisedList.isEmpty() || (organisedList.size < 5 && position > 20) -> {
                null
            }
            else -> {
                position + 1
            }
        }

        prevKey = when (position) {
            SEARCH_STARTING_PAGE_INDEX -> null
            else -> position + 1
        }

        return LoadResult.Page(
            data = organisedList,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }
}