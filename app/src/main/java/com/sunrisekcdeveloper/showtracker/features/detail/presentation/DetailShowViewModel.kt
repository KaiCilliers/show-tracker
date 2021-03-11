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

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.updated.features.detail.application.FetchShowDetailsUseCaseContract
import com.sunrisekcdeveloper.showtracker.updated.features.detail.domain.model.ShowDetailUIModel
import kotlinx.coroutines.launch

class DetailShowViewModel @ViewModelInject constructor(
    private val fetchShowDetailsUseCase: FetchShowDetailsUseCaseContract
) : ViewModel() {
    private val _showDetails = MutableLiveData<Resource<ShowDetailUIModel>>()
    val showDetails: LiveData<Resource<ShowDetailUIModel>>
        get() = _showDetails

    fun showDetails(id: String) = viewModelScope.launch {
        _showDetails.value = fetchShowDetailsUseCase(id)!!
    }
}