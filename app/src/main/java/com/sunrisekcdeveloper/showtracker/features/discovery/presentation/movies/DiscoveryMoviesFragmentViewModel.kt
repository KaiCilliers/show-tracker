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

package com.sunrisekcdeveloper.showtracker.features.discovery.presentation.movies

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.features.discovery.application.LoadPopularMoviesUseCaseContractUpdated
import com.sunrisekcdeveloper.showtracker.features.discovery.application.LoadTopRatedMoviesUseCaseContractUpdated
import com.sunrisekcdeveloper.showtracker.features.discovery.application.LoadUpcomingMoviesUseCaseContractUpdated
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.DiscoveryUIModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscoveryMoviesFragmentViewModel @ViewModelInject constructor(
    private val loadUpcomingMoviesUseCase: LoadUpcomingMoviesUseCaseContractUpdated,
    private val loadPopularMoviesUseCase: LoadPopularMoviesUseCaseContractUpdated,
    private val loadTopRatedMoviesUseCase: LoadTopRatedMoviesUseCaseContractUpdated
) : ViewModel() {

    var popularMoviesPage = 0
    var topRatedMoviesPage = 0
    var upcomingMoviesPage = 0

    private val _popularMovies = MutableLiveData<Resource<List<DiscoveryUIModel>>>()
    val popularMovies: LiveData<Resource<List<DiscoveryUIModel>>>
        get() = _popularMovies

    private val _topRatedMovies = MutableLiveData<Resource<List<DiscoveryUIModel>>>()
    val topRatedMovies: LiveData<Resource<List<DiscoveryUIModel>>>
        get() = _topRatedMovies

    private val _upcomingMovies = MutableLiveData<Resource<List<DiscoveryUIModel>>>()
    val upcomingMovies: LiveData<Resource<List<DiscoveryUIModel>>>
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


    private suspend fun dispatch(
        mutableLiveData: MutableLiveData<Resource<List<DiscoveryUIModel>>>,
        call: suspend () -> Resource<List<DiscoveryUIModel>>
    ) {
        val data = withContext(Dispatchers.IO) { call() }
        withContext(Dispatchers.Main) { mutableLiveData.value = data }
    }
}