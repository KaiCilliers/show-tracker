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

package com.sunrisekcdeveloper.showtracker.features.progress.domain.model

sealed class ActionProgress {
    object NavigateBack : ActionProgress()
    data class SetShowProgress(
        val showId: String,
        val seasonNumber: Int,
        val episodeNumber: Int
    ) : ActionProgress()
    data class MarkShowUpToDate(val showId: String) : ActionProgress()
    data class CreateToast(val msg: String) : ActionProgress()
    data class Load(val showId: String) : ActionProgress()

    companion object {
        fun navigateBack() = NavigateBack
        fun setShowProgress(showId: String, seasonNumber: Int, episodeNumber: Int) =
            SetShowProgress(showId, seasonNumber, episodeNumber)
        fun markShowUpToDate(showId: String) = MarkShowUpToDate(showId)
        fun createToast(message: String) = CreateToast(message)
        fun load(showId: String) = Load(showId)
    }
}