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

package com.sunrisekcdeveloper.detail.usecase

import com.sunrisekcdeveloper.cache.MediaType
import com.sunrisekcdeveloper.common.timber

interface AddMediaToWatchlistUseCaseContract {
    suspend operator fun invoke(id: String, type: MediaType)
    class Fake() : AddMediaToWatchlistUseCaseContract {
        private val log by timber()
        override suspend fun invoke(id: String, type: MediaType) {
            when (type) {
                MediaType.Movie -> { log.d("Fake - Add Movie (id=$id) to Watchlist") }
                MediaType.Show -> { log.d("Fake - Add Show (id=$id) to Watchlist") }
            }
        }
    }
}