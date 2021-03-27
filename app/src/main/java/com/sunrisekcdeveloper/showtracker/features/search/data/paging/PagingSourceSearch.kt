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

package com.sunrisekcdeveloper.showtracker.features.search.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sunrisekcdeveloper.showtracker.common.NetworkResult
import com.sunrisekcdeveloper.showtracker.features.search.data.network.RemoteDataSourceSearchContract
import com.sunrisekcdeveloper.showtracker.features.search.data.repository.asUIModelSearch
import com.sunrisekcdeveloper.showtracker.features.search.data.repository.asUIModelSearchh
import com.sunrisekcdeveloper.showtracker.features.search.domain.model.UIModelSearch
import timber.log.Timber
import java.io.IOException

class PagingSourceSearch(
    private val remote: RemoteDataSourceSearchContract,
    private val query: String
) : PagingSource<Int, UIModelSearch>() {

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
                result.addAll(movieResponse.data.media.asUIModelSearch())
            }
            is NetworkResult.Error -> {
                // todo dont swallow exceptions
                Timber.d("Error - movie search call was not successful: ${movieResponse.exception}")
            }
        }
        when (showResponse) {
            is NetworkResult.Success -> {
                result.addAll(showResponse.data.media.asUIModelSearchh())
            }
            is NetworkResult.Error -> {
                Timber.d("Error - show search call was not successful: ${showResponse.exception}")
            }
        }

        // todo this can be done nicer
        val filtered = result.filter { it.posterPath != "" }
            .filter { it.popularity > 10 } // attempt to filer out the bulk of unappropriated items
        val sorted = filtered.sortedWith(compareByDescending<UIModelSearch>
        { it.ratingVotes }.thenByDescending { it.rating }.thenByDescending { it.popularity }
        )

        nextKey = when {
            result.isEmpty() -> {
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
            data = sorted,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }
}