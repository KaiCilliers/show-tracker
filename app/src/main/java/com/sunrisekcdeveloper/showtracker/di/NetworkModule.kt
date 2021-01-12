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

package com.sunrisekcdeveloper.showtracker.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.sunrisekcdeveloper.showtracker.BuildConfig
import com.sunrisekcdeveloper.showtracker.features.detail.client.DetailDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.detail.client.DetailClient
import com.sunrisekcdeveloper.showtracker.features.detail.client.DetailService
import com.sunrisekcdeveloper.showtracker.features.detail.client.DetailServiceContract
import com.sunrisekcdeveloper.showtracker.features.discover.client.DiscoveryClient
import com.sunrisekcdeveloper.showtracker.features.discover.client.DiscoveryDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.discover.client.DiscoveryService
import com.sunrisekcdeveloper.showtracker.features.discover.client.DiscoveryServiceContract
import com.sunrisekcdeveloper.showtracker.features.search.client.SearchClient
import com.sunrisekcdeveloper.showtracker.features.search.client.SearchDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.search.client.SearchService
import com.sunrisekcdeveloper.showtracker.features.search.client.SearchServiceContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.client.WatchlistDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.client.WatchlistService
import com.sunrisekcdeveloper.showtracker.features.watchlist.client.WatchlistClient
import com.sunrisekcdeveloper.showtracker.features.watchlist.client.WatchlistServiceContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class HeaderInterceptor

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DiscoveryClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DiscoveryApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SearchClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SearchApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class WatchlistClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class WatchlistApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DetailClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DetailApi


    @Singleton
    @DiscoveryClient
    @Provides
    fun providesDiscoveryClient(@DiscoveryApi api: DiscoveryServiceContract) : DiscoveryDataSourceContract {
        return DiscoveryClient(api)
    }

    @Singleton
    @DiscoveryApi
    @Provides
    fun provideDiscoveryApi(retrofit: Retrofit): DiscoveryServiceContract {
        return retrofit.create(DiscoveryService::class.java)
    }

    @Singleton
    @SearchClient
    @Provides
    fun provideSearchClient(@SearchApi api: SearchServiceContract) : SearchDataSourceContract {
        return SearchClient(api)
    }

    @Singleton
    @SearchApi
    @Provides
    fun provideSearchApi(retrofit: Retrofit): SearchServiceContract {
        return retrofit.create(SearchService::class.java)
    }

    @Singleton
    @WatchlistClient
    @Provides
    fun provideWatchlistClient(@WatchlistApi api: WatchlistServiceContract) : WatchlistDataSourceContract {
        return WatchlistClient(api)
    }

    @Singleton
    @WatchlistApi
    @Provides
    fun provideWatchlistApi(retrofit: Retrofit): WatchlistServiceContract {
        return retrofit.create(WatchlistService::class.java)
    }

    @Singleton
    @DetailClient
    @Provides
    fun provideDetailClient(@DetailApi api: DetailServiceContract) : DetailDataSourceContract {
        return DetailClient(api)
    }

    @Singleton
    @DetailApi
    @Provides
    fun provideDetailApi(retrofit: Retrofit): DetailServiceContract {
        return retrofit.create(DetailService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.TRAKT_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            ))
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @HeaderInterceptor headerInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    @Singleton
    @HeaderInterceptor
    @Provides
    fun provideHeaderInterceptor(): Interceptor {
        return object : Interceptor {
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
    }
}