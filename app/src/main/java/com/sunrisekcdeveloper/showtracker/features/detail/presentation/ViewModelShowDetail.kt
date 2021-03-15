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

package com.sunrisekcdeveloper.showtracker.features.detail.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.features.detail.application.AddShowToWatchlistUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.detail.application.FetchShowDetailsUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.detail.application.RemoveShowFromWatchlistUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelShowDetail
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ViewModelShowDetail @ViewModelInject constructor(
    private val fetchShowDetailsUseCase: FetchShowDetailsUseCaseContract,
    private val addShowToWatchlistUseCase: AddShowToWatchlistUseCaseContract,
    private val removeShowFromWatchlistUseCase: RemoveShowFromWatchlistUseCaseContract
) : ViewModel() {
    private val _showDetails = MutableLiveData<Resource<UIModelShowDetail>>()
    val showDetails: LiveData<Resource<UIModelShowDetail>>
        get() = _showDetails

    fun showDetails(id: String) = viewModelScope.launch {
        fetchShowDetailsUseCase(id).collect {
            _showDetails.value = it
        }
    }

    fun removeShowFromWatchlist(showId: String) = viewModelScope.launch {
        removeShowFromWatchlistUseCase(showId)
    }

    fun addShowToWatchlist(showId: String) = viewModelScope.launch {
        addShowToWatchlistUseCase(showId)
    }

    fun updateShowProgress(showId: String) = viewModelScope.launch {

    }

}