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
import androidx.lifecycle.*
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.MainRepo
import com.sunrisekcdeveloper.showtracker.repository.RepositoryContract
import kotlinx.coroutines.*

/**
 * Home ViewModel
 *
 * @constructor Create empty Home view model
 */
@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(
    @MainRepo private val repo: RepositoryContract
) : ViewModel() {

    val trend = repo.trendingMoviesFlow().asLiveData()
    val pop = repo.popularMoviesFlow().asLiveData()
    val box = repo.boxofficeMoviesFlow().asLiveData()
    val mostWatched = repo.mostWatchedMoviesFlow().asLiveData()
    val mostPlayed = repo.mostPlayedMoviesFlow().asLiveData()
    val anticipated = repo.mostAnticipatedMoviesFlow().asLiveData()
    val recommended = repo.recommendedMoviesFlow().asLiveData()
}
