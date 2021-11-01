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

package com.sunrisekcdeveloper.progress.impl

import com.sunrisekcdeveloper.network.NetworkContract
import com.sunrisekcdeveloper.network.NetworkResult
import com.sunrisekcdeveloper.progress.ProgressRemoteDataSourceContract
import com.sunrisekcdeveloper.progress.ProgressServiceContract
import com.sunrisekcdeveloper.progress.extras.model.ResponseShowDetailWithSeasons
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ProgressRemoteDataSource(
    private val api: ProgressServiceContract,
    private val network: NetworkContract
) : ProgressRemoteDataSourceContract {
    override suspend fun showWithSeasons(showId: String): NetworkResult<ResponseShowDetailWithSeasons> = network.request {
        api.showWithSeasons(showId)
    }

    override suspend fun seasonDetails(showId: String, season: Int) = network.request {
        api.seasonsWithEpisodes(showId, season)
    }
}