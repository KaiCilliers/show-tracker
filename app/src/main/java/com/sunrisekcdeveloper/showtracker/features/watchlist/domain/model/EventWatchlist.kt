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

package com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model

import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType

sealed class EventWatchlist{
    data class LoadMediaDetails(val mediaId: String, val title: String, val posterPath: String, val type: MediaType) : EventWatchlist()
    data class ConfigureShow(val showId: String, val title: String) : EventWatchlist()
    data class ShowToast(val msg: String) : EventWatchlist()

    companion object {
        fun loadMediaDetails(mediaId: String, title: String, posterPath: String, type: MediaType) =
            LoadMediaDetails(mediaId, title, posterPath, type)
        fun configureShow(showId: String, title: String) = ConfigureShow(showId, title)
        fun showToast(message: String) = ShowToast(message)
    }
}