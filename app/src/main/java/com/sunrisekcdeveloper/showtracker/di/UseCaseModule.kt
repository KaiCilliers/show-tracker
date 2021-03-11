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

import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.DetRepo
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.DiscRepo
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.SearchRepoUpdated
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.WatchlistRepo
import com.sunrisekcdeveloper.showtracker.updated.features.detail.application.FetchMovieDetailsUseCaseContract
import com.sunrisekcdeveloper.showtracker.updated.features.detail.application.FetchShowDetailsUseCaseContract
import com.sunrisekcdeveloper.showtracker.updated.features.detail.domain.repository.DetailRepositoryContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.detail.domain.usecase.FetchMovieDetailsUseCase
import com.sunrisekcdeveloper.showtracker.updated.features.detail.domain.usecase.FetchShowDetailsUseCase
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.application.*
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.domain.repository.DiscoveryRepositoryContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.domain.usecase.*
import com.sunrisekcdeveloper.showtracker.updated.features.search.application.SearchMediaByTitleUseCaseContract
import com.sunrisekcdeveloper.showtracker.updated.features.search.domain.repository.SearchRepositoryContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.search.domain.usecase.SearchMediaByTitleUseCase
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
        @SearchRepoUpdated searchRepo: SearchRepositoryContractUpdated
    ): SearchMediaByTitleUseCaseContract =
        SearchMediaByTitleUseCase(searchRepo)

    // Detail
    @Provides
    fun provideFetchMovieDetailUseCase(
        @DetRepo detailRepo: DetailRepositoryContractUpdated
    ): FetchMovieDetailsUseCaseContract =
        FetchMovieDetailsUseCase(detailRepo)

    @Provides
    fun provideFetchShowDetailUseCase(
        @DetRepo detailRepo: DetailRepositoryContractUpdated
    ): FetchShowDetailsUseCaseContract =
        FetchShowDetailsUseCase(detailRepo)

    // Discovery Updated
    @Provides
    fun provideLoadPopularMoviesUseCaseUpdated(
        @DiscRepo discoveryRepo: DiscoveryRepositoryContractUpdated
    ): LoadPopularMoviesUseCaseContractUpdated =
        LoadPopularMoviesUseCaseUpdated(discoveryRepo)

    @Provides
    fun provideLoadTopRatedMoviesUseCaseUpdated(
        @DiscRepo discoveryRepo: DiscoveryRepositoryContractUpdated
    ): LoadTopRatedMoviesUseCaseContractUpdated =
        LoadTopRatedMoviesUseCaseUpdated(discoveryRepo)

    @Provides
    fun provideLoadUpcomingMoviesUseCaseUpdated(
        @DiscRepo discoveryRepo: DiscoveryRepositoryContractUpdated
    ): LoadUpcomingMoviesUseCaseContractUpdated =
        LoadUpcomingMoviesUseCaseUpdated(discoveryRepo)

    @Provides
    fun provideLoadAiringTodayShowsUseCaseUpdated(
        @DiscRepo discoveryRepo: DiscoveryRepositoryContractUpdated
    ): LoadAiringTodayShowsUseCaseContractUpdated =
        LoadAiringTodayShowsUseCaseUpdated(discoveryRepo)

    @Provides
    fun provideLoadPopularShowsUseCaseUpdated(
        @DiscRepo discoveryRepo: DiscoveryRepositoryContractUpdated
    ): LoadPopularShowsUseCaseContractUpdated =
        LoadPopularShowsUseCaseUpdated(discoveryRepo)

    @Provides
    fun provideTopRatedShowsUseCaseUpdated(
        @DiscRepo discoveryRepo: DiscoveryRepositoryContractUpdated
    ): LoadTopRatedShowsUseCaseContractUpdated =
        LoadTopRatedShowsUseCaseUpdated(discoveryRepo)
}