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

package com.sunrisekcdeveloper.discovery.di

import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.discovery.application.*
import com.sunrisekcdeveloper.discovery.data.network.RemoteDataSourceDiscovery
import com.sunrisekcdeveloper.discovery.data.network.RemoteDataSourceDiscoveryContract
import com.sunrisekcdeveloper.discovery.data.network.ServiceDiscovery
import com.sunrisekcdeveloper.discovery.data.network.ServiceDiscoveryContract
import com.sunrisekcdeveloper.discovery.data.repository.RepositoryDiscovery
import com.sunrisekcdeveloper.discovery.domain.repository.RepositoryDiscoveryContract
import com.sunrisekcdeveloper.discovery.domain.usecase.*
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
object ModuleDiscovery {

    @Provides
    @ViewModelScoped
    fun provideServiceDiscovery(
        retrofit: Retrofit
    ): ServiceDiscoveryContract = retrofit.create(ServiceDiscovery::class.java)

    @Provides
    @ViewModelScoped
    fun provideRemoteDataSourceDiscovery(
        api: ServiceDiscoveryContract
    ): RemoteDataSourceDiscoveryContract = RemoteDataSourceDiscovery(api)

    @Provides
    @ViewModelScoped
    fun provideRepositoryDiscovery(
        remote: RemoteDataSourceDiscoveryContract,
        database: TrackerDatabase
    ): RepositoryDiscoveryContract = RepositoryDiscovery(remote, database)

    @Provides
    @ViewModelScoped
    fun provideLoadPopularMoviesUseCase(
        repo: RepositoryDiscoveryContract
    ): LoadPopularMoviesUseCaseContract =
        LoadPopularMoviesUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideLoadTopRatedMoviesUseCase(
        repo: RepositoryDiscoveryContract
    ): LoadTopRatedMoviesUseCaseContract =
        LoadTopRatedMoviesUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideLoadUpcomingMoviesUseCase(
        repo: RepositoryDiscoveryContract
    ): LoadUpcomingMoviesUseCaseContract =
        LoadUpcomingMoviesUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideLoadAiringTodayShowsUseCase(
        repo: RepositoryDiscoveryContract
    ): LoadAiringTodayShowsUseCaseContract =
        LoadAiringTodayShowsUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideLoadPopularShowsUseCase(
        repo: RepositoryDiscoveryContract
    ): LoadPopularShowsUseCaseContract =
        LoadPopularShowsUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideTopRatedShowsUseCase(
        repo: RepositoryDiscoveryContract
    ): LoadTopRatedShowsUseCaseContract =
        LoadTopRatedShowsUseCase(repo)
}