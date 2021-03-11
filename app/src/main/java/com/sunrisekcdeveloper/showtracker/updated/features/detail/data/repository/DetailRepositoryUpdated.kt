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

package com.sunrisekcdeveloper.showtracker.updated.features.detail.data.repository

import com.sunrisekcdeveloper.showtracker.common.util.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DetClient
import com.sunrisekcdeveloper.showtracker.updated.features.detail.data.network.DetailRemoteDataSourceContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.detail.data.network.ResponseMovieDetail
import com.sunrisekcdeveloper.showtracker.updated.features.detail.data.network.ResponseShowDetail
import com.sunrisekcdeveloper.showtracker.updated.features.detail.domain.model.MovieDetailUIModel
import com.sunrisekcdeveloper.showtracker.updated.features.detail.domain.model.ShowDetailUIModel
import com.sunrisekcdeveloper.showtracker.updated.features.detail.domain.repository.DetailRepositoryContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.network.NetworkResult
import kotlinx.coroutines.*
import timber.log.Timber

class DetailRepositoryUpdated(
    @DetClient private val remote: DetailRemoteDataSourceContractUpdated,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : DetailRepositoryContractUpdated {
    override suspend fun showDetails(id: String): Resource<ShowDetailUIModel> = withContext(scope.coroutineContext) {
        val detailCall = async { remote.showDetail(id) }
        val certificationCall = async { remote.showCertification(id) }

        val detailResponse = detailCall.await()
        val certificationResponse = certificationCall.await()

        Timber.d("$detailResponse")
        Timber.d("$certificationResponse")

        var partial: ShowDetailUIModel? = null
        val certs = mutableListOf<String>()

        when(detailResponse) {
            is NetworkResult.Success -> {
                partial = detailResponse.data.asShowDetailUIModel()
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

    override suspend fun movieDetails(id: String): Resource<MovieDetailUIModel> = withContext(scope.coroutineContext) {
        val detailCall = async { remote.movieDetails(id) }
        val certificationCall = async { remote.movieReleaseDates(id) }

        val detailResponse = detailCall.await()
        val certificationResponse = certificationCall.await()

        Timber.d("$detailResponse")
        Timber.d("$certificationResponse")

        var partial: MovieDetailUIModel? = null
        val certs = mutableListOf<String>()

        when(detailResponse) {
            is NetworkResult.Success -> {
                partial = detailResponse.data.asMovieDetailUIModel()
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

// todo save call to db then get entity to convert to UI
fun ResponseMovieDetail.asMovieDetailUIModel() = MovieDetailUIModel(
    id = "$id",
    title = title,
    posterPath = posterPath ?: "",
    overview = overview,
    releaseYear = releaseDate,
    certification = "",
    runtime = "$runtime",
    watchlisted = false,
    watched = false
)
fun ResponseShowDetail.asShowDetailUIModel() = ShowDetailUIModel(
    id = "$id",
    name = name,
    posterPath = posterPath,
    overview = overview,
    certification = "",
    firstAirDate = firstAirYear,
    seasonsTotal = seasonTotal,
    watchlisted = false,
    startedWatching = false,
    upToDate = false
)