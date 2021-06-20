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

package com.sunrisekcdeveloper.detail.domain.model

data class UIModelMovieDetail(
    val id: String,
    val title: String,
    val posterPath: String,
    val overview: String,
    val releaseYear: String,
    val certification: String,
    val runtime: String,
    val status: MovieWatchlistStatus,
    val watched: WatchedStatus
) {
    companion object {
        fun create(amount: Int): List<UIModelMovieDetail> {
            val models = mutableListOf<UIModelMovieDetail>()
            repeat(amount) {
                models.add(
                    UIModelMovieDetail(
                 id = "id$it",
                 title = "title$it",
                 posterPath = "posterPath$it",
                 overview = "overview$it",
                    releaseYear = "releaseYear$it",
                    certification = "certification$it",
                    runtime = "runtime$it",
                    status = MovieWatchlistStatus.NotWatchlisted,
                    watched = WatchedStatus.NotWatched
                )
                )
            }
            return models.toList()
        }
        fun single() = create(1)[0]
    }
}

sealed class MovieWatchlistStatus {
    object Watchlisted : MovieWatchlistStatus()
    object NotWatchlisted : MovieWatchlistStatus()
}
sealed class WatchedStatus {
    object Watched : WatchedStatus()
    object NotWatched : WatchedStatus()
}