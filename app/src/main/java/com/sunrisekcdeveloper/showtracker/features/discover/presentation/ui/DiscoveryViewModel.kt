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

package com.sunrisekcdeveloper.showtracker.features.discover.presentation.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.features.discover.application.LoadPopularMoviesUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.discover.application.LoadTopRatedMoviesUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.discover.application.LoadUpcomingMoviesUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.discover.application.SaveMediaToWatchListUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.MediaModelSealed
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * Home ViewModel
 *
 * @constructor Create empty Home view model
 */
@ExperimentalCoroutinesApi
class DiscoveryViewModel @ViewModelInject constructor(
    private val loadUpcomingMoviesUseCase: LoadUpcomingMoviesUseCaseContract,
    private val loadPopularMoviesUseCase: LoadPopularMoviesUseCaseContract,
    private val loadTopRatedMoviesUseCase: LoadTopRatedMoviesUseCaseContract,
    private val saveMediaToWatchListUseCase: SaveMediaToWatchListUseCaseContract
) : ViewModel() {

    var popularMoviesPage = 1
    var topRatedMoviesPage = 1
    var upcomingMoviesPage = 1

    private val _popularMovies = MutableLiveData<Resource<List<MediaModelSealed>>>()
    val popularMovies: LiveData<Resource<List<MediaModelSealed>>>
        get() = _popularMovies

    private val _topRatedMovies = MutableLiveData<Resource<List<MediaModelSealed>>>()
    val topRatedMovies: LiveData<Resource<List<MediaModelSealed>>>
        get() = _topRatedMovies

    private val _upcomingMovies = MutableLiveData<Resource<List<MediaModelSealed>>>()
    val upcomingMovies: LiveData<Resource<List<MediaModelSealed>>>
        get() = _upcomingMovies

    init {
        viewModelScope.launch {
            launch { getPopularMovies() }
            launch { getTopRatedMovies() }
            launch { getUpcomingMovies() }
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

    fun addMediaToRecentlyAdded(media: MediaModelSealed) = viewModelScope.launch {
        saveMediaToWatchListUseCase(media)
    }

    private suspend fun dispatch(
        mutableLiveData: MutableLiveData<Resource<List<MediaModelSealed>>>,
        call: suspend () -> Resource<List<MediaModelSealed>>
    ) {
        val data = withContext(Dispatchers.IO) { call() }
        withContext(Dispatchers.Main) { mutableLiveData.value = data }
    }
}
