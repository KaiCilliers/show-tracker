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

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sunrisekcdeveloper.showtracker.commons.models.local.*
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.features.watchlist.application.*
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.WatchListType
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.MediaModelSealed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

/**
 * Progress ViewModel
 *
 * @constructor Create empty Progress view model
 */
@ExperimentalCoroutinesApi
class WatchlistViewModel @ViewModelInject constructor(
    private val loadWatchListMediaUseCase: LoadWatchListMediaUseCaseContract
//    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var media: MutableMap<WatchListType, MutableList<MediaModelSealed>> = mutableMapOf()

    private var _watchlistMedia =
        MutableLiveData<Resource<MutableMap<WatchListType, MutableList<MediaModelSealed>>>>()

    @ExperimentalCoroutinesApi
    val watchlistMedia: LiveData<Resource<MutableMap<WatchListType, MutableList<MediaModelSealed>>>>
        get() = _watchlistMedia

    init {
        viewModelScope.launch {
            temp().collect {
                _watchlistMedia.postValue(it)
                media = it.data
            }
        }
    }

    fun search(query: String) {
        val list = media
        val filteredList = if (query.isEmpty()) {
            list
        } else {
            list.mapValues {
                val new = it.value.filter { media ->
                    val regex = Regex(pattern = query, options = setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE))
                    when (media) {
                        is MediaModelSealed.ShowModel -> {
                            regex.containsMatchIn(input = media.name)
                        }
                        is MediaModelSealed.MovieModel -> {
                            regex.containsMatchIn(input = media.title)
                        }
                    }
                }
                new.toMutableList()
            }
        }
        _watchlistMedia.value = Resource.Success(filteredList.toMutableMap())
    }

    @ExperimentalCoroutinesApi
    private fun temp() = loadWatchListMediaUseCase()
        .onStart { emit(Resource.Loading) }
        .map {
            val results = mutableMapOf<WatchListType, MutableList<MediaModelSealed>>()
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                }
                is Resource.Success -> {
                    it.data.forEach { uiModel ->
                        when (uiModel.watchListType) {
                            WatchListType.RECENTLY_ADDED -> {
                                results.getOrPut(
                                    WatchListType.RECENTLY_ADDED,
                                    { mutableListOf() }).add(uiModel)
                            }
                            WatchListType.IN_PROGRESS -> {
                                results.getOrPut(WatchListType.IN_PROGRESS, { mutableListOf() })
                                    .add(uiModel)
                            }
                            WatchListType.UPCOMING -> {
                                results.getOrPut(WatchListType.UPCOMING, { mutableListOf() })
                                    .add(uiModel)
                            }
                            WatchListType.COMPLETED -> {
                                results.getOrPut(WatchListType.COMPLETED, { mutableListOf() })
                                    .add(uiModel)
                            }
                            WatchListType.ANTICIPATED -> {
                                results.getOrPut(WatchListType.ANTICIPATED, { mutableListOf() })
                                    .add(uiModel)
                            }
                            else -> {
                            }
                        }
                    }
                }
            }
            Resource.Success(results)
        }
}
