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

package com.sunrisekcdeveloper.search.domain.model

import androidx.paging.PagingData

sealed class StateSearch {
    object EmptyWatchlist : StateSearch()
    data class EmptySearch(val data: List<UIModelPoster>) : StateSearch()
    object NoResultsFound : StateSearch()
    object Loading : StateSearch()
    data class Success(val data: PagingData<UIModelPoster>) : StateSearch()
    data class Error(val exception: Exception) : StateSearch()

    companion object {
        fun emptyWatchlist() = EmptyWatchlist
        fun emptySearch(unWatchedList: List<UIModelPoster>) = EmptySearch(unWatchedList)
        fun noResultsFound() = NoResultsFound
        fun loading() = Loading
        fun success(pagingData: PagingData<UIModelPoster>) = Success(pagingData)
        fun error(errorMessage: String) = Error(Exception(errorMessage))
    }
}