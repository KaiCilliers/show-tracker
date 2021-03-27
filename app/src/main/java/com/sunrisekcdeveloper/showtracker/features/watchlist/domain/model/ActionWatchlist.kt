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

sealed class ActionWatchlist{
    object LoadWatchlistData : ActionWatchlist()
    data class LoadMediaDetails(val mediaId: String, val title: String, val posterPath: String, val type: MediaType) : ActionWatchlist()
    data class MarkMovieWatched(val movieId: String) : ActionWatchlist()
    data class MarkMovieUnWatched(val movieId: String) : ActionWatchlist()
    data class UpdateShowProgress(val instructions: UpdateShowAction) : ActionWatchlist()
    data class StartWatchingShow(val showId: String): ActionWatchlist()
    data class ShowToast(val msg: String) : ActionWatchlist()

    companion object {
        fun loadWatchlistData() = LoadWatchlistData
        fun loadMediaDetails(mediaId: String, title: String, posterPath: String, type: MediaType) =
            LoadMediaDetails(mediaId, title, posterPath, type)
        fun markMovieAsWatched(movieId: String) = MarkMovieWatched(movieId)
        fun markMovieAsUnwatched(movieId: String) = MarkMovieUnWatched(movieId)
        fun updateShowProgress(instructions: UpdateShowAction) = UpdateShowProgress(instructions)
        fun startWatchingShow(showId: String) = StartWatchingShow(showId)
        fun showToast(message: String) = ShowToast(message)
    }
}