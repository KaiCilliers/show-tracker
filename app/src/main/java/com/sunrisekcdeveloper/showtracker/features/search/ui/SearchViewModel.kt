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

package com.sunrisekcdeveloper.showtracker.features.search.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.SearchRepo
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie
import com.sunrisekcdeveloper.showtracker.features.search.SearchRepositoryContract
import kotlinx.coroutines.launch

/**
 * Search ViewModel
 *
 * @constructor Create empty Search view model
 */
class SearchViewModel @ViewModelInject constructor(
    @SearchRepo val repo: SearchRepositoryContract
) : ViewModel() {

    // TODO all this code is still test data
    private val _movieListData = MutableLiveData<List<Movie>>()
    val movieListData: LiveData<List<Movie>>
        get() = _movieListData

    init {
        _movieListData.value = fakeMovieData()
    }

    fun search(query: String) = viewModelScope.launch {
        _movieListData.value = repo.search(query)
    }

    private fun fakeMovieData(): List<Movie> {
        return listOf(
            Movie("Finding Nemo", "some slug"),
            Movie("Harry Potter", "some slug"),
            Movie("Deadpool", "some slug"),
            Movie("Jurassic Park", "some slug"),
            Movie("Forest Gump", "some slug"),
            Movie("Mall Cop", "some slug"),
            Movie("Miss Congeniality", "some slug"),
            Movie("Gladiator", "some slug"),
            Movie("Finding Dory", "some slug"),
            Movie("Finding Nemo", "some slug"),
            Movie("Harry Potter", "some slug"),
            Movie("Deadpool", "some slug"),
            Movie("Jurassic Park", "some slug"),
            Movie("Forest Gump", "some slug"),
            Movie("Mall Cop", "some slug"),
            Movie("Miss Congeniality", "some slug"),
            Movie("Gladiator", "some slug"),
            Movie("Finding Dory", "some slug"),
            Movie("Finding Nemo", "some slug"),
            Movie("Harry Potter", "some slug"),
            Movie("Deadpool", "some slug"),
            Movie("Jurassic Park", "some slug"),
            Movie("Forest Gump", "some slug")
        )
    }
}