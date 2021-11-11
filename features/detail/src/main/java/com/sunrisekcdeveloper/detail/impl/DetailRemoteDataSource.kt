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

package com.sunrisekcdeveloper.detail.impl

import com.sunrisekcdeveloper.detail.DetailRemoteDataSourceContract
import com.sunrisekcdeveloper.detail.DetailServiceContract
import com.sunrisekcdeveloper.detail.extras.*
import com.sunrisekcdeveloper.network.NetworkContract
import com.sunrisekcdeveloper.network.NetworkResult

class DetailRemoteDataSource(
    private val api: DetailServiceContract,
    private val network: NetworkContract
) : DetailRemoteDataSourceContract {
    override suspend fun movieDetails(id: String): NetworkResult<ResponseMovieDetail> = network.request {
        // todo pass api key from BuildConfig
        api.movieDetails(id = id)
    }

    override suspend fun movieReleaseDates(id: String): NetworkResult<EnvelopeMovieReleaseDates> = network.request {
        api.movieCertifications(id = id)
    }

    override suspend fun showDetail(id: String): NetworkResult<ResponseShowDetail> = network.request {
        api.showDetails(id = id)
    }

    override suspend fun showCertification(id: String): NetworkResult<EnvelopeShowCertification> = network.request {
        api.showCertifications(id = id)
    }

    override suspend fun showWithSeasons(showId: String): NetworkResult<ResponseShowDetailWithSeasons> = network.request {
        api.showWithSeasons(showId)
    }
}