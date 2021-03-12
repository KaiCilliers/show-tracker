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
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.search.application.SearchMediaByTitleUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.search.domain.domain.UIModelSearch
import com.sunrisekcdeveloper.showtracker.features.search.domain.domain.ViewStateSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ViewModelSearch @ViewModelInject constructor(
    private val searchMediaByTitleUseCase: SearchMediaByTitleUseCaseContract
) : ViewModel() {

    var mediaPage = 0
    var lastQuery = ""

    private val _results = MutableLiveData<Resource<List<UIModelSearch>>>()
    val results: LiveData<Resource<List<UIModelSearch>>>
        get() = _results

    fun getSearchResults(query: String) = viewModelScope.launch {
        Timber.e("inside viemodel get search results...")
        // reset page
        if (lastQuery != query) {
            mediaPage = 0
            lastQuery = query
        }
        getNextPage()
    }

    fun getNextPage() = viewModelScope.launch {
        Timber.e("inside viewmodel get next page...")
        searchMediaByTitleUseCase(++mediaPage, lastQuery).collect {
            _results.value = it
        }
    }
}
