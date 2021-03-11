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

package com.sunrisekcdeveloper.showtracker.updated.features.search.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.showtracker.common.util.Resource
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.domain.model.MediaTypeUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.search.application.SearchMediaByTitleUseCaseContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel @ViewModelInject constructor(
    private val searchMediaByTitleUseCase: SearchMediaByTitleUseCaseContract
) : ViewModel() {

    var mediaPage = 0

    private val _searchState = MutableLiveData<SearchState<List<SearchUIModel>>>().apply {
        value = SearchState.SuggestedContent
    }
    val searchState: LiveData<SearchState<List<SearchUIModel>>> = _searchState

    fun setState(state: SearchState<List<SearchUIModel>>) {
        viewModelScope.launch {
            when(state) {
                SearchState.SuggestedContent -> _searchState.value = state
                SearchState.NoSearchResults -> _searchState.value = state
                is SearchState.SearchResults -> _searchState.value = state
            }
        }
    }

    fun getSearchResults(query: String, newQuery: Boolean) = viewModelScope.launch {
        // reset page
        if (newQuery) {
            mediaPage = 0
        }
        dispatch(_searchState) { searchMediaByTitleUseCase(++mediaPage, query) }
    }

    private suspend fun dispatch(
        mutableLiveData: MutableLiveData<SearchState<List<SearchUIModel>>>,
        call: suspend () -> Resource<List<SearchUIModel>>
    ) {
        val data = withContext(Dispatchers.IO) { call() }
        withContext(Dispatchers.Main) {
            if (data is Resource.Success) {
                mutableLiveData.value = SearchState.SearchResults(data.data)
            } else {
                mutableLiveData.value = SearchState.SearchResults(listOf())
            }
        }
    }
}

sealed class SearchState<out T: List<SearchUIModel>> {
    object SuggestedContent : SearchState<Nothing>()
    object NoSearchResults : SearchState<Nothing>()
    data class SearchResults(val data: List<SearchUIModel>) : SearchState<List<SearchUIModel>>()
}

data class SearchUIModel(
    val id: String,
    val title: String,
    val posterPath: String,
    val mediaType: MediaTypeUpdated
)
