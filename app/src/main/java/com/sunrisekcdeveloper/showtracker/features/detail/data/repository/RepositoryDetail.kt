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

package com.sunrisekcdeveloper.showtracker.features.detail.data.repository

import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.common.util.asUIModelMovieDetail
import com.sunrisekcdeveloper.showtracker.common.util.asUIModelShowDetail
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.SourceDetail
import com.sunrisekcdeveloper.showtracker.updated.features.detail.data.network.RemoteDataSourceDetailContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelMovieDetail
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelShowDetail
import com.sunrisekcdeveloper.showtracker.features.detail.domain.repository.RepositoryDetailContract
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.NetworkResult
import kotlinx.coroutines.*
import timber.log.Timber

class RepositoryDetail(
    @SourceDetail private val remote: RemoteDataSourceDetailContract,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : RepositoryDetailContract {
    override suspend fun showDetails(id: String): Resource<UIModelShowDetail> = withContext(scope.coroutineContext) {
        val detailCall = async { remote.showDetail(id) }
        val certificationCall = async { remote.showCertification(id) }

        val detailResponse = detailCall.await()
        val certificationResponse = certificationCall.await()

        Timber.d("$detailResponse")
        Timber.d("$certificationResponse")

        var partial: UIModelShowDetail? = null
        val certs = mutableListOf<String>()

        when(detailResponse) {
            is NetworkResult.Success -> {
                partial = detailResponse.data.asUIModelShowDetail()
            }
            is NetworkResult.Error -> { }
        }
        when(certificationResponse) {
            is NetworkResult.Success -> {
                certificationResponse.data.results.forEach {
                    certs.add(it.certification)
                }
            }
            is NetworkResult.Error -> { }
        }
        val whole = partial?.copy(certification = certs[0])
        return@withContext Resource.Success(whole!!)
    }

    override suspend fun movieDetails(id: String): Resource<UIModelMovieDetail> = withContext(scope.coroutineContext) {
        val detailCall = async { remote.movieDetails(id) }
        val certificationCall = async { remote.movieReleaseDates(id) }

        val detailResponse = detailCall.await()
        val certificationResponse = certificationCall.await()

        Timber.d("$detailResponse")
        Timber.d("$certificationResponse")

        var partial: UIModelMovieDetail? = null
        val certs = mutableListOf<String>()

        when(detailResponse) {
            is NetworkResult.Success -> {
                partial = detailResponse.data.asUIModelMovieDetail()
            }
            is NetworkResult.Error -> { }
        }
        when(certificationResponse) {
            is NetworkResult.Success -> {
                certificationResponse.data.results.forEach {
                    it.releaseDates.forEach {
                        Timber.d("dafuq: $it")
                        certs.add(it.certification)
                    }
                }
            }
            is NetworkResult.Error -> { }
        }
        val whole = partial?.copy(certification = certs[0])
        return@withContext Resource.Success(whole!!)
    }
}
