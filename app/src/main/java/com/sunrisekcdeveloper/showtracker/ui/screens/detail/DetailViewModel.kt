/*
 * Copyright © 2020. The Android Open Source Project
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

package com.sunrisekcdeveloper.showtracker.ui.screens.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.MainRepo
import com.sunrisekcdeveloper.showtracker.model.DetailedMovie
import com.sunrisekcdeveloper.showtracker.model.Movie
import com.sunrisekcdeveloper.showtracker.repository.RepositoryContract
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Detail ViewModel
 *
 * @constructor Create empty Detail view model
 */
class DetailViewModel @ViewModelInject constructor(
    @MainRepo val repo: RepositoryContract
) : ViewModel() {

    private val _detailedMovie = MutableLiveData<DetailedMovie>()
    val detailedMovie: LiveData<DetailedMovie>
        get() = _detailedMovie

    private val _relatedMovies = MutableLiveData<List<Movie>>()
    val relatedMovies: LiveData<List<Movie>>
        get() = _relatedMovies

    fun getMovieDetails(slug: String) = viewModelScope.launch {
        _detailedMovie.value = repo.movieDetails(slug, "full")
        Timber.e("Slug value: ${detailedMovie.value?.basics?.slug}")
        _relatedMovies.value = repo.relatedMovies(detailedMovie.value?.basics?.slug ?: "")
    }
}