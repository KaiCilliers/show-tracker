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

package com.sunrisekcdeveloper.detail.application

import com.sunrisekcdeveloper.cache.MediaType
import timber.log.Timber

interface RemoveMediaFromWatchlistUseCaseContract {
    suspend operator fun invoke(id: String, type: MediaType)
    class Fake() : RemoveMediaFromWatchlistUseCaseContract {
        override suspend fun invoke(id: String, type: MediaType) {
            when (type) {
                MediaType.Movie -> { Timber.d("Fake - Removed Movie (id=$id) from Watchlist") }
                MediaType.Show -> { Timber.d("Fake - Removed Show (id=$id) from Watchlist") }
            }
        }
    }
}