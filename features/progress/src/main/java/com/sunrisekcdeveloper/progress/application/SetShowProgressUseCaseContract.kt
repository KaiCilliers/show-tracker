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

package com.sunrisekcdeveloper.progress.application

import com.sunrisekcdeveloper.progress.domain.model.SetShowProgress
import timber.log.Timber

interface SetShowProgressUseCaseContract {
    suspend operator fun invoke(progress: SetShowProgress)
    class Fake() : SetShowProgressUseCaseContract {
        override suspend fun invoke(progress: SetShowProgress) {
            when (progress) {
                is SetShowProgress.Partial -> { Timber.d("Fake - Setting Show (id=${progress.showId} Partial") }
                is SetShowProgress.UpToDate -> { Timber.d("Fake - Setting Show (id=${progress.showId} Up to date") }
            }
        }
    }
}