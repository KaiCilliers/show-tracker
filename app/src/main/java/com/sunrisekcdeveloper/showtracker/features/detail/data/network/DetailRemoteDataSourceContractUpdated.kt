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

package com.sunrisekcdeveloper.showtracker.updated.features.detail.data.network

import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.NetworkResult

interface DetailRemoteDataSourceContractUpdated {
    suspend fun movieDetails(id: String): NetworkResult<ResponseMovieDetail>
    suspend fun movieReleaseDates(id: String) : NetworkResult<EnvelopeMovieReleaseDates>
    suspend fun showDetail(id: String): NetworkResult<ResponseShowDetail>
    suspend fun showCertification(id: String): NetworkResult<EnvelopeShowCertification>
}