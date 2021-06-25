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

package com.sunrisekcdeveloper.watchlist.di

import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.watchlist.application.FetchWatchlistMoviesUseCaseContract
import com.sunrisekcdeveloper.watchlist.application.FetchWatchlistShowsUseCaseContract
import com.sunrisekcdeveloper.watchlist.application.UpdateMovieWatchedStatusUseCaseContract
import com.sunrisekcdeveloper.watchlist.application.UpdateShowProgressUseCaseContract
import com.sunrisekcdeveloper.watchlist.data.repository.RepositoryWatchlist
import com.sunrisekcdeveloper.watchlist.domain.repository.RepositoryWatchlistContract
import com.sunrisekcdeveloper.watchlist.domain.usecase.FetchWatchlistMoviesUseCase
import com.sunrisekcdeveloper.watchlist.domain.usecase.FetchWatchlistShowsUseCase
import com.sunrisekcdeveloper.watchlist.domain.usecase.UpdateMovieWatchedStatusUseCase
import com.sunrisekcdeveloper.watchlist.domain.usecase.UpdateShowProgressUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@InstallIn(ViewModelComponent::class)
@ExperimentalCoroutinesApi
object WatchlistModule {

    @Provides
    @ViewModelScoped
    fun provideRepositoryWatchlist(
        db: TrackerDatabase
    ): RepositoryWatchlistContract = RepositoryWatchlist(db)

    @Provides
    @ViewModelScoped
    fun provideFetchWatchlistMoviesUseCase(
        repo: RepositoryWatchlistContract
    ): FetchWatchlistMoviesUseCaseContract = FetchWatchlistMoviesUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideFetchWatchlistShowsUseCase(
        repo: RepositoryWatchlistContract
    ): FetchWatchlistShowsUseCaseContract = FetchWatchlistShowsUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideUpdateMovieWatchedStatusUseCase(
        repo: RepositoryWatchlistContract
    ): UpdateMovieWatchedStatusUseCaseContract = UpdateMovieWatchedStatusUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideUpdateShowProgressUseCase(
        repo: RepositoryWatchlistContract
    ): UpdateShowProgressUseCaseContract = UpdateShowProgressUseCase(repo)
}