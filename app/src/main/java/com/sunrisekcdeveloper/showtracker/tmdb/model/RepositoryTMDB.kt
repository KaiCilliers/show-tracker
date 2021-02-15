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

package com.sunrisekcdeveloper.showtracker.tmdb.model

import com.sunrisekcdeveloper.showtracker.BuildConfig
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton

class RepositoryTMDB {
    private val api: TMDBService

    init {
        val http = OkHttpClient.Builder()
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    var request = chain.request()
                    val headers = request.headers.newBuilder()
                        .add("Content-type", "application/json")

                    if (request.header("Fanart-Api") == null) {
                        headers.add("trakt-api-key", BuildConfig.TRAKT_API_KEY)
                            .add("trakt-api-version", "2")
                    }

                    request = request.newBuilder().headers(headers.build()).build()
                    return chain.proceed(request)
                }
            })
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(http)
            .build()
        api = retrofit.create(TMDBService::class.java)
    }

    suspend fun popularMovies(page: Int = 1): Resource<EnvelopePageMovieTMDB> {
        val response = result { api.popularMovies(page = page) }
        return when(response) {
            is Resource.Success -> { Timber.d("$response"); response}
            is Resource.Error -> { Timber.d("$response"); response }
            else -> { Timber.d("Oops"); Resource.Error("Oops") }
        }
    }

    suspend fun topRatedMovies(page: Int = 1): Resource<EnvelopePageMovieTMDB> {
        val response = result { api.topRatedMovies(page = page) }
        return when(response) {
            is Resource.Success -> { Timber.d("$response"); response}
            is Resource.Error -> { Timber.d("$response"); response }
            else -> { Timber.d("Oops"); Resource.Error("Oops") }
        }
    }

    suspend fun upcomingMovies(page: Int = 1): Resource<EnvelopePageMovieTMDB> {
        val response = result { api.upcomingMovies(page = page) }
        return when(response) {
            is Resource.Success -> { Timber.d("$response"); response}
            is Resource.Error -> { Timber.d("$response"); response }
            else -> { Timber.d("Oops"); Resource.Error("Oops") }
        }
    }

    private suspend fun <T> result(request: suspend () -> retrofit2.Response<T>): Resource<T> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = request()
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        return@withContext Resource.Success(it)
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