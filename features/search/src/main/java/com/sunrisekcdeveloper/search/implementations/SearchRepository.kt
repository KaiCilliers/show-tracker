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
import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.movie.MovieRepositoryContract
import com.sunrisekcdeveloper.search.SearchRemoteDataSourceContract
import com.sunrisekcdeveloper.search.extras.PagingSourceSearch
import com.sunrisekcdeveloper.search.extras.model.UIModelPoster
import com.sunrisekcdeveloper.search.extras.model.UIModelSearch
import com.sunrisekcdeveloper.search.SearchRepositoryContract
import com.sunrisekcdeveloper.search.extras.toUiPosterModel
import com.sunrisekcdeveloper.show.FullTVShow
import com.sunrisekcdeveloper.show.TVShow
import com.sunrisekcdeveloper.show.TVShowRepositoryContract
import com.sunrisekcdeveloper.show.WatchlistTVShowRepositoryContract
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow


class SearchRepository(
    private val remote: SearchRemoteDataSourceContract, // paging makes it difficult to remove this
    private val movieRepo: MovieRepositoryContract, // CONTINUE HERE implement this contract like you did tv show
    private val showRepo: TVShowRepositoryContract
) : SearchRepositoryContract {

    override suspend fun loadUnwatchedMedia(): Resource<List<UIModelPoster>> {
        val movie = movieRepo.unwatched()
        val shows = showRepo.unwatched()

        val list = movie.map { it.toUiPosterModel() } + shows.map { it.toUiPosterModel() }

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
