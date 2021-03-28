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
import com.sunrisekcdeveloper.showtracker.features.detail.application.AddMovieToWatchlistUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.detail.application.FetchMovieDetailsUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.detail.application.RemoveMovieFromWatchlistUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.detail.application.UpdateMovieWatchedStatusUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.ActionDetailMovie
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.EventDetailMovie
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.MovieWatchedStatus
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.StateDetailMovie
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ViewModelMovieDetail @ViewModelInject constructor(
    private val fetchMovieDetailsUseCase: FetchMovieDetailsUseCaseContract,
    private val addMovieToWatchlistUseCase: AddMovieToWatchlistUseCaseContract,
    private val updateMovieWatchedStatusUseCase: UpdateMovieWatchedStatusUseCaseContract,
    private val removeMovieFromWatchlistUseCase: RemoveMovieFromWatchlistUseCaseContract
) : ViewModel() {

    private val eventChannel = Channel<EventDetailMovie>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _state = MutableLiveData<StateDetailMovie>()
    val state: LiveData<StateDetailMovie>
        get() = _state

    private fun movieDetails(id: String) = viewModelScope.launch {
        fetchMovieDetailsUseCase(id).collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _state.value = StateDetailMovie.Success(resource.data)
                }
                is Resource.Error -> {
                    _state.value = StateDetailMovie.Error(Exception(resource.exception))
                }
                Resource.Loading -> {
                    _state.value = StateDetailMovie.Loading
                }
            }
        }
    }

    fun submitAction(action: ActionDetailMovie) = viewModelScope.launch {
        when (action) {
            is ActionDetailMovie.Load -> {
                movieDetails(action.movieId)
            }
            is ActionDetailMovie.Add -> {
                addMovieToWatchlistUseCase(action.movieId)
            }
            is ActionDetailMovie.Remove -> {
                removeMovieFromWatchlistUseCase(action.movieId)
            }
            is ActionDetailMovie.SetWatched -> {
                updateMovieWatchedStatusUseCase(action.movieId, MovieWatchedStatus.Watched)
            }
            is ActionDetailMovie.SetUnwatched -> {
               updateMovieWatchedStatusUseCase(action.movieId, MovieWatchedStatus.NotWatched)
            }
            ActionDetailMovie.Close -> {
                eventChannel.send(EventDetailMovie.Close)
            }
            is ActionDetailMovie.ShowToast -> {
                eventChannel.send(EventDetailMovie.ShowToast("msg"))
            }
        }
    }
}

