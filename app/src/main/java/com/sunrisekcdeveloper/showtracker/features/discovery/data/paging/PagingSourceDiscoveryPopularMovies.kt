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

package com.sunrisekcdeveloper.showtracker.features.discovery.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sunrisekcdeveloper.showtracker.common.NetworkResult
import com.sunrisekcdeveloper.showtracker.common.util.asUIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.RemoteDataSourceDiscoveryContract
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ListType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import java.io.IOException

// todo this class remains as an example due to inexperience in Paging 3
class PagingSourceDiscoveryPopularMovies(
    private val remote: RemoteDataSourceDiscoveryContract
) : PagingSource<Int, UIModelDiscovery>() {

    companion object {
        private const val TMDB_STARTING_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, UIModelDiscovery>): Int? {
        return TMDB_STARTING_PAGE_INDEX
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UIModelDiscovery> {
        val position = params.key ?: TMDB_STARTING_PAGE_INDEX

        val response = remote.popularMovies(position)

        return when (response) {
            is NetworkResult.Success -> {

                val nextKey = when {
                    response.data.media.isEmpty() -> { null }
                    else -> { position + 1 }
                }

                val prevKey = when(position) {
                    TMDB_STARTING_PAGE_INDEX -> null
                    else -> position + 1
                }

                val uiModelList = response.data.media.map { it.asUIModelDiscovery(ListType.MoviePopular) }

                LoadResult.Page(
                    data = uiModelList,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
            is NetworkResult.Error -> {
                // todo NetworkResult.Error needs to encapsulate a throwable
                LoadResult.Error(IOException(response.message))
            }
        }

    }
}