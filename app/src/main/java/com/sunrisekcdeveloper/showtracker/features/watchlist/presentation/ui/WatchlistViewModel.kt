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
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.WatchlistRepo
import com.sunrisekcdeveloper.showtracker.features.discover.data.local.model.RecentlyAddedMediaEntity
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.EnvelopePaginatedMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.application.LoadRecentlyAddedMediaUseCaseContract
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository.WatchListRepositoryContract
import com.sunrisekcdeveloper.showtracker.models.FeaturedList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Progress ViewModel
 *
 * @constructor Create empty Progress view model
 */
class WatchlistViewModel @ViewModelInject constructor(
    private val loadRecentlyAddedMediaUseCase: LoadRecentlyAddedMediaUseCaseContract
) : ViewModel() {
    private val _recentlyAddedMovies = MutableLiveData<Resource<List<RecentlyAddedMediaEntity>>>()
    val recentlyAddedMovies: LiveData<Resource<List<RecentlyAddedMediaEntity>>>
        get() = _recentlyAddedMovies

    init {
        getRecentlyAddedMedia()
    }

    fun getRecentlyAddedMedia() = viewModelScope.launch {
        val data = loadRecentlyAddedMediaUseCase()
        _recentlyAddedMovies.value = data
    }
}