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

import com.sunrisekcdeveloper.showtracker.common.NetworkResult
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.common.util.asUIModelMovieDetail
import com.sunrisekcdeveloper.showtracker.common.util.asUIModelShowDetail
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.SourceDetail
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseMovieReleaseDates
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseShowCertification
import com.sunrisekcdeveloper.showtracker.updated.features.detail.data.network.RemoteDataSourceDetailContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelMovieDetail
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelShowDetail
import com.sunrisekcdeveloper.showtracker.features.detail.domain.repository.RepositoryDetailContract
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

@ExperimentalCoroutinesApi
class RepositoryDetail(
    @SourceDetail private val remote: RemoteDataSourceDetailContract,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RepositoryDetailContract {
    override suspend fun showDetails(id: String): Flow<Resource<UIModelShowDetail>> {
        val show = flow { emit(remote.showDetail(id)) }.flowOn(dispatcher)
        val cert = flow { emit(remote.showCertification(id)) }.flowOn(dispatcher)

        return combine(show, cert) { showResponse, certResponse ->
            when (showResponse) {
                is NetworkResult.Success -> {
                    var result = showResponse.data.asUIModelShowDetail()

                    when (certResponse) {
                        is NetworkResult.Success -> {
                            result = result.copy(
                                certification = showCertificationUSIfPossible(certResponse.data.results)
                            )
                        }
                        is NetworkResult.Error -> {
                            Timber.d("Error - certification call was not successful: ${certResponse.message}")
                        }
                    }

                    Resource.Success(result)
                }
                is NetworkResult.Error -> {
                    Resource.Error("Error fetching show with ID: $id [${showResponse.message}]")
                }
            }
        }.onStart { emit(Resource.Loading) }
    }

    override suspend fun movieDetails(id: String): Flow<Resource<UIModelMovieDetail>> {
        val movieFlow = flow { emit(remote.movieDetails(id)) }.flowOn(dispatcher)
        val certificationFlow = flow { emit(remote.movieReleaseDates(id)) }.flowOn(dispatcher)

        return combine(movieFlow, certificationFlow) { movieResult, certificationResult ->
            when (movieResult) {
                is NetworkResult.Success -> {
                    var result = movieResult.data.asUIModelMovieDetail()

                    when (certificationResult) {
                        is NetworkResult.Success -> {
                            result = result.copy(
                                certification = movieCertificationUSIfPossible(certificationResult.data.results)
                            )
                        }
                        is NetworkResult.Error -> {
                            Timber.d("Error - certification call was not successful: ${certificationResult.message}")
                        }
                    }

                    Resource.Success(result)
                }
                is NetworkResult.Error -> {
                    Resource.Error("Error fetching show with ID: $id [${movieResult.message}]")
                }
            }
        }.onStart { emit(Resource.Loading) }
    }

    private fun movieCertificationUSIfPossible(data: List<ResponseMovieReleaseDates>): String {
        return if (data.isNotEmpty()) {
            val iso = data.find { it.iso == "US" } ?: data[0]
            return if (iso.releaseDates.isNotEmpty()) {
                val certification = iso.releaseDates.find { it.releaseType == 3 }?.certification
                    ?: iso.releaseDates[0].certification
                return if (certification != "") {
                    certification
                } else {
                    "N/A"
                }
            } else {
                "N/A"
            }
        } else {
            "N/A"
        }
    }

    private fun showCertificationUSIfPossible(data: List<ResponseShowCertification>): String {
        return if (data.isNotEmpty()) {
            data.find {
                it.iso == "US"
            }?.certification ?: data[0].certification
        } else {
            "N/A"
        }
    }
}
