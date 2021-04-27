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
import com.sunrisekcdeveloper.showtracker.features.progress.data.model.ResponseSeasonDetailWithEpisodes
import com.sunrisekcdeveloper.showtracker.features.progress.data.model.ResponseShowDetailWithSeasons

interface RemoteDataSourceProgressContract {
    suspend fun showWithSeasons(showId: String): NetworkResult<ResponseShowDetailWithSeasons>
    suspend fun seasonDetails(showId: String, season: Int): NetworkResult<ResponseSeasonDetailWithEpisodes>
    class Fake() : RemoteDataSourceProgressContract {
        var expectException = false
        override suspend fun showWithSeasons(showId: String): NetworkResult<ResponseShowDetailWithSeasons> {
            return if (expectException) NetworkResult.error("Fake - Could not fetch Show (id=$showId)")
            else NetworkResult.success(ResponseShowDetailWithSeasons.single())
        }

        override suspend fun seasonDetails(
            showId: String,
            season: Int
        ): NetworkResult<ResponseSeasonDetailWithEpisodes> {
            return if (expectException) NetworkResult.error("Fake - Could not fetch season (number=$season) from Show (id=$showId)")
            else NetworkResult.success(ResponseSeasonDetailWithEpisodes.single())
        }
    }
}