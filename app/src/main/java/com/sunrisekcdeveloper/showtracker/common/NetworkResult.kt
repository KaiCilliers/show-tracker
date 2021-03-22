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

package com.sunrisekcdeveloper.showtracker.common

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    // todo add optional throwable
    data class Error(val message: String) : NetworkResult<Nothing>()
    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(message: String) = Error(message)
    }
}