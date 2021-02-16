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
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.DiscoveryRepo
import com.sunrisekcdeveloper.showtracker.features.discover.application.LoadPopularMoviesUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.discover.application.LoadTopRatedMoviesUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.discover.application.LoadUpcomingMoviesUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.EnvelopePaginatedMovie
import com.sunrisekcdeveloper.showtracker.features.discover.domain.repository.DiscoveryRepositoryContract
import kotlinx.coroutines.*

/**
 * Home ViewModel
 *
 * @constructor Create empty Home view model
 */
@ExperimentalCoroutinesApi
class DiscoveryViewModel @ViewModelInject constructor(
    private val loadUpcomingMoviesUseCase: LoadUpcomingMoviesUseCaseContract,
    private val loadPopularMoviesUseCase: LoadPopularMoviesUseCaseContract,
    private val loadTopRatedMoviesUseCase: LoadTopRatedMoviesUseCaseContract
) : ViewModel() {

    var popularMoviesPage = 1
    var topRatedMoviesPage = 1
    var upcomingMoviesPage = 1

    private val _popularMovies = MutableLiveData<Resource<EnvelopePaginatedMovie>>()
    val popularMovies: LiveData<Resource<EnvelopePaginatedMovie>>
        get() = _popularMovies

    private val _topRatedMovies = MutableLiveData<Resource<EnvelopePaginatedMovie>>()
    val topRatedMovies: LiveData<Resource<EnvelopePaginatedMovie>>
        get() = _topRatedMovies

    private val _upcomingMovies = MutableLiveData<Resource<EnvelopePaginatedMovie>>()
    val upcomingMovies: LiveData<Resource<EnvelopePaginatedMovie>>
        get() = _upcomingMovies

    init {
        viewModelScope.launch {
            launch { getPopularMovies() }
            launch { getTopRatedMovies() }
            launch { getUpcomingMovies() }
        }
    }

    suspend fun getPopularMovies() = dispatch(_popularMovies) { loadPopularMoviesUseCase(++popularMoviesPage) }
    suspend fun getTopRatedMovies() = dispatch(_topRatedMovies) { loadTopRatedMoviesUseCase(++topRatedMoviesPage) }
    suspend fun getUpcomingMovies() = dispatch(_upcomingMovies) { loadUpcomingMoviesUseCase(++upcomingMoviesPage) }

    private suspend fun dispatch(
        mutableLiveData: MutableLiveData<Resource<EnvelopePaginatedMovie>>,
        call: suspend () -> Resource<EnvelopePaginatedMovie>
    ) {
        val data = withContext(Dispatchers.IO) { call() }
        withContext(Dispatchers.Main) { mutableLiveData.value = data }
    }
}
