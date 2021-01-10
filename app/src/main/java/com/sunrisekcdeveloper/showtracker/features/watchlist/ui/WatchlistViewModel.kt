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

package com.sunrisekcdeveloper.showtracker.features.watchlist.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.MainRepo
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.WatchlistRepo
import com.sunrisekcdeveloper.showtracker.features.discover.models.Movie
import com.sunrisekcdeveloper.showtracker.features.watchlist.WatchListRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.models.SuggestionListModel
import com.sunrisekcdeveloper.showtracker.repository.RepositoryContract

/**
 * Progress ViewModel
 *
 * @constructor Create empty Progress view model
 */
class WatchlistViewModel @ViewModelInject constructor(
    @WatchlistRepo private val repo: WatchListRepositoryContract
) : ViewModel() {

    // TODO all this code is still test data
    private val _suggestionListData = MutableLiveData<List<SuggestionListModel>>()
    val suggestionListData: LiveData<List<SuggestionListModel>>
        get() = _suggestionListData

    private val _movieListData = MutableLiveData<List<Movie>>()
    val movieListData: LiveData<List<Movie>>
        get() = _movieListData

    init {
        _suggestionListData.value = fakeSuggestionData()
        _movieListData.value = fakeMovieData()
    }

    private fun fakeSuggestionData(): List<SuggestionListModel> {
        return listOf(
            SuggestionListModel.HeaderItem("Today"),
            SuggestionListModel.MovieItem(Movie("Finding Nemo", "some slug")),
            SuggestionListModel.MovieItem(Movie("Harry Potter", "some slug")),
            SuggestionListModel.MovieItem(Movie("Deadpool", "some slug")),
            SuggestionListModel.HeaderItem("Tomorrow"),
            SuggestionListModel.MovieItem(Movie("Jurassic Park", "some slug")),
            SuggestionListModel.MovieItem(Movie("Forest Gump", "some slug")),
            SuggestionListModel.HeaderItem("Next Week"),
            SuggestionListModel.MovieItem(Movie("Mall Cop", "some slug")),
            SuggestionListModel.MovieItem(Movie("Miss Congeniality", "some slug")),
            SuggestionListModel.MovieItem(Movie("Gladiator", "some slug")),
            SuggestionListModel.MovieItem(Movie("Finding Dory", "some slug")),
            SuggestionListModel.MovieItem(Movie("Shrek", "some slug")),
            SuggestionListModel.MovieItem(Movie("Snow Whit", "some slug"))
        )
    }

    private fun fakeMovieData(): List<Movie> {
        return listOf<Movie>(
            Movie("Finding Nemo", "myslug"),
            Movie("Harry Potter", "myslug"),
            Movie("Deadpool", "myslug"),
            Movie("Jurassic Park", "myslug"),
            Movie("Forest Gump", "myslug"),
            Movie("Mall Cop", "myslug"),
            Movie("Miss Congeniality", "myslug"),
            Movie("Gladiator", "myslug"),
            Movie("Finding Dory", "myslug")
        )
    }
}