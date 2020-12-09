/*
 * Copyright © 2020. The Android Open Source Project
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

package com.sunrisekcdeveloper.showtracker.ui.moreentities

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseMovie

// TODO defs a new name - for all these wrappers - maybe WrapperViews
data class ResponseWrapperMostPlayedWatchedCollected(
    @Json(name = "watcher_count") private val watchers: Int,
    @Json(name = "play_count") private val played: Int,
    @Json(name = "collected_count") private val collected: Int,
    @Json(name = "movie") private val movie: ResponseMovie
)