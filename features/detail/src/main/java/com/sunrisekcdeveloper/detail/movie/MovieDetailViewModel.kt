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

package com.sunrisekcdeveloper.detail.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.cache.MediaType
import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.detail.usecase.AddMediaToWatchlistUseCaseContract
import com.sunrisekcdeveloper.detail.movie.usecase.FetchMovieDetailsUseCaseContract
import com.sunrisekcdeveloper.detail.usecase.RemoveMediaFromWatchlistUseCaseContract
import com.sunrisekcdeveloper.detail.movie.usecase.UpdateMovieWatchedStatusUseCaseContract
import com.sunrisekcdeveloper.detail.extras.model.ActionDetailMovie
import com.sunrisekcdeveloper.detail.extras.model.EventDetailMovie
import com.sunrisekcdeveloper.detail.extras.model.StateDetailMovie
import com.sunrisekcdeveloper.models.MovieWatchedStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val fetchMovieDetailsUseCase: FetchMovieDetailsUseCaseContract,
    private val addMediaToWatchlistUseCase: AddMediaToWatchlistUseCaseContract,
    private val updateMovieWatchedStatusUseCase: UpdateMovieWatchedStatusUseCaseContract,
    private val removeMediaFromWatchlistUseCase: RemoveMediaFromWatchlistUseCaseContract
) : ViewModel() {


    private val eventChannel = Channel<EventDetailMovie>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _state = MutableLiveData<StateDetailMovie>()
    val state: LiveData<StateDetailMovie>
        get() = _state

    fun submitAction(action: ActionDetailMovie) = viewModelScope.launch {
        when (action) {
            is ActionDetailMovie.Load -> {
                fetchDetails(action.movieId)
            }
            is ActionDetailMovie.Add -> {
                addMediaToWatchlistUseCase(action.movieId, MediaType.movie())
                eventChannel.send(EventDetailMovie.saveSnackbarMessage("Successfully added \"${action.title}\"!"))
                eventChannel.send(EventDetailMovie.close())
            }
            is ActionDetailMovie.Remove -> {
                removeMediaFromWatchlistUseCase(action.movieId, MediaType.movie())
                eventChannel.send(EventDetailMovie.saveSnackbarMessage("Removed \"${action.title}\""))
                eventChannel.send(EventDetailMovie.close())
            }
            is ActionDetailMovie.SetWatched -> {
                updateMovieWatchedStatusUseCase(action.movieId, MovieWatchedStatus.Watched)
                eventChannel.send(EventDetailMovie.saveSnackbarMessage("\"${action.title}\" marked as watched!"))
                eventChannel.send(EventDetailMovie.close())
            }
            is ActionDetailMovie.SetUnwatched -> {
                updateMovieWatchedStatusUseCase(action.movieId, MovieWatchedStatus.NotWatched)
                eventChannel.send(EventDetailMovie.saveSnackbarMessage("\"${action.title}\" marked as unwatched"))
                eventChannel.send(EventDetailMovie.close())
            }
            ActionDetailMovie.Close -> {
                eventChannel.send(EventDetailMovie.close())
            }
            is ActionDetailMovie.ShowToast -> {
                eventChannel.send(EventDetailMovie.ShowToast(action.msg))
            }
            is ActionDetailMovie.AttemptRemove -> {
                eventChannel.send(
                    EventDetailMovie.showConfirmationDialog(
                        action.movieId,
                        action.title
                    )
                )
            }
            is ActionDetailMovie.AttemptUnwatch -> {
                eventChannel.send(
                    EventDetailMovie.showConfirmationDialogUnwatch(
                        action.movieId,
                        action.title
                    )
                )
            }
        }
    }

    private fun fetchDetails(id: String) = viewModelScope.launch {
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
}