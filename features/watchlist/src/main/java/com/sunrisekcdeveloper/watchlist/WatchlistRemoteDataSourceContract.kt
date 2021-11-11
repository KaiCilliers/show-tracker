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

package com.sunrisekcdeveloper.watchlist

import com.sunrisekcdeveloper.network.NetworkResult
import com.sunrisekcdeveloper.watchlist.extras.*

interface WatchlistRemoteDataSourceContract {
    suspend fun episodeDetails(showId: String, season: Int, episode: Int): NetworkResult<ResponseEpisode>
    suspend fun season(showId: String, season: Int): NetworkResult<ResponseSeason>
    suspend fun seasonDetails(showId: String, season: Int): NetworkResult<ResponseSeasonDetailWithEpisodes>
    suspend fun showDetail(id: String): NetworkResult<ResponseShowDetail>
    suspend fun showCertification(id: String): NetworkResult<EnvelopeShowCertification>
}