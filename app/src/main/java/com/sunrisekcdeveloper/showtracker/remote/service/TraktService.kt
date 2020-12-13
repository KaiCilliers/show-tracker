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

package com.sunrisekcdeveloper.showtracker.remote.service

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.sunrisekcdeveloper.showtracker.BuildConfig
import com.sunrisekcdeveloper.showtracker.remote.source.NetworkDataSource
import com.sunrisekcdeveloper.showtracker.remote.source.TraktDataSource
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

interface TraktService {
    companion object {
        private const val BASE_URL = BuildConfig.TRAKT_BASE_URL
        fun create(): NetworkDataSource {
            val loggerInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val headerAuthInterceptor = object : Interceptor {
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
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggerInterceptor)
                .addInterceptor(headerAuthInterceptor)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                ))
                .build()
                .create(TraktDataSource::class.java)
        }
    }
}