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
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.features.search.application.SearchMediaByTitleUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.search.domain.domain.UIModelSearch
import com.sunrisekcdeveloper.showtracker.features.search.domain.domain.ViewStateSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelSearch @ViewModelInject constructor(
    private val searchMediaByTitleUseCase: SearchMediaByTitleUseCaseContract
) : ViewModel() {

    var mediaPage = 0

    private val _searchState = MutableLiveData<ViewStateSearch<List<UIModelSearch>>>().apply {
        value = ViewStateSearch.SuggestedContent
    }
    val searchState: LiveData<ViewStateSearch<List<UIModelSearch>>> = _searchState

    fun setState(state: ViewStateSearch<List<UIModelSearch>>) {
        viewModelScope.launch {
            when(state) {
                ViewStateSearch.SuggestedContent -> _searchState.value = state
                ViewStateSearch.NoSearchResults -> _searchState.value = state
                is ViewStateSearch.SearchResults -> _searchState.value = state
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
        mutableLiveData: MutableLiveData<ViewStateSearch<List<UIModelSearch>>>,
        call: suspend () -> Resource<List<UIModelSearch>>
    ) {
        val data = withContext(Dispatchers.IO) { call() }
        withContext(Dispatchers.Main) {
            if (data is Resource.Success) {
                mutableLiveData.value = ViewStateSearch.SearchResults(data.data)
            } else {
                mutableLiveData.value = ViewStateSearch.SearchResults(listOf())
            }
        }
    }
}
