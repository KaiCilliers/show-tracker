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
import com.sunrisekcdeveloper.network.NetworkContract
import com.sunrisekcdeveloper.watchlist.impl.*
import com.sunrisekcdeveloper.watchlist.usecase.FetchWatchlistMoviesUseCaseContract
import com.sunrisekcdeveloper.watchlist.usecase.FetchWatchlistShowsUseCaseContract
import com.sunrisekcdeveloper.watchlist.usecase.UpdateMovieWatchedStatusUseCaseContract
import com.sunrisekcdeveloper.watchlist.usecase.UpdateShowProgressUseCaseContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
@ExperimentalCoroutinesApi
object WatchlistModule {

    @Provides
    @ViewModelScoped
    fun provideRepositoryWatchlist(
        remote: WatchlistRemoteDataSourceContract,
        db: TrackerDatabase
    ): WatchlistRepositoryContract = WatchlistRepository(
        watchlistShowRepo = WatchlistTVShowRepository(db),
        showRepository = TVShowRepository(remote, db),
        watchlistMovieRepo = WatchlistMovieRepository(db),
        watchlistSeasonRepo = WatchlistSeasonRepository(db),
        seasonRepo = SeasonRepository(remote, db),
        watchlistEpisodeRepo = WatchlistEpisodeRepository(db),
        episodeRepo = EpisodeRepository(remote, db)
    )

    @Provides
    @ViewModelScoped
    fun provideServiceWatchlist(retrofit: Retrofit): WatchlistServiceContract {
        return retrofit.create(WatchlistService::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideRemoteWatchlistDataSource(
        api: WatchlistServiceContract
    ): WatchlistRemoteDataSourceContract {
        return WatchlistRemoteDataSource(api, NetworkContract.Impl())
    }

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