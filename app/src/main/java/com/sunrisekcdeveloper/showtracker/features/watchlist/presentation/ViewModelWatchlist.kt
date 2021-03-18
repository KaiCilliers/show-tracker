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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.features.detail.application.UpdateMovieWatchedStatusUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.MovieWatchedStatus
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelMovieDetail
import com.sunrisekcdeveloper.showtracker.features.watchlist.application.FetchWatchlistMoviesUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.application.FetchWatchlistShowsUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.application.UpdateShowProgressUseCaseContract
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class ViewModelWatchlist @ViewModelInject constructor(
    private val fetchWatchlistMoviesUseCase: FetchWatchlistMoviesUseCaseContract,
    private val fetchWatchlistShowsUseCase: FetchWatchlistShowsUseCaseContract,
    private val updateMovieWatchedStatusUseCase: UpdateMovieWatchedStatusUseCaseContract,
    private val updateShowProgressUseCase: UpdateShowProgressUseCaseContract
) : ViewModel() {
    private val _watchlistMovies = MutableLiveData<Resource<List<UIModelWatchlisMovie>>>()
    val watchlistMovies: LiveData<Resource<List<UIModelWatchlisMovie>>>
        get() = _watchlistMovies

    private val _watchlistShows = MutableLiveData<Resource<List<UIModelWatchlistShow>>>()
    val watchlistShows: LiveData<Resource<List<UIModelWatchlistShow>>>
        get() = _watchlistShows

    init {
        watchlistMovies()
        watchlistShows()
    }

    fun updateShowProgress(action: UpdateShowAction) = viewModelScope.launch{
        updateShowProgressUseCase(action)
    }

    fun markMovieAsWatched(movieId: String) = viewModelScope.launch {
        Timber.e("mark as watched: $movieId")
        updateMovieWatchedStatusUseCase(movieId, MovieWatchedStatus.Watched)
    }

    fun markMovieAsUnWatched(movieId: String) = viewModelScope.launch {
        Timber.e("mark as not watched $movieId")
        updateMovieWatchedStatusUseCase(movieId, MovieWatchedStatus.NotWatched)
    }

    fun watchlistMovies() = viewModelScope.launch {
        fetchWatchlistMoviesUseCase().collect {
            _watchlistMovies.value = it
        }
    }

    fun watchlistShows() = viewModelScope.launch {
        fetchWatchlistShowsUseCase().collect {
            _watchlistShows.value = it
        }
    }
}

data class UIModelWatchlisMovie(
    val id: String,
    val title: String,
    val overview: String,
    val posterPath: String,
    val watched: Boolean,
    val dateAdded: Long,
    val dateWatched: Long,
    val lastUpdated: Long
)

sealed class UpdateShowAction {
    data class IncrementEpisode(val showId: String) : UpdateShowAction()
    data class CompleteSeason(val showId: String) : UpdateShowAction()
    data class UpToDateWithShow(val showId: String) : UpdateShowAction()
}