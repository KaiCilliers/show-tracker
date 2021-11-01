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

package com.sunrisekcdeveloper.discovery

import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.discovery.impl.*
import com.sunrisekcdeveloper.discovery.usecase.*
import com.sunrisekcdeveloper.network.NetworkContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
@Module
@InstallIn(ViewModelComponent::class)
object DiscoveryModule {

    @Provides
    @ViewModelScoped
    fun provideServiceDiscovery(
        retrofit: Retrofit
    ): DiscoveryServiceContract = retrofit.create(DiscoveryService::class.java)

    @Provides
    @ViewModelScoped
    fun provideRemoteDataSourceDiscovery(
        api: DiscoveryServiceContract
    ): DiscoveryRemoteDataSourceContract = DiscoveryRemoteDataSource(api, NetworkContract.Impl())

    @Provides
    @ViewModelScoped
    fun provideRepositoryDiscovery(
        remote: DiscoveryRemoteDataSourceContract,
        database: TrackerDatabase
    ): DiscoveryRepositoryContract = DiscoveryRepository(remote, database)

    @Provides
    @ViewModelScoped
    fun provideLoadPopularMoviesUseCase(
        repo: DiscoveryRepositoryContract
    ): LoadPopularMoviesUseCaseContract =
        LoadPopularMoviesUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideLoadTopRatedMoviesUseCase(
        repo: DiscoveryRepositoryContract
    ): LoadTopRatedMoviesUseCaseContract =
        LoadTopRatedMoviesUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideLoadUpcomingMoviesUseCase(
        repo: DiscoveryRepositoryContract
    ): LoadUpcomingMoviesUseCaseContract =
        LoadUpcomingMoviesUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideLoadAiringTodayShowsUseCase(
        repo: DiscoveryRepositoryContract
    ): LoadAiringTodayShowsUseCaseContract =
        LoadAiringTodayShowsUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideLoadPopularShowsUseCase(
        repo: DiscoveryRepositoryContract
    ): LoadPopularShowsUseCaseContract =
        LoadPopularShowsUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideTopRatedShowsUseCase(
        repo: DiscoveryRepositoryContract
    ): LoadTopRatedShowsUseCaseContract =
        LoadTopRatedShowsUseCase(repo)
}