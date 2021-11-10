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
package com.sunrisekcdeveloper.detail.show

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.cache.MediaType
import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.detail.usecase.AddMediaToWatchlistUseCaseContract
import com.sunrisekcdeveloper.detail.show.usecase.FetchShowDetailsUseCaseContract
import com.sunrisekcdeveloper.detail.usecase.RemoveMediaFromWatchlistUseCaseContract
import com.sunrisekcdeveloper.detail.extras.model.ActionDetailShow
import com.sunrisekcdeveloper.detail.extras.model.EventDetailShow
import com.sunrisekcdeveloper.detail.extras.model.StateDetailShow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowDetailViewModel @Inject constructor(
    private val fetchShowDetailsUseCase: FetchShowDetailsUseCaseContract,
    private val addMediaToWatchlistUseCase: AddMediaToWatchlistUseCaseContract,
    private val removeMediaFromWatchlistUseCase: RemoveMediaFromWatchlistUseCaseContract
) : ViewModel() {

    private val eventChannel = Channel<EventDetailShow>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _state = MutableLiveData<StateDetailShow>()
    val state: LiveData<StateDetailShow>
        get() = _state

    fun submitAction(action: ActionDetailShow) = viewModelScope.launch {
        when (action) {
            is ActionDetailShow.Load -> {
                fetchDetails(action.showId)
            }
            is ActionDetailShow.Add -> {
                addMediaToWatchlistUseCase(action.showId, MediaType.show())
                eventChannel.send(EventDetailShow.saveSnackbarMessage("Successfully added \"${action.title}\"!"))
                eventChannel.send(EventDetailShow.Close)
            }
            is ActionDetailShow.Remove -> {
                removeMediaFromWatchlistUseCase(action.showId, MediaType.show())
                eventChannel.send(EventDetailShow.saveSnackbarMessage("Removed \"${action.title}\""))
                eventChannel.send(EventDetailShow.Close)
            }
            ActionDetailShow.Close -> {
                eventChannel.send(EventDetailShow.Close)
            }
            is ActionDetailShow.ShowToast -> {
                eventChannel.send(EventDetailShow.ShowToast(action.msg))
            }
            is ActionDetailShow.StartWatching -> {
                eventChannel.send(EventDetailShow.LaunchStartWatching(action.showId, action.title))
            }
            is ActionDetailShow.UpdateProgress -> {
                eventChannel.send(EventDetailShow.GoToShowInWatchlist(action.showId))
            }
            is ActionDetailShow.AttemptRemove -> {
                eventChannel.send(
                    EventDetailShow.showConfirmationDialog(
                        action.showId,
                        action.title
                    )
                )
            }
        }
    }

    private fun fetchDetails(id: String) = viewModelScope.launch {
        fetchShowDetailsUseCase(id).collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _state.value = StateDetailShow.Success(resource.data)
                }
                is Resource.Error -> {
                    _state.value = StateDetailShow.Error(Exception(resource.exception))
                }
                Resource.Loading -> {
                    _state.value = StateDetailShow.Loading
                }
            }
        }
    }
}