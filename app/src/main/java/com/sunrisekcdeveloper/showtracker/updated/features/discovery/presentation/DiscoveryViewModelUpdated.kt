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

package com.sunrisekcdeveloper.showtracker.updated.features.discovery.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.application.*
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.domain.model.DiscoveryUIModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscoveryViewModelUpdated @ViewModelInject constructor(
    private val loadUpcomingMoviesUseCase: LoadUpcomingMoviesUseCaseContractUpdated,
    private val loadPopularMoviesUseCase: LoadPopularMoviesUseCaseContractUpdated,
    private val loadTopRatedMoviesUseCase: LoadTopRatedMoviesUseCaseContractUpdated,
    private val loadPopularShowsUseCase: LoadPopularShowsUseCaseContractUpdated,
    private val loadTopRatedShowsUseCase: LoadTopRatedShowsUseCaseContractUpdated,
    private val loadAiringTodayShowsUseCase: LoadAiringTodayShowsUseCaseContractUpdated
) : ViewModel() {

    // todo implement paging 3
    var popularMoviesPage = 0
    var topRatedMoviesPage = 0
    var upcomingMoviesPage = 0
    var popularShowsPage = 0
    var topRatedShowsPage = 0
    var latestShowsPage = 0

    private val _state = MutableLiveData<DiscoveryViewState>().apply {
        value = DiscoveryViewState.Loading
    }
    val state: LiveData<DiscoveryViewState> = _state

    private val _popularMovies = MutableLiveData<Resource<List<DiscoveryUIModel>>>()
    val popularMovies: LiveData<Resource<List<DiscoveryUIModel>>>
        get() = _popularMovies

    private val _topRatedMovies = MutableLiveData<Resource<List<DiscoveryUIModel>>>()
    val topRatedMovies: LiveData<Resource<List<DiscoveryUIModel>>>
        get() = _topRatedMovies

    private val _upcomingMovies = MutableLiveData<Resource<List<DiscoveryUIModel>>>()
    val upcomingMovies: LiveData<Resource<List<DiscoveryUIModel>>>
        get() = _upcomingMovies

    private val _popularShows = MutableLiveData<Resource<List<DiscoveryUIModel>>>()
    val popularShows: LiveData<Resource<List<DiscoveryUIModel>>>
        get() = _popularShows

    private val _topRatedShows = MutableLiveData<Resource<List<DiscoveryUIModel>>>()
    val topRatedShows: LiveData<Resource<List<DiscoveryUIModel>>>
        get() = _topRatedShows

    private val _airingTodayShows = MutableLiveData<Resource<List<DiscoveryUIModel>>>()
    val airingTodayShows: LiveData<Resource<List<DiscoveryUIModel>>>
        get() = _airingTodayShows

    fun performAction(action: DiscoveryViewActions) = viewModelScope.launch {
        when (action) {
            DiscoveryViewActions.FetchMovieAndShowData -> {
                viewModelScope.launch {
                    launch { getPopularMovies() }
                    launch { getTopRatedMovies() }
                    launch { getUpcomingMovies() }
                    launch { getPopularShows() }
                    launch { getTopRatedShows() }
                    launch { getAiringTodayShows() }
                }
            }
        }
    }

    fun getPopularMovies() = viewModelScope.launch {
        dispatch(_popularMovies) { loadPopularMoviesUseCase(++popularMoviesPage) }
    }
    fun getTopRatedMovies() = viewModelScope.launch {
        dispatch(_topRatedMovies) { loadTopRatedMoviesUseCase(++topRatedMoviesPage) }
    }
    fun getUpcomingMovies() = viewModelScope.launch {
        dispatch(_upcomingMovies) { loadUpcomingMoviesUseCase(++upcomingMoviesPage) }
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

sealed class DiscoveryViewActions {
    object FetchMovieAndShowData : DiscoveryViewActions()
}

sealed class DiscoveryViewState {
    object Loading : DiscoveryViewState()
    object Error : DiscoveryViewState()
    object Success : DiscoveryViewState()
}