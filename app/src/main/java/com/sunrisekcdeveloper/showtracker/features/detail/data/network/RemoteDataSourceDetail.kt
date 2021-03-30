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

package com.sunrisekcdeveloper.showtracker.features.detail.data.network

import com.sunrisekcdeveloper.showtracker.common.NetworkResult
import com.sunrisekcdeveloper.showtracker.common.base.RemoteDataSourceBase
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.EnvelopeMovieReleaseDates
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.EnvelopeShowCertification
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseMovieDetail
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseShowDetail
import com.sunrisekcdeveloper.showtracker.updated.features.detail.data.network.RemoteDataSourceDetailContract
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RemoteDataSourceDetail(
    private val api: ServiceDetailContract,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteDataSourceDetailContract, RemoteDataSourceBase(dispatcher) {
    override suspend fun movieDetails(id: String): NetworkResult<ResponseMovieDetail> = safeApiCall {
        api.movieDetail(id = id)
    }

    override suspend fun movieReleaseDates(id: String): NetworkResult<EnvelopeMovieReleaseDates> = safeApiCall {
        api.movieCertification(id = id)
    }

    override suspend fun showDetail(id: String): NetworkResult<ResponseShowDetail> = safeApiCall {
        api.showDetail(id = id)
    }

    override suspend fun showCertification(id: String): NetworkResult<EnvelopeShowCertification> = safeApiCall {
        api.showCertification(id = id)
    }
}