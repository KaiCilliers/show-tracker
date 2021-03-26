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

package com.sunrisekcdeveloper.showtracker.features.search.domain.model

import androidx.paging.PagingData
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery

sealed class StateSearch {
    data class EmptySearch(val data: List<UIModelUnwatchedSearch>) : StateSearch()
    object NoResultsFound : StateSearch()
    object Loading : StateSearch()
    data class Success(val data: PagingData<UIModelDiscovery>) : StateSearch()
    data class Error(val exception: Exception) : StateSearch()

    companion object {
        fun emptySearch(unWatchedList: List<UIModelUnwatchedSearch>) = EmptySearch(unWatchedList)
        fun noResultsFound() = NoResultsFound
        fun loading() = Loading
        fun success(pagingData: PagingData<UIModelDiscovery>) = Success(pagingData)
        fun error(errorMessage: String) = Error(Exception(errorMessage))
    }
}