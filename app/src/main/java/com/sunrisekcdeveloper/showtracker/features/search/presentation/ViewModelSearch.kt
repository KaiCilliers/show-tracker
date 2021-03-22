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

package com.sunrisekcdeveloper.showtracker.features.search.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.common.util.asUIModelPoster
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.RepoSearch
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ListType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.search.application.SearchMediaByTitleUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.search.domain.domain.UIModelSearch
import com.sunrisekcdeveloper.showtracker.features.search.domain.domain.ViewStateSearch
import com.sunrisekcdeveloper.showtracker.features.search.domain.repository.RepositorySearchContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@ExperimentalCoroutinesApi
class ViewModelSearch @ViewModelInject constructor(
    private val searchMediaByTitleUseCase: SearchMediaByTitleUseCaseContract,
    @RepoSearch private val repo: RepositorySearchContract
) : ViewModel() {

    private var currentQuery: String? = null

    private var currentSearchResult: Flow<PagingData<UIModelDiscovery>>? = null

    // todo returning different UIModel due to pagingadapter requirements
    //  the adapter needs to have its own data type
    fun searchMedia(query: String): Flow<PagingData<UIModelDiscovery>> {
        val lastResult = currentSearchResult
        if (query == currentQuery && lastResult != null) {
            return lastResult
        }
        currentQuery = query
        val newResult: Flow<PagingData<UIModelDiscovery>> = repo.searchMediaByTitlePage(query)
            .map {
                it.map { model ->
                    model.asUIModelDiscovery()
                }
            }
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}

// todo this is temp fix (adapter needs its own data type)
private fun UIModelSearch.asUIModelDiscovery() = UIModelDiscovery(
    id = id,
    mediaType = mediaType,
    listType = ListType.MoviePopular,
    posterPath = posterPath
)