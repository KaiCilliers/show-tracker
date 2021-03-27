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

sealed class ActionDetailMovie {
    data class Load(val movieId: String) : ActionDetailMovie()
    data class Add(val movieId: String) : ActionDetailMovie()
    data class Remove(val movieId: String) : ActionDetailMovie()
    data class SetWatched(val movieId: String) : ActionDetailMovie()
    data class SetUnwatched(val movieId: String) : ActionDetailMovie()
    object Close : ActionDetailMovie()
    data class ShowToast(val msg: String) : ActionDetailMovie()

    companion object {
        fun load(movieId: String) = Load(movieId)
        fun add(movieId: String) = Add(movieId)
        fun remove(movieId: String) = Remove(movieId)
        fun setWatched(movieId: String) = SetWatched(movieId)
        fun setUnwatched(movieId: String) = SetUnwatched(movieId)
        fun close() = Close
        fun showToast(message: String) = ShowToast(message)
    }
}