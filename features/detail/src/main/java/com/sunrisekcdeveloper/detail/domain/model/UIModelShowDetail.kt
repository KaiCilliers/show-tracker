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

data class UIModelShowDetail(
    val id: String,
    val name: String,
    val posterPath: String,
    val overview: String,
    val certification: String,
    val firstAirDate: String,
    val seasonsTotal: Int,
    val watchlist: ShowWatchlistStatus,
    val status: ShowStatus
) {
    companion object {
        fun create(amount: Int): List<UIModelShowDetail> {
            val models = mutableListOf<UIModelShowDetail>()
            repeat(amount) {
                models.add(
                    UIModelShowDetail(
                    id = "id$it",
                    name = "name$it",
                    posterPath = "posterPath$it",
                    overview = "overview$it",
                    firstAirDate = "firstAirDate$it",
                    certification = "certification$it",
                    seasonsTotal = 1,
                        watchlist = ShowWatchlistStatus.NotWatchlisted,
                    status = ShowStatus.NotStarted
                )
                )
            }
            return models.toList()
        }
        fun single() = create(1)[0]
    }
}

sealed class ShowWatchlistStatus {
    object Watchlisted : ShowWatchlistStatus()
    object NotWatchlisted : ShowWatchlistStatus()
}
sealed class ShowStatus {
    object NotStarted : ShowStatus()
    object Started : ShowStatus()
    object UpToDate : ShowStatus()
}
