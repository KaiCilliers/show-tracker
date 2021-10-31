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

package com.sunrisekcdeveloper.search.implementations

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.search.extras.asUiModelPosterResult
import com.sunrisekcdeveloper.search.SearchRemoteDataSourceContract
import com.sunrisekcdeveloper.search.extras.PagingSourceSearch
import com.sunrisekcdeveloper.search.extras.model.UIModelPoster
import com.sunrisekcdeveloper.search.extras.model.UIModelSearch
import com.sunrisekcdeveloper.search.SearchRepositoryContract
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow


class SearchRepository(
    private val remote: SearchRemoteDataSourceContract,
    private val database: TrackerDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SearchRepositoryContract {

    override suspend fun loadUnwatchedMedia(): Resource<List<UIModelPoster>> {
        val movie = database.watchlistMovieDao().unwatched()
        val shows = database.watchlistShowDao().unwatched()

        val list =
            movie.map { it.asUiModelPosterResult() } + shows.map { it.asUiModelPosterResult() }

        return Resource.Success(list.sortedBy { it.title })
    }

    override fun searchMediaByTitlePage(query: String): Flow<PagingData<UIModelSearch>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = 40,
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSourceSearch(remote, query) }
        ).flow
    }
}