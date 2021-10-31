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

package com.sunrisekcdeveloper.watchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.cache.FilterMovies
import com.sunrisekcdeveloper.cache.FilterShows
import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.models.MovieWatchedStatus
import com.sunrisekcdeveloper.watchlist.usecase.FetchWatchlistMoviesUseCaseContract
import com.sunrisekcdeveloper.watchlist.usecase.FetchWatchlistShowsUseCaseContract
import com.sunrisekcdeveloper.watchlist.usecase.UpdateMovieWatchedStatusUseCaseContract
import com.sunrisekcdeveloper.watchlist.usecase.UpdateShowProgressUseCaseContract
import com.sunrisekcdeveloper.watchlist.extras.model.ActionWatchlist
import com.sunrisekcdeveloper.watchlist.extras.model.EventWatchlist
import com.sunrisekcdeveloper.watchlist.extras.model.StateWatchlist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val fetchWatchlistMoviesUseCase: FetchWatchlistMoviesUseCaseContract,
    private val fetchWatchlistShowsUseCase: FetchWatchlistShowsUseCaseContract,
    private val updateMovieWatchedStatusUseCase: UpdateMovieWatchedStatusUseCaseContract,
    private val updateShowProgressUseCase: UpdateShowProgressUseCaseContract
) : ViewModel() {

    private val eventChannel = Channel<EventWatchlist>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    val _state = MutableLiveData<StateWatchlist>()
    val state: LiveData<StateWatchlist>
        get() = _state

    private var showSearchQuery = ""
    private var movieSearchQuery = ""
    var movieFilterOption: FilterMovies = FilterMovies.NoFilters
    var showFilterOption: FilterShows = FilterShows.NoFilters

    fun updateShowSearchQuery(query: String) {
        showSearchQuery = query
        // todo not pretty but it works
        if (query.isEmpty()) {
            submitAction(ActionWatchlist.LoadWatchlistData)
        }
    }

    fun updateMovieSearchQuery(query: String) {
        movieSearchQuery = query
        // todo not pretty but it works
        if (query.isEmpty()) {
            submitAction(ActionWatchlist.LoadWatchlistData)
        }
    }

    fun updateShowSortBy(sortBy: FilterShows) {
        showFilterOption = sortBy
        submitAction(ActionWatchlist.LoadWatchlistData)
    }

    fun updateMovieSortBy(sortBy: FilterMovies) {
        movieFilterOption = sortBy
        submitAction(ActionWatchlist.LoadWatchlistData)
    }

    fun submitAction(action: ActionWatchlist) = viewModelScope.launch {
        Timber.e("action submitted: $action")
        when (action) {
            ActionWatchlist.LoadWatchlistData -> {
                watchlistData()
            }
            is ActionWatchlist.ShowToast -> {
                eventChannel.send(EventWatchlist.ShowToast(action.msg))
            }
            is ActionWatchlist.MarkMovieWatched -> {
                updateMovieWatchedStatusUseCase(action.movieId, MovieWatchedStatus.Watched)
            }
            is ActionWatchlist.MarkMovieUnWatched -> {
                updateMovieWatchedStatusUseCase(action.movieId, MovieWatchedStatus.NotWatched)
            }
            is ActionWatchlist.UpdateShowProgress -> {
                updateShowProgressUseCase(action.instructions)
            }
            is ActionWatchlist.StartWatchingShow -> {
                eventChannel.send(EventWatchlist.ConfigureShow(action.showId, action.title))
            }
            is ActionWatchlist.LoadMediaDetails -> {
                eventChannel.send(
                    EventWatchlist.LoadMediaDetails(
                        action.mediaId,
                        action.title,
                        action.posterPath,
                        action.type
                    )
                )
            }
            is ActionWatchlist.ShowSnackbar -> {
                eventChannel.send(EventWatchlist.showSnackbar(action.msg))
            }
            is ActionWatchlist.AttemptUnwatch -> {
                eventChannel.send(
                    EventWatchlist.showConfirmationDialog(
                        action.movieId,
                        action.title
                    )
                )
            }
            ActionWatchlist.ShowEmptyState -> {
                _state.value = StateWatchlist.emptyList()
            }
            ActionWatchlist.NoFilterResults -> {
                _state.value = StateWatchlist.noFilterResults()
            }
        }
    }

    private fun watchlistData() = viewModelScope.launch {
        val moviesFlow = fetchWatchlistMoviesUseCase(movieFilterOption)
        val showsFlow = fetchWatchlistShowsUseCase(showFilterOption)

        // todo consider replacing with zip for less emissions
        combine(moviesFlow, showsFlow) { resourceMovie, resourceShow ->
            // If either are loading
            if (resourceMovie is Resource.Loading || resourceShow is Resource.Loading) {
                return@combine StateWatchlist.Loading
            }

            // If either throws an error
            if (resourceMovie is Resource.Error || resourceShow is Resource.Error) {
                return@combine StateWatchlist.Error(Exception("Error retrieving watchlist data"))
            }

            // If both are success
            if (resourceMovie is Resource.Success && resourceShow is Resource.Success) {
                return@combine StateWatchlist.success(
                    moviesList = resourceMovie.data.filter {
                        if (movieSearchQuery.isEmpty()) {
                            true
                        } else {
                            it.title.contains(movieSearchQuery, true)
                        }
                    },
                    showsList = resourceShow.data.filter {
                        if (showSearchQuery.isEmpty()) {
                            true
                        } else {
                            it.title.contains(showSearchQuery, true)
                        }
                    }
                )
            } else {
                return@combine StateWatchlist.Error(Exception("Fetching watchlist data unknown state..."))
            }
            // todo consider adding conflate() to flow
        }.collect {
            _state.value = it
        }
    }
}