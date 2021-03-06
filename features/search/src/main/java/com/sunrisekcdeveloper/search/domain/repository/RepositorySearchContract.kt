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

package com.sunrisekcdeveloper.search.domain.repository

import androidx.paging.PagingData
import com.sunrisekcdeveloper.cache.common.Resource
import com.sunrisekcdeveloper.search.domain.model.UIModelPoster
import com.sunrisekcdeveloper.search.domain.model.UIModelSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface RepositorySearchContract {
    suspend fun loadUnwatchedMedia() : Resource<List<UIModelPoster>>
    fun searchMediaByTitlePage(query: String): Flow<PagingData<UIModelSearch>>
    class Fake() : RepositorySearchContract {
        var expectException = false
        override suspend fun loadUnwatchedMedia(): Resource<List<UIModelPoster>> {
            return if (expectException) Resource.error("Fake - Error fetching unwatched media")
            else Resource.success(UIModelPoster.create(100))
        }

        override fun searchMediaByTitlePage(query: String): Flow<PagingData<UIModelSearch>> {
            return flowOf(PagingData.from(UIModelSearch.create(20)))
        }
    }
}