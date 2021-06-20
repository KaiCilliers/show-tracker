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

package com.sunrisekcdeveloper.showtracker.features.detail.domain.usecase

import com.sunrisekcdeveloper.showtracker.features.detail.application.AddMediaToWatchlistUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.ActionRepositoryMovie
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.ActionRepositoryShow
import com.sunrisekcdeveloper.showtracker.features.detail.domain.repository.RepositoryDetailContract
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AddMediaToWatchlistUseCase(
    private val detailRepo: RepositoryDetailContract
) : AddMediaToWatchlistUseCaseContract {
    override suspend fun invoke(id: String, type: MediaType) {
        when (type) {
            MediaType.Movie -> { detailRepo.submitMovieAction(ActionRepositoryMovie.Add(id)) }
            MediaType.Show -> { detailRepo.submitShowAction(ActionRepositoryShow.Add(id)) }
        }
    }
}