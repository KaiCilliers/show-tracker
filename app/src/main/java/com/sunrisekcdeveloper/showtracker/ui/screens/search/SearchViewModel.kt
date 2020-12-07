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

package com.sunrisekcdeveloper.showtracker.ui.screens.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunrisekcdeveloper.showtracker.entities.domain.Movie

/**
 * Search ViewModel
 *
 * @constructor Create empty Search view model
 */
class SearchViewModel @ViewModelInject constructor() : ViewModel() {

    private val _movieListData = MutableLiveData<List<Movie>>()
    val movieListData: LiveData<List<Movie>>
        get() = _movieListData

    init {
        _movieListData.value = fakeMovieData()
    }

    private fun fakeMovieData(): List<Movie> {
        return listOf(
            Movie("Finding Nemo"),
            Movie("Harry Potter"),
            Movie("Deadpool"),
            Movie("Jurassic Park"),
            Movie("Forest Gump"),
            Movie("Mall Cop"),
            Movie("Miss Congeniality"),
            Movie("Gladiator"),
            Movie("Finding Dory"),
            Movie("Finding Nemo"),
            Movie("Harry Potter"),
            Movie("Deadpool"),
            Movie("Jurassic Park"),
            Movie("Forest Gump"),
            Movie("Mall Cop"),
            Movie("Miss Congeniality"),
            Movie("Gladiator"),
            Movie("Finding Dory"),
            Movie("Finding Nemo"),
            Movie("Harry Potter"),
            Movie("Deadpool"),
            Movie("Jurassic Park"),
            Movie("Forest Gump")
        )
    }
}