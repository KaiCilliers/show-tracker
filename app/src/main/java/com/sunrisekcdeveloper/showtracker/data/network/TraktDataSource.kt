/*
 * Copyright © 2020. The Android Open Source Project
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

package com.sunrisekcdeveloper.showtracker.data.network

import com.sunrisekcdeveloper.showtracker.data.network.model.base.ResponseImages
import com.sunrisekcdeveloper.showtracker.data.network.model.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.data.network.model.base.ResponsePoster
import com.sunrisekcdeveloper.showtracker.data.network.model.envelopes.*
import com.sunrisekcdeveloper.showtracker.di.NetworkModule
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.TraktApi
import com.sunrisekcdeveloper.showtracker.util.datastate.Resource
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class TraktDataSource @Inject constructor(
    @TraktApi private val api: ApiServiceContract
) : NetworkDataSourceContract {

    override suspend fun poster(id: String): Resource<ResponseImages> = result {
        api.poster(id)
    }

    override suspend fun search(type: String, searchText: String, field: String) = result {
        api.search(type, searchText, field)
    }

    override suspend fun fetchBox(): Resource<List<EnvelopeRevenue>> = result {
        api.boxOffice()
    }

    override suspend fun fetchTrend(): Resource<List<EnvelopeWatchers>> = result {
        api.trendingMovies()
    }

    override suspend fun fetchPop(): Resource<List<ResponseMovie>> = result {
        api.popularMovies()
    }

    override suspend fun fetchMostPlayed(): Resource<List<EnvelopeViewStats>> = result {
        api.mostPlayedMovies()
    }

    override suspend fun fetchMostWatched(): Resource<List<EnvelopeViewStats>> = result {
        api.mostWatchedMovies()
    }

    override suspend fun fetchAnticipated(): Resource<List<EnvelopeListCount>> = result {
        api.mostAnticipated()
    }

    override suspend fun fetchRecommended(): Resource<List<EnvelopeUserCount>> = result {
        api.recommendedMovies()
    }

    // TODO this logic should be at the repo level to include updating cache logic
    //  val wrappedResult = safeApiCall(Dispatchers.IO) { api.getRandomDogBreed() }
    //        when (wrappedResult) {
    //            is ResultWrapper.Success<*> -> {
    //                val dogResponse = wrappedResult.value as ApiResponse<String>
    //                val breedImageUrl = dogResponse.message
    //                val dog = extractBreedName(breedImageUrl)?.let { Dog(it, breedImageUrl) }
    //                dog?.run {
    //                    dogDao.save(this)
    //                }
    //            }
    //        }
    //        return wrappedResult
    private suspend fun <T> result(request: suspend () -> Response<T>): Resource<T> {
        return try {
            val response = request()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return Resource.Success(it)
                }
            }
            error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            error(e.message ?: e.toString(), e)
        }
    }

    private fun <T> error(message: String, e: Exception): Resource<T> {
        Timber.e(message)
        return Resource.Error("Network call has failed for the following reason: $message")
    }
}