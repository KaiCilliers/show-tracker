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

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sunrisekcdeveloper.showtracker.di.ModuleRepository.RepoDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.application.*
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.repository.RepositoryDiscoveryContract
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

// todo use usecases to load data and not repository directly
@ExperimentalCoroutinesApi
class ViewModelDiscovery @ViewModelInject constructor(
    private val loadUpcomingMoviesUseCase: LoadUpcomingMoviesUseCaseContractUpdated,
    private val loadPopularMoviesUseCase: LoadPopularMoviesUseCaseContract,
    private val loadTopRatedMoviesUseCase: LoadTopRatedMoviesUseCaseContract,
    private val loadPopularShowsUseCase: LoadPopularShowsUseCaseContract,
    private val loadTopRatedShowsUseCase: LoadTopRatedShowsUseCaseContract,
    private val loadAiringTodayShowsUseCase: LoadAiringTodayShowsUseCaseContract,
    @RepoDiscovery private val repoTemp: RepositoryDiscoveryContract,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val streamPopularMovies: Flow<PagingData<UIModelDiscovery>> = repoTemp.popularMoviesStream()
        .cachedIn(viewModelScope)
    val streamPopularShows: Flow<PagingData<UIModelDiscovery>> = repoTemp.popularShowsStream()
        .cachedIn(viewModelScope)
    val streamTopRatedMovies: Flow<PagingData<UIModelDiscovery>> = repoTemp.topRatedMoviesStream()
        .cachedIn(viewModelScope)
    val streamTopRatedShows: Flow<PagingData<UIModelDiscovery>> = repoTemp.topRatedShowsStream()
        .cachedIn(viewModelScope)
    val streamUpcomingMovies: Flow<PagingData<UIModelDiscovery>> = repoTemp.upcomingMoviesStream()
        .cachedIn(viewModelScope)
    val streamAiringTodayShows: Flow<PagingData<UIModelDiscovery>> = repoTemp.airingTodayShowsStream()
        .cachedIn(viewModelScope)
}