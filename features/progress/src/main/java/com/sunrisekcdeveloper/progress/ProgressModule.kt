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

package com.sunrisekcdeveloper.progress

import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.network.NetworkContract
import com.sunrisekcdeveloper.progress.domain.*
import com.sunrisekcdeveloper.progress.usecase.FetchShowSeasonAndEpisodeTotalsUseCaseContract
import com.sunrisekcdeveloper.progress.usecase.SetShowProgressUseCaseContract
import com.sunrisekcdeveloper.progress.impl.ProgressRemoteDataSource
import com.sunrisekcdeveloper.progress.impl.ProgressService
import com.sunrisekcdeveloper.progress.impl.ProgressRepository
import com.sunrisekcdeveloper.progress.impl.FetchShowSeasonAndEpisodeTotalsUseCase
import com.sunrisekcdeveloper.progress.impl.SetShowProgressUseCase
import com.sunrisekcdeveloper.show.TVShowRepositoryContract
import com.sunrisekcdeveloper.show.WatchlistTVShowRepositoryContract
import com.sunrisekcdeveloper.show.episode.WatchlistEpisodeRepositoryContract
import com.sunrisekcdeveloper.show.season.SeasonRepositoryContract
import com.sunrisekcdeveloper.show.season.WatchlistSeasonRepositoryContract
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
object ProgressModule {

    @Provides
    @ViewModelScoped
    fun provideServiceProgress(retrofit: Retrofit): ProgressServiceContract {
        return retrofit.create(ProgressService::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideRemoteDataSourceProgress(
        api: ProgressServiceContract
    ): ProgressRemoteDataSourceContract = ProgressRemoteDataSource(api, NetworkContract.Impl())

    @Provides
    @ViewModelScoped
    fun provideRepositoryProgress(
        remote: ProgressRemoteDataSourceContract,
        local: TrackerDatabase
    ): ProgressRepositoryContract = ProgressRepository(
        EpisodeRepository(remote, local),
        WatchlistEpisodeRepository(local),
        SeasonRepository(remote, local),
        WatchlistSeasonRepository(local),
        TVShowRepository(remote, local),
        WatchlistTVShowRepository(local)
    )

    @Provides
    @ViewModelScoped
    fun provideFetchShowSeasonAndEpisodeTotalsUseCase(
        repo: ProgressRepositoryContract
    ): FetchShowSeasonAndEpisodeTotalsUseCaseContract = FetchShowSeasonAndEpisodeTotalsUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideSetShowProgressUseCase(
        repo: ProgressRepositoryContract
    ): SetShowProgressUseCaseContract = SetShowProgressUseCase(repo)

}