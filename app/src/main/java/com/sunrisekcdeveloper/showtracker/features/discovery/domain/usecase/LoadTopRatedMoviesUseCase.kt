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

package com.sunrisekcdeveloper.showtracker.features.discovery.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.sunrisekcdeveloper.showtracker.common.util.asUIModelPosterResult
import com.sunrisekcdeveloper.showtracker.features.discovery.application.LoadTopRatedMoviesUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.repository.RepositoryDiscoveryContract
import com.sunrisekcdeveloper.showtracker.features.search.domain.model.UIModelPoster
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class LoadTopRatedMoviesUseCase(
    private val discoveryRepo: RepositoryDiscoveryContract
) : LoadTopRatedMoviesUseCaseContract {
    override fun invoke(): Flow<PagingData<UIModelPoster>> =
        discoveryRepo.topRatedMoviesStream()
            .map { pagingData -> pagingData.map { it.asUIModelPosterResult() } }
}