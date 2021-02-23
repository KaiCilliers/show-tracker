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

import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.DetailRepo
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.DiscoveryRepo
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.WatchlistRepo
import com.sunrisekcdeveloper.showtracker.features.detail.application.UpdateWatchListContentsUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.repository.DetailRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.usecase.UpdateWatchListContentsUseCase
import com.sunrisekcdeveloper.showtracker.features.discover.application.*
import com.sunrisekcdeveloper.showtracker.features.discover.domain.repository.DiscoveryRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.discover.domain.usecase.*
import com.sunrisekcdeveloper.showtracker.features.watchlist.application.*
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository.WatchListRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object UseCaseModule {
    // Discovery
    @Provides
    fun provideLoadPopularMoviesUseCase(
        @DiscoveryRepo discoveryRepo: DiscoveryRepositoryContract
    ): LoadPopularMoviesUseCaseContract =
        LoadPopularMoviesUseCase(discoveryRepo)

    @Provides
    fun provideLoadTopRatedMoviesUseCase(
        @DiscoveryRepo discoveryRepo: DiscoveryRepositoryContract
    ): LoadTopRatedMoviesUseCaseContract =
        LoadTopRatedMoviesUseCase(discoveryRepo)

    @Provides
    fun provideLoadUpcomingMoviesUseCase(
        @DiscoveryRepo discoveryRepo: DiscoveryRepositoryContract
    ): LoadUpcomingMoviesUseCaseContract =
        LoadUpcomingMoviesUseCase(discoveryRepo)

    @Provides
    fun provideSaveMediaToWatchListUseCase(
        @DiscoveryRepo discoveryRepo: DiscoveryRepositoryContract
    ): SaveMediaToWatchListUseCaseContract =
        SaveMediaToWatchListUseCase(discoveryRepo)

    @Provides
    fun provideLoadAiringTodayShowsUseCase(
        @DiscoveryRepo discoveryRepo: DiscoveryRepositoryContract
    ): LoadAiringTodayShowsUseCaseContract =
        LoadAiringTodayShowsUseCase(discoveryRepo)

    @Provides
    fun provideLoadPopularShowsUseCase(
        @DiscoveryRepo discoveryRepo: DiscoveryRepositoryContract
    ): LoadPopularShowsUseCaseContract =
        LoadPopularShowsUseCase(discoveryRepo)

    @Provides
    fun provideTopRatedShowsUseCase(
        @DiscoveryRepo discoveryRepo: DiscoveryRepositoryContract
    ): LoadTopRatedShowsUseCaseContract =
        LoadTopRatedShowsUseCase(discoveryRepo)

    // Watchlist
    @Provides
    fun provideLoadWatchListMediaUseCase(
        @WatchlistRepo watchlistRepo: WatchListRepositoryContract
    ): LoadWatchListMediaUseCaseContract =
        LoadWatchListMediaUseCase(watchlistRepo)

    // Detail
    @Provides
    fun providesUpdateWatchListContentsUseCase(
        @DetailRepo detailRepo: DetailRepositoryContract
    ): UpdateWatchListContentsUseCaseContract =
        UpdateWatchListContentsUseCase(detailRepo)
}