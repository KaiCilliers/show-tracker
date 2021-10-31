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

package com.sunrisekcdeveloper.progress.extras.model

sealed class SetShowProgress {
    data class Partial(
        val showId: String,
        val seasonNumber: Int,
        val episodeNumber: Int
    ) : SetShowProgress()

    data class UpToDate(val showId: String) : SetShowProgress()

    companion object {
        fun partial(showId: String, seasonNumber: Int, episodeNumber: Int) =
            Partial(showId, seasonNumber, episodeNumber)

        fun upToDate(showId: String) = UpToDate(showId)
    }
}