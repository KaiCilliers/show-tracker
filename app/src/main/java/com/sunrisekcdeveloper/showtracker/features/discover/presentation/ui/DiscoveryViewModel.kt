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

package com.sunrisekcdeveloper.showtracker.features.discover.presentation.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.DiscoveryRepo
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.UseCase
import com.sunrisekcdeveloper.showtracker.features.discover.application.LoadFeaturedMediaUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.discover.domain.repository.DiscoveryRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.discover.domain.usecase.LoadFeaturedMediaUseCase
import com.sunrisekcdeveloper.showtracker.tmdb.model.EnvelopePageMovieTMDB
import com.sunrisekcdeveloper.showtracker.tmdb.model.RepositoryTMDB
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * Home ViewModel
 *
 * @constructor Create empty Home view model
 */
@ExperimentalCoroutinesApi
class DiscoveryViewModel @ViewModelInject constructor(
    @UseCase private val loadFeaturedMediaUseCase: LoadFeaturedMediaUseCaseContract
) : ViewModel() {
    private val _tmdb2 = MutableLiveData<Resource<EnvelopePageMovieTMDB>>()
    val tmdb2: LiveData<Resource<EnvelopePageMovieTMDB>>
        get() = _tmdb2

    private val _tmdbTopRated = MutableLiveData<Resource<EnvelopePageMovieTMDB>>()
    val tmdbTopRated: LiveData<Resource<EnvelopePageMovieTMDB>>
        get() = _tmdbTopRated

    private val _tmdbUpcoming = MutableLiveData<Resource<EnvelopePageMovieTMDB>>()
    val tmdbUpcoming: LiveData<Resource<EnvelopePageMovieTMDB>>
        get() = _tmdbUpcoming

    var popularMoviesPage = 1
    var topRatedMoviesPage = 1
    var upcomingMoviesPage = 1

    val featured = liveData {
        emitSource(loadFeaturedMediaUseCase.invoke().asLiveData())
    }

//    val tmdb = liveData { emit(getPopularMovies()) }

    suspend fun getPopularMovies() = withContext(Dispatchers.IO){
        val result = RepositoryTMDB().popularMovies(++popularMoviesPage)
        Timber.d("Gots some stuff TMDB: $result")
        withContext(Dispatchers.Main) {
            _tmdb2.value = result
        }
    }
    suspend fun getTopRatedMovies() = withContext(Dispatchers.IO){
        val result = RepositoryTMDB().topRatedMovies(++topRatedMoviesPage)
        Timber.d("Gots some stuff TMDB: $result")
        withContext(Dispatchers.Main) {
            _tmdbTopRated.value = result
        }
    }
    suspend fun getUpcomingMovies() = withContext(Dispatchers.IO){
        val result = RepositoryTMDB().upcomingMovies(++upcomingMoviesPage)
        Timber.d("Gots some stuff TMDB: $result")
        withContext(Dispatchers.Main) {
            _tmdbUpcoming.value = result
        }
    }
}
