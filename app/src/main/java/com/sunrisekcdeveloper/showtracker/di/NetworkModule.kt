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
import com.sunrisekcdeveloper.showtracker.features.detail.data.network.DetailDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.detail.data.network.DetailRemoteDataSource
import com.sunrisekcdeveloper.showtracker.features.detail.data.network.DetailService
import com.sunrisekcdeveloper.showtracker.features.detail.data.network.DetailServiceContract
import com.sunrisekcdeveloper.showtracker.features.discover.data.network.DiscoveryRemoteDataSource
import com.sunrisekcdeveloper.showtracker.features.discover.data.network.DiscoveryRemoteDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.discover.data.network.DiscoveryService
import com.sunrisekcdeveloper.showtracker.features.discover.data.network.DiscoveryServiceContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.network.WatchlistDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.network.WatchlistService
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.network.WatchlistRemoteDataSource
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.network.WatchlistServiceContract
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.network.DiscoveryRemoteDataSourceContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.network.DiscoveryRemoteDataSourceUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.network.DiscoveryServiceContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.network.DiscoveryServiceUpdated
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DiscClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DiscoveryClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DiscApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DiscoveryApi

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
    @DiscClient
    @Provides
    fun providesDiscoveryClientUpdated(
        @DiscApi api: DiscoveryServiceContractUpdated
    ) : DiscoveryRemoteDataSourceContractUpdated {
        return DiscoveryRemoteDataSourceUpdated(api)
    }

    @Singleton
    @DiscApi
    @Provides
    fun provideDiscoveryApiUpdated(retrofit: Retrofit): DiscoveryServiceContractUpdated {
        return retrofit.create(DiscoveryServiceUpdated::class.java)
    }

    @Singleton
    @DiscoveryClient
    @Provides
    fun providesDiscoveryClient(@DiscoveryApi api: DiscoveryServiceContract) : DiscoveryRemoteDataSourceContract {
        return DiscoveryRemoteDataSource(api)
    }

    @Singleton
    @DiscoveryApi
    @Provides
    fun provideDiscoveryApi(retrofit: Retrofit): DiscoveryServiceContract {
        return retrofit.create(DiscoveryService::class.java)
    }

    @Singleton
    @WatchlistClient
    @Provides
    fun provideWatchlistClient(@WatchlistApi api: WatchlistServiceContract) : WatchlistDataSourceContract {
        return WatchlistRemoteDataSource(api)
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
        return DetailRemoteDataSource(api)
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
            .baseUrl("https://api.themoviedb.org/3/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            ))
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
}