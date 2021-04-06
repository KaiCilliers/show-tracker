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

package com.sunrisekcdeveloper.showtracker.features.search.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sunrisekcdeveloper.showtracker.common.util.Resource
import com.sunrisekcdeveloper.showtracker.features.search.application.LoadUnwatchedMediaUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.search.application.SearchMediaByTitleUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.search.domain.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
class ViewModelSearch @ViewModelInject constructor(
    private val searchMediaByTitleUseCase: SearchMediaByTitleUseCaseContract,
    private val loadUnwatchedMediaUseCase: LoadUnwatchedMediaUseCaseContract
) : ViewModel() {

    private val eventChannel = Channel<EventSearch>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _state = MutableLiveData<StateSearch>()
    val state: LiveData<StateSearch>
        get() = _state

    private val _stateNetwork = MutableLiveData<StateNetwork>()
    private val stateNetwork: LiveData<StateNetwork>
        get() = _stateNetwork

    private var currentQuery: String? = null
    private var currentSearchResult: Flow<PagingData<UIModelPoster>>? = null

    private val unwatchedMediaCache = mutableListOf<UIModelPoster>()

    fun submitAction(action: ActionSearch) = viewModelScope.launch {
        when (action) {
            is ActionSearch.ShowToast -> {
                eventChannel.send(EventSearch.ShowToast(action.msg))
            }
            is ActionSearch.LoadMediaDetails -> {
                eventChannel.send(
                    EventSearch.LoadMediaDetails(
                        action.mediaId,
                        action.title,
                        action.posterPath,
                        action.type
                    )
                )
            }
            is ActionSearch.SearchForMedia -> {
                if (stateNetwork.value == StateNetwork.Connected) {
                    searchMedia(action.query).collectLatest { pagingData ->
                        _state.value = StateSearch.Success(pagingData)
                    }
                } else {
                    _state.value = StateSearch.Error(Exception("No internet access..."))
                }
            }
            ActionSearch.BackButtonPress -> {
                eventChannel.send(EventSearch.PopBackStack)
            }
            ActionSearch.NotifyNoSearchResults -> {
                _state.value = StateSearch.NoResultsFound
            }
            ActionSearch.LoadUnwatchedContent -> {
                if (unwatchedMediaCache.isEmpty()) {
                    when (val resource = loadUnwatchedMediaUseCase()) {
                        is Resource.Success -> {
                            unwatchedMediaCache.addAll(resource.data)
                            if (unwatchedMediaCache.isEmpty()) {
                                _state.value = StateSearch.emptyWatchlist()
                            } else {
                                _state.value = StateSearch.EmptySearch(resource.data)
                            }
                        }
                        is Resource.Error -> {
                            eventChannel.send(EventSearch.ShowToast("could not load unwatched media content"))
                        }
                        Resource.Loading -> {
                            _state.value = StateSearch.Loading
                        }
                    }
                } else {
                    _state.value = StateSearch.EmptySearch(unwatchedMediaCache)
                }
            }
            ActionSearch.DeviceIsOnline -> {
                _stateNetwork.value = StateNetwork.Connected
            }
            ActionSearch.DeviceIsOffline -> {
                _stateNetwork.value = StateNetwork.Disconnected
            }
            is ActionSearch.ShowSnackBar -> {
                eventChannel.send(EventSearch.showSnackBar(action.message))
            }
        }
    }

    private fun searchMedia(query: String): Flow<PagingData<UIModelPoster>> {
        val lastResult = currentSearchResult
        if (query == currentQuery && lastResult != null) {
            return lastResult
        }
        currentQuery = query
        val newResult: Flow<PagingData<UIModelPoster>> = searchMediaByTitleUseCase(query)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}