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
    val featured = liveData {
        emitSource(loadFeaturedMediaUseCase.invoke().asLiveData())
    }
    val tmdb = liveData {
        val result = RepositoryTMDB().popularMovies()
        Timber.d("Gots some stuff TMDB: $result")
        emit(result)
    }
}
