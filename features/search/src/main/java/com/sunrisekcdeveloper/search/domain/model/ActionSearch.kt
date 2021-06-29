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

import com.sunrisekcdeveloper.cache.MediaType

sealed class ActionSearch {
    object DeviceIsOnline : ActionSearch()
    object DeviceIsOffline : ActionSearch()
    object BackButtonPress : ActionSearch()
    object NotifyNoSearchResults : ActionSearch()
    data class SearchForMedia(val query: String) : ActionSearch()
    data class LoadMediaDetails(val mediaId: String, val title: String, val posterPath: String, val type: MediaType) : ActionSearch()
    data class ShowToast(val msg: String) : ActionSearch()
    object LoadUnwatchedContent : ActionSearch()
    data class ShowSnackBar(val message: String) : ActionSearch()

    companion object {
        fun showSnackBar(message: String) = ShowSnackBar(message)
        fun deviceIsOnline() = DeviceIsOnline
        fun deviceIsOffline() = DeviceIsOffline
        fun backButtonPressed() = BackButtonPress
        fun notifyNoSearchResults() = NotifyNoSearchResults
        fun searchForMedia(query: String) = SearchForMedia(query)
        fun loadMediaDetails(mediaId: String, title: String, posterPath: String, type: MediaType) =
            LoadMediaDetails(mediaId, title, posterPath, type)
        fun showToast(message: String) = ShowToast(message)
        fun loadUnwatchedContent() = LoadUnwatchedContent
    }
}