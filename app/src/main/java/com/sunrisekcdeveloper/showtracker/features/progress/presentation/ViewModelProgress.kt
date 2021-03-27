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

package com.sunrisekcdeveloper.showtracker.features.progress.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.features.progress.application.FetchShowSeasonAndEpisodeTotalsUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.progress.application.SetShowProgressUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.progress.domain.model.ActionProgress
import com.sunrisekcdeveloper.showtracker.features.progress.domain.model.EventProgress
import com.sunrisekcdeveloper.showtracker.features.progress.domain.model.SetShowProgress
import com.sunrisekcdeveloper.showtracker.features.progress.domain.model.StateProgress
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ViewModelProgress @ViewModelInject constructor(
    private val fetchShowSeasonAndEpisodeTotalsUseCase: FetchShowSeasonAndEpisodeTotalsUseCaseContract,
    private val setShowProgressUseCase: SetShowProgressUseCaseContract
) : ViewModel() {

    private val eventChannel = Channel<EventProgress>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _state = MutableLiveData<StateProgress>()
    val state: LiveData<StateProgress>
        get() = _state

    fun submitAction(action: ActionProgress) = viewModelScope.launch {
        when (action) {
            ActionProgress.NavigateBack -> {
                eventChannel.send(EventProgress.PopBackStack)
            }
            is ActionProgress.CreateToast -> {
                eventChannel.send(EventProgress.ShowToast(action.msg))
            }
            is ActionProgress.Load -> {
                val resource = fetchShowSeasonAndEpisodeTotalsUseCase(action.showId)
                when (resource) {
                    is Resource.Success -> { _state.value = StateProgress.Success(resource.data) }
                    is Resource.Error -> { _state.value = StateProgress.Error(Exception(resource.exception)) }
                    Resource.Loading -> { _state.value = StateProgress.Loading }
                }
            }
            is ActionProgress.SetShowProgress -> {
                setShowProgressUseCase(SetShowProgress.Partial(action.showId, action.seasonNumber, action.episodeNumber))
            }
            is ActionProgress.MarkShowUpToDate -> {
                setShowProgressUseCase(SetShowProgress.UpToDate(action.showId))
            }
        }
    }
}

