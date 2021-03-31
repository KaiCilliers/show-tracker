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

package com.sunrisekcdeveloper.showtracker.features.progress.data.network

import com.sunrisekcdeveloper.showtracker.common.util.NetworkResult
import com.sunrisekcdeveloper.showtracker.common.base.RemoteDataSourceBase
import com.sunrisekcdeveloper.showtracker.features.progress.data.model.ResponseShowDetailWithSeasons
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RemoteDataSourceProgress(
    private val api: ServiceProgressContract,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteDataSourceProgressContract, RemoteDataSourceBase(dispatcher) {
    override suspend fun showWithSeasons(showId: String): NetworkResult<ResponseShowDetailWithSeasons> = safeApiCall {
        api.showWithSeasons(showId)
    }

    override suspend fun seasonDetails(showId: String, season: Int) = safeApiCall {
        api.seasonsWithEpisodes(showId, season)
    }
}