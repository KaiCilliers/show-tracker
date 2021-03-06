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

sealed class EventDetailMovie {
    data class ShowConfirmationDialogUnwatch(val movieId: String, val title: String) : EventDetailMovie()
    object Close : EventDetailMovie()
    data class ShowToast(val msg: String) : EventDetailMovie()
    data class SaveSnackbarMessage(val message: String) : EventDetailMovie()
    data class ShowConfirmationDialog(val movieId: String, val title: String) : EventDetailMovie()

    companion object {
        fun saveSnackbarMessage(message: String) = SaveSnackbarMessage(message)
        fun close() = Close
        fun showToast(message: String) = ShowToast(message)
        fun showConfirmationDialog(movieId: String, title: String) = ShowConfirmationDialog(movieId, title)
        fun showConfirmationDialogUnwatch(movieId: String, title: String) = ShowConfirmationDialogUnwatch(movieId, title)
    }
}