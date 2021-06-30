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

package com.sunrisekcdeveloper.watchlist.domain.model

import com.sunrisekcdeveloper.watchlist.presentation.UIModelWatchlistShow

sealed class StateWatchlist {
    object NoFilterResults : StateWatchlist()
    object EmptyList : StateWatchlist()
    object Loading : StateWatchlist()
    data class Success(val movies: List<UIModelWatchlistMovie>, val shows: List<UIModelWatchlistShow>) : StateWatchlist()
    data class Error(val exception: Exception) : StateWatchlist()

    companion object {
        fun noFilterResults() = NoFilterResults
        fun emptyList() = EmptyList
        fun loading() = Loading
        fun success(moviesList: List<UIModelWatchlistMovie>, showsList: List<UIModelWatchlistShow>) =
            Success(moviesList, showsList)
        fun error(errorMessage: String) = Error(Exception(errorMessage))
    }
}