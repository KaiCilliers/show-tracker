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

package com.sunrisekcdeveloper.showtracker.ui.screens.progress

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.showtracker.data.network.model.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.model.Movie
import com.sunrisekcdeveloper.showtracker.model.SuggestionListModel
import com.sunrisekcdeveloper.showtracker.repository.MainRepository
import com.sunrisekcdeveloper.showtracker.util.datastate.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Progress ViewModel
 *
 * @constructor Create empty Progress view model
 */
class ProgressViewModel @ViewModelInject constructor(
    private val repo: MainRepository
) : ViewModel() {

    private val _test = MutableLiveData<Resource<List<ResponseMovie>>>()
    val test: LiveData<Resource<List<ResponseMovie>>>
        get() = _test

    private val _suggestionListData = MutableLiveData<List<SuggestionListModel>>()
    val suggestionListData: LiveData<List<SuggestionListModel>>
        get() = _suggestionListData

    private val _movieListData = MutableLiveData<List<Movie>>()
    val movieListData: LiveData<List<Movie>>
        get() = _movieListData

    init {
        tester()
        _suggestionListData.value = fakeSuggestionData()
        _movieListData.value = fakeMovieData()
    }

    private fun tester() {
        viewModelScope.launch {
            repo.test().onEach {
                _test.value = it
            }.launchIn(viewModelScope)
        }
    }

    private fun fakeSuggestionData(): List<SuggestionListModel> {
        return listOf(
            SuggestionListModel.HeaderItem("Today"),
            SuggestionListModel.MovieItem(Movie("Finding Nemo")),
            SuggestionListModel.MovieItem(Movie("Harry Potter")),
            SuggestionListModel.MovieItem(Movie("Deadpool")),
            SuggestionListModel.HeaderItem("Tomorrow"),
            SuggestionListModel.MovieItem(Movie("Jurassic Park")),
            SuggestionListModel.MovieItem(Movie("Forest Gump")),
            SuggestionListModel.HeaderItem("Next Week"),
            SuggestionListModel.MovieItem(Movie("Mall Cop")),
            SuggestionListModel.MovieItem(Movie("Miss Congeniality")),
            SuggestionListModel.MovieItem(Movie("Gladiator")),
            SuggestionListModel.MovieItem(Movie("Finding Dory")),
            SuggestionListModel.MovieItem(Movie("Shrek")),
            SuggestionListModel.MovieItem(Movie("Snow White"))
        )
    }

    private fun fakeMovieData(): List<Movie> {
        return listOf<Movie>(
            Movie("Finding Nemo"),
            Movie("Harry Potter"),
            Movie("Deadpool"),
            Movie("Jurassic Park"),
            Movie("Forest Gump"),
            Movie("Mall Cop"),
            Movie("Miss Congeniality"),
            Movie("Gladiator"),
            Movie("Finding Dory")
        )
    }
}