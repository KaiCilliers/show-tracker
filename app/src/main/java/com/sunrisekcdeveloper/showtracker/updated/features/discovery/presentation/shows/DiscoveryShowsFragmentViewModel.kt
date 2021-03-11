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

package com.sunrisekcdeveloper.showtracker.updated.features.discovery.presentation.shows

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.showtracker.common.util.Resource
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.application.LoadAiringTodayShowsUseCaseContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.application.LoadPopularShowsUseCaseContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.application.LoadTopRatedShowsUseCaseContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.domain.model.DiscoveryUIModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscoveryShowsFragmentViewModel @ViewModelInject constructor(
    private val loadPopularShowsUseCase: LoadPopularShowsUseCaseContractUpdated,
    private val loadTopRatedShowsUseCase: LoadTopRatedShowsUseCaseContractUpdated,
    private val loadAiringTodayShowsUseCase: LoadAiringTodayShowsUseCaseContractUpdated
) : ViewModel() {

    var popularShowsPage = 0
    var topRatedShowsPage = 0
    var latestShowsPage = 0

    private val _popularShows = MutableLiveData<Resource<List<DiscoveryUIModel>>>()
    val popularShows: LiveData<Resource<List<DiscoveryUIModel>>>
        get() = _popularShows

    private val _topRatedShows = MutableLiveData<Resource<List<DiscoveryUIModel>>>()
    val topRatedShows: LiveData<Resource<List<DiscoveryUIModel>>>
        get() = _topRatedShows

    private val _airingTodayShows = MutableLiveData<Resource<List<DiscoveryUIModel>>>()
    val airingTodayShows: LiveData<Resource<List<DiscoveryUIModel>>>
        get() = _airingTodayShows

    init {
        viewModelScope.launch {
            launch { getPopularShows() }
            launch { getTopRatedShows() }
            launch { getAiringTodayShows() }
        }
    }

    fun getPopularShows() = viewModelScope.launch {
        dispatch(_popularShows) { loadPopularShowsUseCase(++popularShowsPage) }
    }
    fun getTopRatedShows() = viewModelScope.launch {
        dispatch(_topRatedShows) { loadTopRatedShowsUseCase(++topRatedShowsPage) }
    }
    fun getAiringTodayShows() = viewModelScope.launch {
        dispatch(_airingTodayShows) { loadAiringTodayShowsUseCase(++latestShowsPage) }
    }

    private suspend fun dispatch(
        mutableLiveData: MutableLiveData<Resource<List<DiscoveryUIModel>>>,
        call: suspend () -> Resource<List<DiscoveryUIModel>>
    ) {
        val data = withContext(Dispatchers.IO) { call() }
        withContext(Dispatchers.Main) { mutableLiveData.value = data }
    }
}