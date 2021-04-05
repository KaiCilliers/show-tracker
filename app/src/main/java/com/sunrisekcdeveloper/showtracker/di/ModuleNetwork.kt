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
import com.sunrisekcdeveloper.showtracker.common.util.*
import com.sunrisekcdeveloper.showtracker.features.detail.data.network.RemoteDataSourceDetailContract
import com.sunrisekcdeveloper.showtracker.features.detail.data.network.RemoteDataSourceDetail
import com.sunrisekcdeveloper.showtracker.features.detail.data.network.ServiceDetailContract
import com.sunrisekcdeveloper.showtracker.features.detail.data.network.ServiceDetail
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.RemoteDataSourceDiscoveryContract
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.RemoteDataSourceDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.ServiceDiscoveryContract
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.ServiceDiscovery
import com.sunrisekcdeveloper.showtracker.features.progress.data.network.RemoteDataSourceProgress
import com.sunrisekcdeveloper.showtracker.features.progress.data.network.RemoteDataSourceProgressContract
import com.sunrisekcdeveloper.showtracker.features.progress.data.network.ServiceProgress
import com.sunrisekcdeveloper.showtracker.features.progress.data.network.ServiceProgressContract
import com.sunrisekcdeveloper.showtracker.features.search.data.network.RemoteDataSourceSearchContract
import com.sunrisekcdeveloper.showtracker.features.search.data.network.RemoteDataSourceSearch
import com.sunrisekcdeveloper.showtracker.features.search.data.network.ServiceSearchContract
import com.sunrisekcdeveloper.showtracker.features.search.data.network.ServiceSearch
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ModuleNetwork {

    // Retrofit
    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.TMDB_BASE_URL)
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

    // Services
    @Singleton
    @ApiProgress
    @Provides
    fun  provideServiceProgress(retrofit: Retrofit): ServiceProgressContract {
        return retrofit.create(ServiceProgress::class.java)
    }
    @Singleton
    @ApiSearch
    @Provides
    fun provideServiceSearch(retrofit: Retrofit): ServiceSearchContract {
        return retrofit.create(ServiceSearch::class.java)
    }
    @Singleton
    @ApiDetail
    @Provides
    fun provideServiceDetail(retrofit: Retrofit): ServiceDetailContract {
        return retrofit.create(ServiceDetail::class.java)
    }
    @Singleton
    @ApiDiscovery
    @Provides
    fun provideServiceDiscovery(retrofit: Retrofit): ServiceDiscoveryContract {
        return retrofit.create(ServiceDiscovery::class.java)
    }

    // Remote Data Sources
    @Singleton
    @SourceProgress
    @Provides
    fun provideRemoteDataSourceProgress(
        @ApiProgress api: ServiceProgressContract
    ) : RemoteDataSourceProgressContract =
        RemoteDataSourceProgress(api)
    @Singleton
    @SourceSearch
    @Provides
    fun provideRemoteDataSourceSearch(
        @ApiSearch api: ServiceSearchContract
    ) : RemoteDataSourceSearchContract {
        return RemoteDataSourceSearch(api)
    }
    @Singleton
    @SourceDetail
    @Provides
    fun providesRemoteDataSourceDetail(
        @ApiDetail api: ServiceDetailContract
    ) : RemoteDataSourceDetailContract {
        return RemoteDataSourceDetail(api)
    }
    @Singleton
    @SourceDiscovery
    @Provides
    fun provideRemoteDataSourceDiscovery(
        @ApiDiscovery api: ServiceDiscoveryContract
    ) : RemoteDataSourceDiscoveryContract {
        return RemoteDataSourceDiscovery(api)
    }
}