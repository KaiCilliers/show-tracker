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

package com.sunrisekcdeveloper.showtracker.features.discover

import com.sunrisekcdeveloper.showtracker.BuildConfig
import com.sunrisekcdeveloper.showtracker.commons.data.network.model.base.ResponseImages
import com.sunrisekcdeveloper.showtracker.commons.data.network.model.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.commons.data.network.model.envelopes.*
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DiscoveryApi
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import timber.log.Timber
import java.lang.Exception

class DiscoveryClient(
    @DiscoveryApi private val api: DiscoveryServiceContract
) : DiscoveryDataSourceContract {
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
    // TODO move to commons or utils
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

    override suspend fun poster(id: String): Resource<ResponseImages> = result {
        api.poster(id)
    }
}

interface DiscoveryDataSourceContract {
    suspend fun fetchBox(): Resource<List<EnvelopeRevenue>>
    suspend fun fetchTrend(): Resource<List<EnvelopeWatchers>>
    suspend fun fetchPop(): Resource<List<ResponseMovie>>
    suspend fun fetchMostPlayed(): Resource<List<EnvelopeViewStats>>
    suspend fun fetchMostWatched(): Resource<List<EnvelopeViewStats>>
    suspend fun fetchAnticipated(): Resource<List<EnvelopeListCount>>
    suspend fun fetchRecommended(): Resource<List<EnvelopeUserCount>>

    suspend fun poster(id: String): Resource<ResponseImages>
}

interface DiscoveryServiceContract {
    /**
     * Trending movies all movies currently being watched sorted with most
     * watched returned first
     * Supports Pagination, Extended, Filter
     *
     * @return
     */
    suspend fun trendingMovies(): Response<List<EnvelopeWatchers>>

    /**
     * Popular movies popularity calculated using rating percentage and the number
     * of ratings
     * Supports Pagination, Extended, Filter
     *
     * @return
     */
    suspend fun popularMovies(): Response<List<ResponseMovie>>

    /**
     * Recommended movies in specified time period
     * Supports Pagination, Extended, Filter
     *
     * TODO implement the optional query parameters to calls that can be Extended
     *
     * @param period Optional.
     *      Possible values:  daily , weekly , monthly , yearly , all
     *      Default value: weekly
     * @return
     */
    suspend fun recommendedMovies(
        period: String = "weekly",
        extended: String = ""
    ): Response<List<EnvelopeUserCount>>

    /**
     * Most played movies in specified time period (single account can watch multiple times)
     * Supports Pagination, Extended, Filter
     *
     * @param period Optional.
     *      Possible values:  daily , weekly , monthly , yearly , all
     *      Default value: weekly
     * @return
     */
    suspend fun mostPlayedMovies(period: String = "weekly"): Response<List<EnvelopeViewStats>>

    /**
     * Most watched movies in specified time period (unique watches)
     * Supports Pagination, Extended, Filter
     *
     * @param period Optional.
     *      Possible values:  daily , weekly , monthly , yearly , all
     *      Default value: weekly
     * @return
     */
    // TODO you can create less methods in Repo that takes params which will decide which of
    //  these methods to call (like it'll choose between most played and most watched)
    suspend fun mostWatchedMovies(period: String = "weekly"): Response<List<EnvelopeViewStats>>

    /**
     * Most anticipated based on number of lists movie appears in
     * Supports Pagination, Extended, Filter
     *
     * @return
     */
    suspend fun mostAnticipated(): Response<List<EnvelopeListCount>>

    /**
     * Box office based on top 10 grossing movies in U.S. box office the weekend past. Updates
     * every Monday morning
     * Supports Extended
     *
     * @return
     */
    suspend fun boxOffice(): Response<List<EnvelopeRevenue>>

    /** IMAGES */
    suspend fun poster(id: String): Response<ResponseImages>
}

interface DiscoveryService : DiscoveryServiceContract {
    @GET("movies/trending")
    override suspend fun trendingMovies(): Response<List<EnvelopeWatchers>>

    @GET("movies/popular")
    override suspend fun popularMovies(): Response<List<ResponseMovie>>

    // TODO implement this on all Extended calls
    @GET("movies/recommended/{period}")
    override suspend fun recommendedMovies(
        @Path("period") period: String,
        @Query("extended") extended: String
    ): Response<List<EnvelopeUserCount>>

    @GET("movies/played/{period}")
    override suspend fun mostPlayedMovies(@Path("period") period: String): Response<List<EnvelopeViewStats>>

    @GET("movies/watched/{period}")
    override suspend fun mostWatchedMovies(@Path("period") period: String): Response<List<EnvelopeViewStats>>

    @GET("movies/anticipated")
    override suspend fun mostAnticipated(): Response<List<EnvelopeListCount>>

    @GET("movies/boxoffice")
    override suspend fun boxOffice(): Response<List<EnvelopeRevenue>>

    /** IMAGES */
    @Headers("Fanart-Api: true")
    @GET("${BuildConfig.FANART_BASE_URL}movies/{id}?api_key=${BuildConfig.FANART_API_KEY}")
    override suspend fun poster(@Path("id") id: String): Response<ResponseImages>
}
