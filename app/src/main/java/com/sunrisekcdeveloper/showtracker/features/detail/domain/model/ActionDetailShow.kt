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

package com.sunrisekcdeveloper.showtracker.features.detail.domain.model

sealed class ActionDetailShow {
    data class AttemptRemove(val showId: String, val title: String) : ActionDetailShow()
    data class Load(val showId: String) : ActionDetailShow()
    data class Add(val showId: String) : ActionDetailShow()
    data class Remove(val showId: String) : ActionDetailShow()
    data class StartWatching(val showId: String) : ActionDetailShow()
    data class UpdateProgress(val showId: String) : ActionDetailShow()
    object Close : ActionDetailShow()
    data class ShowToast(val msg: String) : ActionDetailShow()

    companion object {
        fun attemptRemove(showId: String, title: String) = AttemptRemove(showId, title)
        fun load(showId: String) = Load(showId)
        fun add(showId: String) = Add(showId)
        fun remove(showId: String) = Remove(showId)
        fun startWatching(showId: String) = StartWatching(showId)
        fun updateProgress(showId: String) = UpdateProgress(showId)
        fun close() = Close
        fun showToast(message: String) = ShowToast(message)
    }
}
