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

package com.sunrisekcdeveloper.showtracker.features.detail

import com.sunrisekcdeveloper.showtracker.BuildConfig
import com.sunrisekcdeveloper.showtracker.data.network.model.base.ResponseImages
import com.sunrisekcdeveloper.showtracker.data.network.model.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.data.network.model.full.ResponseFullMovie
import com.sunrisekcdeveloper.showtracker.di.NetworkModule
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DetailApi
import com.sunrisekcdeveloper.showtracker.util.datastate.Resource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import timber.log.Timber
import java.lang.Exception

class DetailClient(
    @DetailApi private val api: DetailServiceContract
) : DetailDataSourceContract {
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

    override suspend fun relatedMovies(slug: String): Resource<List<ResponseMovie>> = result {
        api.moviesRelatedTo(slug)
    }
    override suspend fun poster(id: String): Resource<ResponseImages> = result {
        api.poster(id)
    }
    override suspend fun detailedMovie(slug: String, extended: String): Resource<ResponseFullMovie> = result {
        api.movieFull(slug, extended)
    }
}

interface DetailDataSourceContract {
    suspend fun relatedMovies(slug: String): Resource<List<ResponseMovie>>
    suspend fun poster(id: String): Resource<ResponseImages>
    suspend fun detailedMovie(slug: String, extended: String = ""): Resource<ResponseFullMovie>
}

interface DetailService : DetailServiceContract {
    @GET("movies/{id}")
    override suspend fun movieFull(
        @Path("id") id: String,
        @Query("extended") extended: String
    ): Response<ResponseFullMovie>


    @GET("movies/{id}/related")
    override suspend fun moviesRelatedTo(@Path("id") id: String): Response<List<ResponseMovie>>

    /** IMAGES */
    @Headers("Fanart-Api: true")
    @GET("${BuildConfig.FANART_BASE_URL}movies/{id}?api_key=${BuildConfig.FANART_API_KEY}")
    override suspend fun poster(@Path("id") id: String): Response<ResponseImages>
}

interface DetailServiceContract {
    /**
     * Movies related to a specific movie
     * Supports Pagination, Extended Info
     *
     * @param id
     * @return
     */
    suspend fun moviesRelatedTo(id: String): Response<List<ResponseMovie>>
    suspend fun movieFull(id: String, extended: String = ""): Response<ResponseFullMovie>
    /** IMAGES */
    suspend fun poster(id: String): Response<ResponseImages>
}