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

package com.sunrisekcdeveloper.show.season

data class WatchlistSeason(
    val showId: String,
    val status: Status,
    val meta: WatchlistMeta
)
data class Status(
    val number: Int,
    val completed: Boolean,
    val currentEpisode: Int
)
data class WatchlistMeta(
    val dateStarted: Long,
    val dateCompleted: Long,
    val startedTrackingSeason: Boolean,
    val finishedBeforeTracking: Boolean,
    val lastUpdated: Long
)