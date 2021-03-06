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

package com.sunrisekcdeveloper.progress.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.progress.application.FetchShowSeasonAndEpisodeTotalsUseCaseContract
import com.sunrisekcdeveloper.progress.application.SetShowProgressUseCaseContract
import com.sunrisekcdeveloper.progress.domain.model.ActionProgress
import com.sunrisekcdeveloper.progress.domain.model.EventProgress
import com.sunrisekcdeveloper.progress.domain.model.SetShowProgress
import com.sunrisekcdeveloper.progress.domain.model.StateProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelProgress @Inject constructor(
    private val fetchShowSeasonAndEpisodeTotalsUseCase: FetchShowSeasonAndEpisodeTotalsUseCaseContract,
    private val setShowProgressUseCase: SetShowProgressUseCaseContract
) : ViewModel() {

    private val eventChannel = Channel<EventProgress>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _state = MutableLiveData<StateProgress>()
    val state: LiveData<StateProgress>
        get() = _state

    init {
        _state.value = StateProgress.loading()
    }

    fun submitAction(action: ActionProgress) = viewModelScope.launch {
        when (action) {
            ActionProgress.NavigateBack -> {
                eventChannel.send(EventProgress.PopBackStack)
            }
            is ActionProgress.CreateToast -> {
                eventChannel.send(EventProgress.ShowToast(action.msg))
            }
            is ActionProgress.Load -> {
                when (val resource = fetchShowSeasonAndEpisodeTotalsUseCase(action.showId)) {
                    is Resource.Success -> {
                        _state.value = StateProgress.Success(resource.data)
                    }
                    is Resource.Error -> {
                        _state.value = StateProgress.Error(Exception(resource.exception))
                    }
                    Resource.Loading -> {
                        _state.value = StateProgress.Loading
                    }
                }
            }
            is ActionProgress.SetShowProgress -> {
                setShowProgressUseCase(
                    SetShowProgress.Partial(
                        action.showId,
                        action.seasonNumber,
                        action.episodeNumber
                    )
                )
                eventChannel.send(EventProgress.saveSnackbarMessage("Successfully started watching \"${action.title}!\""))
                eventChannel.send(EventProgress.popBackStack())
            }
            is ActionProgress.MarkShowUpToDate -> {
                setShowProgressUseCase(SetShowProgress.UpToDate(action.showId))
                eventChannel.send(EventProgress.saveSnackbarMessage("You are up to date with \"${action.title}!\""))
                eventChannel.send(EventProgress.popBackStack())
            }
            is ActionProgress.AttemptSetProgress -> {
                eventChannel.send(
                    EventProgress.showConfirmationDialogSetProgress(
                        action.showId,
                        action.seasonNumber,
                        action.episodeNumber,
                        action.title
                    )
                )
            }
            is ActionProgress.AttemptMarkUpToDate -> {
                eventChannel.send(
                    EventProgress.showConfirmationDialogUpToDate(
                        action.showId,
                        action.title
                    )
                )
            }
        }
    }
}

