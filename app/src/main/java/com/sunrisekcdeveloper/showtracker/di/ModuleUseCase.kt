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

import com.sunrisekcdeveloper.showtracker.common.util.*
import com.sunrisekcdeveloper.showtracker.features.discovery.application.*
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.repository.RepositoryDiscoveryContract
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.usecase.*
import com.sunrisekcdeveloper.showtracker.features.progress.application.FetchShowSeasonAndEpisodeTotalsUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.progress.application.SetShowProgressUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.progress.domain.repository.RepositoryProgressContract
import com.sunrisekcdeveloper.showtracker.features.progress.domain.usecase.FetchShowSeasonAndEpisodeTotalsUseCase
import com.sunrisekcdeveloper.showtracker.features.progress.domain.usecase.SetShowProgressUseCase
import com.sunrisekcdeveloper.showtracker.features.search.application.LoadUnwatchedMediaUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.search.application.SearchMediaByTitleUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.search.domain.repository.RepositorySearchContract
import com.sunrisekcdeveloper.showtracker.features.search.domain.usecase.LoadUnwatchedMediaUseCase
import com.sunrisekcdeveloper.showtracker.features.search.domain.usecase.SearchMediaByTitleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(ActivityRetainedComponent::class)
object ModuleUseCase {
    // Progress
    @Provides
    fun provideFetchShowSeasonAndEpisodeTotalsUseCase(
        @RepoProgress progressRepo: RepositoryProgressContract
    ) : FetchShowSeasonAndEpisodeTotalsUseCaseContract =
        FetchShowSeasonAndEpisodeTotalsUseCase(progressRepo)

    @Provides
    fun provideSetShowProgressUseCase(
        @RepoProgress repo: RepositoryProgressContract
    ) : SetShowProgressUseCaseContract =
        SetShowProgressUseCase(repo)

    // Search
    @Provides
    fun provideSearchMediaByTitleUseCase(
        @RepoSearch searchRepo: RepositorySearchContract
    ): SearchMediaByTitleUseCaseContract =
        SearchMediaByTitleUseCase(searchRepo)

    @Provides
    fun provideLoadUnwatchedMediaUseCase(
        @RepoSearch searchRepo: RepositorySearchContract
    ) : LoadUnwatchedMediaUseCaseContract =
        LoadUnwatchedMediaUseCase(searchRepo)

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
    ): LoadUpcomingMoviesUseCaseContract =
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