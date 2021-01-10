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

package com.sunrisekcdeveloper.showtracker.features.discover.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.DiscoveryRepo
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.MainRepo
import com.sunrisekcdeveloper.showtracker.features.discover.DiscoveryRepositoryContract
import com.sunrisekcdeveloper.showtracker.repository.RepositoryContract
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

/**
 * Home ViewModel
 *
 * @constructor Create empty Home view model
 */
@ExperimentalCoroutinesApi
class DiscoveryViewModel @ViewModelInject constructor(
    @DiscoveryRepo private val repo: DiscoveryRepositoryContract
) : ViewModel() {

    val trend = liveData { emit(repo.trendingMovie()) }
    val pop = liveData { emit(repo.popularMovie()) }
    val box = liveData { emit(repo.boxofficeMovie()) }
    val mostWatched = liveData { emit(repo.mostWatchedMovie()) }
    val mostPlayed = liveData { emit(repo.mostPlayedMovie()) }
    val anticipated = liveData { emit(repo.mostAnticipatedMovie()) }
    val recommended = liveData { emit(repo.recommendedMovie()) }

    fun anythingReally(value: String) = viewModelScope.launch {
        // repository suspend call
    }

    val someVal = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit("some initial loading text")
        emitSource(liveData { emit("dataSource.fetchLiveData() - obviously without the liveData builder") })
    }

//    val someBetterValue: LiveData<String> =
//        dataSource.getFlow()
//            .onStart { emit("loading string") }
//            .asLiveData()

//    // continuous
//    val trenidng = liveData {
//        repo.trendingMoviesFlow().collect {
//            emit(it)
//        }
//    }

}