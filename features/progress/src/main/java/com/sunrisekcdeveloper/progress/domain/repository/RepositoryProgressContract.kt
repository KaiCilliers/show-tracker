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

package com.sunrisekcdeveloper.progress.domain.repository

import com.sunrisekcdeveloper.cache.common.Resource
import timber.log.Timber

// todo redo naming conventions for Interfaces - remove contract suffix and implementations can have suffix "Base" if no other name can be given
interface RepositoryProgressContract {
    suspend fun cacheEntireShow(showId: String)
    suspend fun showSeasons(showId: String): Resource<Map<Int, List<Int>>>
    suspend fun setShowProgress(showId: String, season: Int, episode: Int)
    suspend fun setNewShowAsUpToDate(showId: String)
    class Fake() : RepositoryProgressContract {
        var expectException = false
        override suspend fun cacheEntireShow(showId: String) {
            Timber.d("Fake - cached Show (id=$showId)")
        }

        override suspend fun showSeasons(showId: String): Resource<Map<Int, List<Int>>> {
            return if (expectException) Resource.error("Fake - Error fetching Show (id=$showId) seasons")
            else Resource.success(
                mapOf(
                    1 to listOf(1,2,3,4,5,6),
                    2 to listOf(1,2,3,4,5,6,7)
                )
            )
        }

        override suspend fun setShowProgress(showId: String, season: Int, episode: Int) {
            Timber.d("Fake - Set Show (id=$showId) season (number=$season) episode (number=$episode) progress")
        }

        override suspend fun setNewShowAsUpToDate(showId: String) {
            Timber.d("Fake - Set Show (id=$showId) as up to date")
        }
    }
}