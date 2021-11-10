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

package com.sunrisekcdeveloper.detail.extras.model

sealed class EventDetailShow {
    data class ShowConfirmationDialog(val showId: String, val title: String) : EventDetailShow()
    data class LaunchStartWatching(val showId: String, val title: String) : EventDetailShow()
    data class GoToShowInWatchlist(val showId: String) : EventDetailShow()
    object Close : EventDetailShow()
    data class ShowToast(val msg: String) : EventDetailShow()
    data class SaveSnackbarMessage(val message: String) : EventDetailShow()

    companion object {
        fun saveSnackbarMessage(message: String) = SaveSnackbarMessage(message)
        fun showConfirmationDialog(showId: String, title: String) =
            ShowConfirmationDialog(showId, title)
        fun launchStartWatching(showId: String, title: String) = LaunchStartWatching(showId, title)
        fun goToShowInWatchlist(showId: String) = GoToShowInWatchlist(showId)
        fun close() = Close
        fun showToast(message: String) = ShowToast(message)
    }
}