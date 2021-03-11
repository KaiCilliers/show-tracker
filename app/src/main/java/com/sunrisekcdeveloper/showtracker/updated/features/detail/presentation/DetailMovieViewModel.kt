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

package com.sunrisekcdeveloper.showtracker.updated.features.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.updated.features.detail.application.FetchMovieDetailsUseCaseContract
import com.sunrisekcdeveloper.showtracker.updated.features.detail.domain.model.MovieDetailUIModel
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.application.LoadAiringTodayShowsUseCaseContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.application.LoadPopularShowsUseCaseContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.application.LoadTopRatedShowsUseCaseContractUpdated
import kotlinx.coroutines.launch

class DetailMovieViewModel @ViewModelInject constructor(
    private val fetchMovieDetailsUseCase: FetchMovieDetailsUseCaseContract
) : ViewModel() {
    private val _movieDetails = MutableLiveData<Resource<MovieDetailUIModel>>()
    val movieDetails: LiveData<Resource<MovieDetailUIModel>>
        get() = _movieDetails

    fun movieDetails(id: String) = viewModelScope.launch {
        _movieDetails.value = fetchMovieDetailsUseCase(id)!!
    }
}