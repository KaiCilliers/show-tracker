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
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.WatchListType
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.MediaModelSealed
import kotlinx.coroutines.launch
import java.util.*

/**
 * Progress ViewModel
 *
 * @constructor Create empty Progress view model
 */
class WatchlistViewModel @ViewModelInject constructor(
    private val loadWatchListMediaUseCase: LoadWatchListMediaUseCaseContract
) : ViewModel() {

    private val _watchListMedia = MutableLiveData<Resource<Map<WatchListType, MutableList<MediaModelSealed>>>>()
    val watchListMedia: LiveData<Resource<Map<WatchListType, MutableList<MediaModelSealed>>>>
        get() = _watchListMedia

    init {
        getWatchListMedia()
    }

    private fun getWatchListMedia() = viewModelScope.launch {
        val data = loadWatchListMediaUseCase()
        val results = mutableMapOf<WatchListType, MutableList<MediaModelSealed>>()
        when (data) {
            is Resource.Loading -> {}
            is Resource.Error -> {}
            is Resource.Success -> {
                data.data.forEach {
                    when (it.watchListType) {
                        WatchListType.RECENTLY_ADDED -> {
                            results.getOrPut(WatchListType.RECENTLY_ADDED, { mutableListOf() }).add(it)
                        }
                        WatchListType.IN_PROGRESS -> {
                            results.getOrPut(WatchListType.IN_PROGRESS, { mutableListOf() }).add(it)
                        }
                        WatchListType.UPCOMING -> {
                            results.getOrPut(WatchListType.UPCOMING, { mutableListOf() }).add(it)
                        }
                        WatchListType.COMPLETED -> {
                            results.getOrPut(WatchListType.COMPLETED, { mutableListOf() }).add(it)
                        }
                        WatchListType.ANTICIPATED -> {
                            results.getOrPut(WatchListType.ANTICIPATED, { mutableListOf() }).add(it)
                        }
                        else -> {
                        }
                    }
                }
            }
        }
        _watchListMedia.postValue(Resource.Success(results))
    }
}