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

import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.EventDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType

sealed class EventSearch {
    object PopBackStack : EventSearch()
    data class LoadMediaDetails(val mediaId: String, val title: String, val posterPath: String, val type: MediaType) : EventSearch()
    data class ShowToast(val msg: String) : EventSearch()
    data class ShowSnackBar(val message: String) : EventSearch()

    companion object {
        fun showSnackBar(message: String) = ShowSnackBar(message)
        fun popBackStack() = PopBackStack
        fun loadMediaDetails(mediaId: String, title: String, posterPath: String, type: MediaType) =
            LoadMediaDetails(mediaId, title, posterPath, type)
        fun showToast(message: String) = ShowToast(message)
    }
}