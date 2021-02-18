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

package com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sunrisekcdeveloper.showtracker.commons.models.local.*
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.features.watchlist.application.*
import kotlinx.coroutines.launch

/**
 * Progress ViewModel
 *
 * @constructor Create empty Progress view model
 */
class WatchlistViewModel @ViewModelInject constructor(
    private val loadRecentlyAddedMediaUseCase: LoadRecentlyAddedMediaUseCaseContract,
    private val loadInProgressMediaUseCase: LoadInProgressMediaUseCaseContract,
    private val loadUpcomingMediaUseCase: LoadUpcomingMediaUseCaseContract,
    private val loadCompletedMediaUseCase: LoadCompletedMediaUseCaseContract,
    private val loadAnticipatedMediaUseCase: LoadAnticipatedMediaUseCaseContract
) : ViewModel() {
    private val _recentlyAddedMedia = MutableLiveData<Resource<List<RecentlyAddedMediaEntity>>>()
    val recentlyAddedMedia: LiveData<Resource<List<RecentlyAddedMediaEntity>>>
        get() = _recentlyAddedMedia

    private val _inProgressMedia = MutableLiveData<Resource<List<InProgressMediaEntity>>>()
    val inProgressMedia: LiveData<Resource<List<InProgressMediaEntity>>>
        get() = _inProgressMedia

    private val _upcomingMedia = MutableLiveData<Resource<List<UpcomingMediaEntity>>>()
    val upcomingMedia: LiveData<Resource<List<UpcomingMediaEntity>>>
        get() = _upcomingMedia

    private val _completedMedia = MutableLiveData<Resource<List<CompletedMediaEntity>>>()
    val completedMedia: LiveData<Resource<List<CompletedMediaEntity>>>
        get() = _completedMedia

    private val _anticipatedMedia = MutableLiveData<Resource<List<AnticipatedMediaEntity>>>()
    val anticipatedMedia: LiveData<Resource<List<AnticipatedMediaEntity>>>
        get() = _anticipatedMedia

    init {
        getRecentlyAddedMedia()
        getInProgressMedia()
        getUpcomingMedia()
        getCompletedMedia()
        getAnticipatedMedia()
    }

    private fun getRecentlyAddedMedia() = viewModelScope.launch {
        val data = loadRecentlyAddedMediaUseCase()
        _recentlyAddedMedia.value = data
    }

    private fun getInProgressMedia() = viewModelScope.launch {
        val data = loadInProgressMediaUseCase()
        _inProgressMedia.value = data
    }

    private fun getUpcomingMedia() = viewModelScope.launch {
        val data = loadUpcomingMediaUseCase()
        _upcomingMedia.value = data
    }

    private fun getCompletedMedia() = viewModelScope.launch {
        val data = loadCompletedMediaUseCase()
        _completedMedia.value = data
    }

    private fun getAnticipatedMedia() = viewModelScope.launch {
        val data = loadAnticipatedMediaUseCase()
        _anticipatedMedia.value = data
    }
}