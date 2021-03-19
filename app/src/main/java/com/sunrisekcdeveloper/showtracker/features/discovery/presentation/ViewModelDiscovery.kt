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

package com.sunrisekcdeveloper.showtracker.features.discovery.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.features.detail.domain.repository.RepositoryDetailContract
import com.sunrisekcdeveloper.showtracker.features.discovery.application.*
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ViewActionsDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ViewStateDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.repository.RepositoryDiscoveryContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelDiscovery @ViewModelInject constructor(
    private val loadUpcomingMoviesUseCase: LoadUpcomingMoviesUseCaseContractUpdated,
    private val loadPopularMoviesUseCase: LoadPopularMoviesUseCaseContract,
    private val loadTopRatedMoviesUseCase: LoadTopRatedMoviesUseCaseContract,
    private val loadPopularShowsUseCase: LoadPopularShowsUseCaseContract,
    private val loadTopRatedShowsUseCase: LoadTopRatedShowsUseCaseContract,
    private val loadAiringTodayShowsUseCase: LoadAiringTodayShowsUseCaseContract,
    private val repoTemp: RepositoryDiscoveryContract
) : ViewModel() {

    // todo try not use nullables (perhaps lateinit with and init{ }?)
    private var currentSearchResult: Flow<PagingData<UIModelDiscovery>>? = null

    // todo this can be improved
    fun popularMoviesStream(): Flow<PagingData<UIModelDiscovery>> {
        val lastResult = currentSearchResult

        val newResult = repoTemp.popularMoviesStream()
            .cachedIn(viewModelScope)

        currentSearchResult = newResult

        return newResult
    }

    // todo implement paging 3
    var popularMoviesPage = 0
    var topRatedMoviesPage = 0
    var upcomingMoviesPage = 0
    var popularShowsPage = 0
    var topRatedShowsPage = 0
    var latestShowsPage = 0

    private val _state = MutableLiveData<ViewStateDiscovery>().apply {
        value = ViewStateDiscovery.Loading
    }
    val state: LiveData<ViewStateDiscovery> = _state

    private val _popularMovies = MutableLiveData<Resource<List<UIModelDiscovery>>>()
    val popularMovies: LiveData<Resource<List<UIModelDiscovery>>>
        get() = _popularMovies

    private val _topRatedMovies = MutableLiveData<Resource<List<UIModelDiscovery>>>()
    val topRatedMovies: LiveData<Resource<List<UIModelDiscovery>>>
        get() = _topRatedMovies

    private val _upcomingMovies = MutableLiveData<Resource<List<UIModelDiscovery>>>()
    val upcomingMovies: LiveData<Resource<List<UIModelDiscovery>>>
        get() = _upcomingMovies

    private val _popularShows = MutableLiveData<Resource<List<UIModelDiscovery>>>()
    val popularShows: LiveData<Resource<List<UIModelDiscovery>>>
        get() = _popularShows

    private val _topRatedShows = MutableLiveData<Resource<List<UIModelDiscovery>>>()
    val topRatedShows: LiveData<Resource<List<UIModelDiscovery>>>
        get() = _topRatedShows

    private val _airingTodayShows = MutableLiveData<Resource<List<UIModelDiscovery>>>()
    val airingTodayShows: LiveData<Resource<List<UIModelDiscovery>>>
        get() = _airingTodayShows

    fun performAction(action: ViewActionsDiscovery) = viewModelScope.launch {
        when (action) {
            ViewActionsDiscovery.FetchMovieAndShowData -> {
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
        mutableLiveData: MutableLiveData<Resource<List<UIModelDiscovery>>>,
        call: suspend () -> Resource<List<UIModelDiscovery>>
    ) {
        val data = withContext(Dispatchers.IO) { call() }
        withContext(Dispatchers.Main) { mutableLiveData.value = data }
    }
}