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

package com.sunrisekcdeveloper.showtracker.features.search

import com.sunrisekcdeveloper.showtracker.BuildConfig
import com.sunrisekcdeveloper.showtracker.data.network.model.base.ResponseImages
import com.sunrisekcdeveloper.showtracker.data.network.model.envelopes.EnvelopeSearchMovie
import com.sunrisekcdeveloper.showtracker.di.NetworkModule
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.SearchApi
import com.sunrisekcdeveloper.showtracker.util.datastate.Resource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import timber.log.Timber
import java.lang.Exception

class SearchClient(
    @SearchApi private val api: SearchServiceContract
) : SearchDataSourceContract {
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

    override suspend fun poster(id: String): Resource<ResponseImages> = result {
        api.poster(id)
    }

    private fun <T> error(message: String, e: Exception): Resource<T> {
        Timber.e(message)
        return Resource.Error("Network call has failed for the following reason: $message")
    }
    override suspend fun search(type: String, searchText: String, field: String) = result {
        api.search(type, searchText, field)
    }
}

interface SearchDataSourceContract {
    suspend fun search(type: String, searchText: String, field: String = ""): Resource<List<EnvelopeSearchMovie>>
    suspend fun poster(id: String): Resource<ResponseImages>
}

interface SearchService : SearchServiceContract {
    /** TODO SEARCH */

    @GET("search/{type}")
    override suspend fun search(
        @Path("type") type: String,
        @Query("query") searchText: String,
        @Query("field") field: String
    ): Response<List<EnvelopeSearchMovie>>

    /** IMAGES */
    @Headers("Fanart-Api: true")
    @GET("${BuildConfig.FANART_BASE_URL}movies/{id}?api_key=${BuildConfig.FANART_API_KEY}")
    override suspend fun poster(@Path("id") id: String): Response<ResponseImages>
}

interface SearchServiceContract {
    /** TODO SEARCH */
    suspend fun search(
        type: String,
        searchText: String,
        field: String = ""
    ): Response<List<EnvelopeSearchMovie>>

    /** IMAGES */
    suspend fun poster(id: String): Response<ResponseImages>
}