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

package com.sunrisekcdeveloper.watchlist

import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.watchlist.usecase.FetchWatchlistMoviesUseCaseContract
import com.sunrisekcdeveloper.watchlist.usecase.FetchWatchlistShowsUseCaseContract
import com.sunrisekcdeveloper.watchlist.usecase.UpdateMovieWatchedStatusUseCaseContract
import com.sunrisekcdeveloper.watchlist.usecase.UpdateShowProgressUseCaseContract
import com.sunrisekcdeveloper.watchlist.impl.WatchlistRepository
import com.sunrisekcdeveloper.watchlist.impl.FetchWatchlistMoviesUseCase
import com.sunrisekcdeveloper.watchlist.impl.FetchWatchlistShowsUseCase
import com.sunrisekcdeveloper.watchlist.impl.UpdateMovieWatchedStatusUseCase
import com.sunrisekcdeveloper.watchlist.impl.UpdateShowProgressUseCase
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
    ): WatchlistRepositoryContract = WatchlistRepository(db)

    @Provides
    @ViewModelScoped
    fun provideFetchWatchlistMoviesUseCase(
        repo: WatchlistRepositoryContract
    ): FetchWatchlistMoviesUseCaseContract = FetchWatchlistMoviesUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideFetchWatchlistShowsUseCase(
        repo: WatchlistRepositoryContract
    ): FetchWatchlistShowsUseCaseContract = FetchWatchlistShowsUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideUpdateMovieWatchedStatusUseCase(
        repo: WatchlistRepositoryContract
    ): UpdateMovieWatchedStatusUseCaseContract = UpdateMovieWatchedStatusUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideUpdateShowProgressUseCase(
        repo: WatchlistRepositoryContract
    ): UpdateShowProgressUseCaseContract = UpdateShowProgressUseCase(repo)
}