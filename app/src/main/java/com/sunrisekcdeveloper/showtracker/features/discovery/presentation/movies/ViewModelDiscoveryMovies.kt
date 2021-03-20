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
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.RepoDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.application.LoadPopularMoviesUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.discovery.application.LoadTopRatedMoviesUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.discovery.application.LoadUpcomingMoviesUseCaseContractUpdated
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.repository.RepositoryDiscoveryContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// todo use usecases to load data and not repository directly
@ExperimentalCoroutinesApi
class ViewModelDiscoveryMovies @ViewModelInject constructor(
    private val loadUpcomingMoviesUseCase: LoadUpcomingMoviesUseCaseContractUpdated,
    private val loadPopularMoviesUseCase: LoadPopularMoviesUseCaseContract,
    private val loadTopRatedMoviesUseCase: LoadTopRatedMoviesUseCaseContract,
    @RepoDiscovery repo: RepositoryDiscoveryContract
) : ViewModel() {

    val streamPopularMovies: Flow<PagingData<UIModelDiscovery>> = repo.popularMoviesStream()
        .cachedIn(viewModelScope)
    val streamTopRatedMovies: Flow<PagingData<UIModelDiscovery>> = repo.topRatedMoviesStream()
        .cachedIn(viewModelScope)
    val streamUpcomingMovies: Flow<PagingData<UIModelDiscovery>> = repo.upcomingMoviesStream()
        .cachedIn(viewModelScope)
}