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
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.RepoWatchlist
import com.sunrisekcdeveloper.showtracker.features.detail.application.*
import com.sunrisekcdeveloper.showtracker.features.detail.domain.repository.RepositoryDetailContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.usecase.*
import com.sunrisekcdeveloper.showtracker.features.discovery.application.*
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.repository.RepositoryDiscoveryContract
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.usecase.*
import com.sunrisekcdeveloper.showtracker.features.search.application.SearchMediaByTitleUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.search.domain.repository.RepositorySearchContract
import com.sunrisekcdeveloper.showtracker.features.search.domain.usecase.SearchMediaByTitleUseCase
import com.sunrisekcdeveloper.showtracker.features.watchlist.application.FetchWatchlistMoviesUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository.RepositoryWatchlistContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.usecase.FetchWatchlistMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(ActivityRetainedComponent::class)
object UseCaseModule {
    // Watchlist
    @Provides
    fun provideFetchWatchlistMoviesUseCase(
        @RepoWatchlist watchlistRepo: RepositoryWatchlistContract
    ) : FetchWatchlistMoviesUseCaseContract =
        FetchWatchlistMoviesUseCase(watchlistRepo)

    // Search
    @Provides
    fun provideSearchMediaByTitleUseCase(
        @RepoSearch searchRepo: RepositorySearchContract
    ): SearchMediaByTitleUseCaseContract =
        SearchMediaByTitleUseCase(searchRepo)

    // Detail
    @Provides
    fun provideAddShowToWatchlistUseCase(
        @RepoDetail detailRepo: RepositoryDetailContract
    ) : AddShowToWatchlistUseCaseContract =
        AddShowToWatchlistUseCase(detailRepo)

    @Provides
    fun provideRemoveShowFromWatchlistUseCase(
        @RepoDetail detailRepo: RepositoryDetailContract
    ) : RemoveShowFromWatchlistUseCaseContract =
        RemoveShowFromWatchlistUseCase(detailRepo)

    @Provides
    fun provideRemoveMovieFromWatchlistUseCase(
        @RepoDetail detailRepo: RepositoryDetailContract
    ) : RemoveMovieFromWatchlistUseCaseContract =
        RemoveMovieFromWatchlistUseCase(detailRepo)

    @Provides
    fun provideUpdateMovieWatchedStatusUseCase(
        @RepoDetail detailRepo: RepositoryDetailContract
    ) : UpdateMovieWatchedStatusUseCaseContract =
        UpdateMovieWatchedStatusUseCase(detailRepo)

    @Provides
    fun provideAddMovieToWatchlistUseCase(
        @RepoDetail detailRepo: RepositoryDetailContract
    ): AddMovieToWatchlistUseCaseContract =
        AddMovieToWatchlistUseCase(detailRepo)

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