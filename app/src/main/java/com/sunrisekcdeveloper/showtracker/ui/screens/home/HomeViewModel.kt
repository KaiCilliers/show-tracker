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

package com.sunrisekcdeveloper.showtracker.ui.screens.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.showtracker.data.local.MovieDao
import com.sunrisekcdeveloper.showtracker.di.MainActivityModule.MainRepo
import com.sunrisekcdeveloper.showtracker.model.FeaturedList
import com.sunrisekcdeveloper.showtracker.model.Movie
import com.sunrisekcdeveloper.showtracker.model.roomresults.TrendingMoviesNew
import com.sunrisekcdeveloper.showtracker.repository.RepositoryContract
import com.sunrisekcdeveloper.showtracker.util.datastate.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Home ViewModel
 *
 * @constructor Create empty Home view model
 */
class HomeViewModel @ViewModelInject constructor(
    @MainRepo val repo: RepositoryContract,
    val dao: MovieDao
) : ViewModel() {

    private val _trendingMovies = MutableLiveData<Resource<List<TrendingMoviesNew>>>()
    val trendingMovies: LiveData<Resource<List<TrendingMoviesNew>>>
        get() = _trendingMovies

    private val _featuredListData = MutableLiveData<List<FeaturedList>>()
    val featuredListData: LiveData<List<FeaturedList>>
        get() = _featuredListData

    init {
//        _featuredListData.value = fakeFeaturedData()
//        featuredMovies()
        Timber.d("gona call init function")
        trendingMovies()
    }

    private fun trendingMovies() = viewModelScope.launch {
//        Timber.d("adsakdoakaldaskdoka")
//        repo.trendingMoviesNewFlow().onEach {
//            Timber.d("ok?")
//            _trendingMovies.value = it
//        }.launchIn(viewModelScope)
        Timber.d("inside - gona call repo")
        repo.trendingMoviesNewFlow().collect {
            Timber.d("inside collect")
            when(it) {
                is Resource.Loading -> { Timber.d("loading")}
                is Resource.Error -> { Timber.d("erorr")
                    throw it.exception
                }
                is Resource.Success -> {
                    Timber.d("success and setting value")
                _trendingMovies.value = it
                    delayed()
                }
            }
        }
    }

    private var count = 0

    private suspend fun delayed() {
        CoroutineScope(Dispatchers.IO).launch {
            if (count == 0) {
                count++
                delay(10000)
                dao.tempClearTopThree()
            }
        }
    }

    private fun featuredMovies() = viewModelScope.launch {
        _featuredListData.value = repo.featuredMovies()
    }

    private fun fakeFeaturedData(): List<FeaturedList> {
        return listOf(
            FeaturedList(
                "Featured",
                listOf<Movie>(
                    Movie("ONE ONE"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "New Releases",
                listOf<Movie>(
                    Movie("TWO TWO"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "Comedy",
                listOf<Movie>(
                    Movie("THREE THREE"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "Watchlist",
                listOf<Movie>(
                    Movie("FOUR FOUR"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "For you",
                listOf<Movie>(
                    Movie("FIVE FIVE"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "Horror",
                listOf<Movie>(
                    Movie("SIX SIX"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "Beacuse you watched Breaking Bad",
                listOf<Movie>(
                    Movie("SEVEN SEVEN"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "Featured",
                listOf<Movie>(
                    Movie("EIGHT EIGHT"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "New Releases",
                listOf<Movie>(
                    Movie("NINE NINE"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "Comedy",
                listOf<Movie>(
                    Movie("TEN TEN"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "Watchlist",
                listOf<Movie>(
                    Movie("ELEVEN ELEVEN"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "For you",
                listOf<Movie>(
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
            ),
            FeaturedList(
                "Horror",
                listOf<Movie>(
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
            ),
            FeaturedList(
                "Beacuse you watched Breaking Bad",
                listOf<Movie>(
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
            )
        )
}
}