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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sunrisekcdeveloper.showtracker.common.TrackerDatabase
import com.sunrisekcdeveloper.showtracker.common.util.NetworkResult
import com.sunrisekcdeveloper.showtracker.common.util.Resource
import com.sunrisekcdeveloper.showtracker.common.util.asUIModelSearch
import com.sunrisekcdeveloper.showtracker.common.util.asUiModelPosterResult
import com.sunrisekcdeveloper.showtracker.features.search.data.network.RemoteDataSourceSearchContract
import com.sunrisekcdeveloper.showtracker.features.search.data.paging.PagingSourceSearch
import com.sunrisekcdeveloper.showtracker.features.search.data.util.organisedList.DefaultList
import com.sunrisekcdeveloper.showtracker.features.search.data.util.organisedList.FilteredList
import com.sunrisekcdeveloper.showtracker.features.search.data.util.organisedList.SortedList
import com.sunrisekcdeveloper.showtracker.features.search.domain.model.UIModelPoster
import com.sunrisekcdeveloper.showtracker.features.search.domain.model.UIModelSearch
import com.sunrisekcdeveloper.showtracker.features.search.domain.repository.RepositorySearchContract
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class RepositorySearch(
    private val remote: RemoteDataSourceSearchContract,
    private val database: TrackerDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RepositorySearchContract {

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