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

package com.sunrisekcdeveloper.showtracker.features.watchlist.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.features.detail.application.UpdateMovieWatchedStatusUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.MovieWatchedStatus
import com.sunrisekcdeveloper.showtracker.features.watchlist.application.FetchWatchlistMoviesUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.application.FetchWatchlistShowsUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.application.UpdateShowProgressUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.FilterMovies
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.SortShows
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.ActionWatchlist
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.EventWatchlist
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.StateWatchlist
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class ViewModelWatchlist @ViewModelInject constructor(
    private val fetchWatchlistMoviesUseCase: FetchWatchlistMoviesUseCaseContract,
    private val fetchWatchlistShowsUseCase: FetchWatchlistShowsUseCaseContract,
    private val updateMovieWatchedStatusUseCase: UpdateMovieWatchedStatusUseCaseContract,
    private val updateShowProgressUseCase: UpdateShowProgressUseCaseContract
) : ViewModel() {

    private val eventChannel = Channel<EventWatchlist>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _state = MutableLiveData<StateWatchlist>()
    val state: LiveData<StateWatchlist>
        get() = _state

    private var showSearchQuery = ""
    private var movieSearchQuery = ""
    private var movieFilterOption: FilterMovies = FilterMovies.NoFilters
    private var showSortOrder: SortShows  = SortShows.ByTitle

    fun showSearchQuery() = showSearchQuery
    fun movieSearchQuery() = movieSearchQuery

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
    fun updateShowSortBy(sortBy: SortShows) {
        showSortOrder = sortBy
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
                eventChannel.send(EventWatchlist.ConfigureShow(action.showId))
            }
            is ActionWatchlist.LoadMediaDetails -> {
                eventChannel.send(EventWatchlist.LoadMediaDetails(
                    action.mediaId,
                    action.title,
                    action.posterPath,
                    action.type
                ))
            }
        }
    }

    private fun watchlistData() = viewModelScope.launch {
        val moviesFlow = fetchWatchlistMoviesUseCase(movieFilterOption)
        val showsFlow = fetchWatchlistShowsUseCase(showSortOrder)

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
                return@combine StateWatchlist.Success(
                    movies = resourceMovie.data.filter {
                        if (movieSearchQuery.isEmpty()) {
                            true
                        } else {
                            it.title.contains(movieSearchQuery, true)
                        }
                    },
                    shows = resourceShow.data.filter {
                        if (showSearchQuery.isEmpty()) {
                            true
                        } else {
                            it.title.contains(showSearchQuery, true)
                        }
                    }
                )
            } else {
                return@combine StateWatchlist.Error(Exception("Fetching watchlist data unkown state..."))
            }
            // todo consider adding conflate() to flow
        }.collect {
            _state.value = it
        }
    }
}