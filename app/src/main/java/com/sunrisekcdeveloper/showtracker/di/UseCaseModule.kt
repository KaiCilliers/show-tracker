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

package com.sunrisekcdeveloper.showtracker.di

import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.RepoDetail
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.RepoDiscovery
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.RepoSearch
import com.sunrisekcdeveloper.showtracker.features.detail.application.FetchMovieDetailsUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.detail.application.FetchShowDetailsUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.repository.RepositoryDetailContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.usecase.FetchMovieDetailsUseCase
import com.sunrisekcdeveloper.showtracker.features.detail.domain.usecase.FetchShowDetailsUseCase
import com.sunrisekcdeveloper.showtracker.features.discovery.application.*
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.repository.RepositoryDiscoveryContract
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.usecase.*
import com.sunrisekcdeveloper.showtracker.features.search.application.SearchMediaByTitleUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.search.domain.repository.SearchRepositoryContractUpdated
import com.sunrisekcdeveloper.showtracker.features.search.domain.usecase.SearchMediaByTitleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object UseCaseModule {
    // Search
    @Provides
    fun provideSearchMediaByTitleUseCase(
        @RepoSearch searchRepo: SearchRepositoryContractUpdated
    ): SearchMediaByTitleUseCaseContract =
        SearchMediaByTitleUseCase(searchRepo)

    // Detail
    @Provides
    fun provideFetchMovieDetailsUseCase(
        @RepoDetail detailRepo: RepositoryDetailContract
    ): FetchMovieDetailsUseCaseContract =
        FetchMovieDetailsUseCase(detailRepo)

    @Provides
    fun provideFetchShowDetailsUseCase(
        @RepoDetail detailRepo: RepositoryDetailContract
    ): FetchShowDetailsUseCaseContract =
        FetchShowDetailsUseCase(detailRepo)

    // Discovery
    @Provides
    fun provideLoadPopularMoviesUseCase(
        @RepoDiscovery discoveryRepo: RepositoryDiscoveryContract
    ): LoadPopularMoviesUseCaseContract =
        LoadPopularMoviesUseCase(discoveryRepo)

    @Provides
    fun provideLoadTopRatedMoviesUseCase(
        @RepoDiscovery discoveryRepo: RepositoryDiscoveryContract
    ): LoadTopRatedMoviesUseCaseContract =
        LoadTopRatedMoviesUseCase(discoveryRepo)

    @Provides
    fun provideLoadUpcomingMoviesUseCase(
        @RepoDiscovery discoveryRepo: RepositoryDiscoveryContract
    ): LoadUpcomingMoviesUseCaseContractUpdated =
        LoadUpcomingMoviesUseCase(discoveryRepo)

    @Provides
    fun provideLoadAiringTodayShowsUseCase(
        @RepoDiscovery discoveryRepo: RepositoryDiscoveryContract
    ): LoadAiringTodayShowsUseCaseContract =
        LoadAiringTodayShowsUseCase(discoveryRepo)

    @Provides
    fun provideLoadPopularShowsUseCase(
        @RepoDiscovery discoveryRepo: RepositoryDiscoveryContract
    ): LoadPopularShowsUseCaseContract =
        LoadPopularShowsUseCase(discoveryRepo)

    @Provides
    fun provideTopRatedShowsUseCase(
        @RepoDiscovery discoveryRepo: RepositoryDiscoveryContract
    ): LoadTopRatedShowsUseCaseContract =
        LoadTopRatedShowsUseCase(discoveryRepo)
}