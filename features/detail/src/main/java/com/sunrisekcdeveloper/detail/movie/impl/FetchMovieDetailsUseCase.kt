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

package com.sunrisekcdeveloper.detail.movie.impl

import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.detail.movie.usecase.FetchMovieDetailsUseCaseContract
import com.sunrisekcdeveloper.detail.extras.model.UIModelMovieDetail
import com.sunrisekcdeveloper.detail.RepositoryDetailContract
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class FetchMovieDetailsUseCase(
    private val detailRepo: RepositoryDetailContract
) : FetchMovieDetailsUseCaseContract {
    override suspend fun invoke(id: String): Flow<Resource<UIModelMovieDetail>> {
        detailRepo.updateMovieDetails(id)
        return detailRepo.movieDetails(id)
    }
}