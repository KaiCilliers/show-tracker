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

package com.sunrisekcdeveloper.watchlist.impl

import com.sunrisekcdeveloper.network.NetworkContract
import com.sunrisekcdeveloper.network.NetworkResult
import com.sunrisekcdeveloper.watchlist.WatchlistRemoteDataSourceContract
import com.sunrisekcdeveloper.watchlist.WatchlistServiceContract
import com.sunrisekcdeveloper.watchlist.extras.EnvelopeShowCertification
import com.sunrisekcdeveloper.watchlist.extras.ResponseEpisode
import com.sunrisekcdeveloper.watchlist.extras.ResponseSeason
import com.sunrisekcdeveloper.watchlist.extras.ResponseShowDetail

class WatchlistRemoteDataSource(
    private val api: WatchlistServiceContract,
    private val network: NetworkContract
) : WatchlistRemoteDataSourceContract {
    override suspend fun episodeDetails(
        showId: String,
        season: Int,
        episode: Int
    ): NetworkResult<ResponseEpisode> = network.request {
        api.episodeDetails(showId, season, episode)
    }

    override suspend fun season(showId: String, season: Int): NetworkResult<ResponseSeason> = network.request {
        api.season(showId, season)
    }

    override suspend fun showDetail(id: String): NetworkResult<ResponseShowDetail> = network.request {
        api.showDetails(id)
    }

    override suspend fun showCertification(id: String): NetworkResult<EnvelopeShowCertification> = network.request {
        api.showCertifications(id)
    }
}