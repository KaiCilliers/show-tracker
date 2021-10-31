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

package com.sunrisekcdeveloper.discovery.usecase

import androidx.paging.PagingData
import com.sunrisekcdeveloper.discovery.extras.UIModelPoster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface LoadUpcomingMoviesUseCaseContract {
    operator fun invoke(): Flow<PagingData<UIModelPoster>>
    class Fake() : LoadUpcomingMoviesUseCaseContract {
        override fun invoke(): Flow<PagingData<UIModelPoster>> {
            return flowOf(PagingData.from(UIModelPoster.create(20)))
        }
    }
}