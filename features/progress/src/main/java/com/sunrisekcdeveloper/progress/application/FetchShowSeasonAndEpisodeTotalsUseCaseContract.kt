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

import com.sunrisekcdeveloper.cache.common.Resource

// todo refactor interface and class names
interface FetchShowSeasonAndEpisodeTotalsUseCaseContract {
    suspend operator fun invoke(showId: String): Resource<Map<Int, List<Int>>>
    class Fake() : FetchShowSeasonAndEpisodeTotalsUseCaseContract {
        var expectException = false
        override suspend fun invoke(showId: String): Resource<Map<Int, List<Int>>> {
            return if (expectException) Resource.error("Fake - Could not fetch Show (id=$showId) information")
            else Resource.success(
                mapOf(
                    1 to listOf(1,2,3,4,5,6),
                    2 to listOf(1,2,3,4,5,6,7)
                )
            )
        }
    }
}