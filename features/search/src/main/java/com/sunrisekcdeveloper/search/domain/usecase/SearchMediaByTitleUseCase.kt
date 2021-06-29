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

package com.sunrisekcdeveloper.search.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.sunrisekcdeveloper.search.application.SearchMediaByTitleUseCaseContract
import com.sunrisekcdeveloper.search.asUiModelPosterResult
import com.sunrisekcdeveloper.search.domain.model.UIModelPoster
import com.sunrisekcdeveloper.search.domain.repository.RepositorySearchContract
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class SearchMediaByTitleUseCase(
    private val searchRepo: RepositorySearchContract,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SearchMediaByTitleUseCaseContract {

    override fun invoke(query: String): Flow<PagingData<UIModelPoster>> {
        return searchRepo.searchMediaByTitlePage(query)
            .map { pagingData -> pagingData.map { it.asUiModelPosterResult() } }
    }
}