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

package com.sunrisekcdeveloper.discovery.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.sunrisekcdeveloper.discovery.UIModelPoster
import com.sunrisekcdeveloper.discovery.application.LoadPopularShowsUseCaseContract
import com.sunrisekcdeveloper.discovery.asUIModelPosterResult
import com.sunrisekcdeveloper.discovery.domain.repository.RepositoryDiscoveryContract
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class LoadPopularShowsUseCase(
    private val discoveryRepo: RepositoryDiscoveryContract
) : LoadPopularShowsUseCaseContract {
    override fun invoke(): Flow<PagingData<UIModelPoster>> =
        discoveryRepo.popularShowsStream()
            .map { pagingData -> pagingData.map { it.asUIModelPosterResult() } }
}