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

package com.sunrisekcdeveloper.detail.extras

import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.detail.impl.DetailRemoteDataSource
import com.sunrisekcdeveloper.detail.DetailRemoteDataSourceContract
import com.sunrisekcdeveloper.detail.impl.DetailService
import com.sunrisekcdeveloper.detail.DetailServiceContract
import com.sunrisekcdeveloper.detail.impl.AddMediaToWatchlistUseCase
import com.sunrisekcdeveloper.detail.impl.RemoveMediaFromWatchlistUseCase
import com.sunrisekcdeveloper.detail.movie.impl.FetchMovieDetailsUseCase
import com.sunrisekcdeveloper.detail.movie.impl.UpdateMovieWatchedStatusUseCase
import com.sunrisekcdeveloper.detail.movie.usecase.FetchMovieDetailsUseCaseContract
import com.sunrisekcdeveloper.detail.show.usecase.FetchShowDetailsUseCaseContract
import com.sunrisekcdeveloper.detail.usecase.RemoveMediaFromWatchlistUseCaseContract
import com.sunrisekcdeveloper.detail.movie.usecase.UpdateMovieWatchedStatusUseCaseContract
import com.sunrisekcdeveloper.detail.show.impl.FetchShowDetailsUseCase
import com.sunrisekcdeveloper.detail.usecase.AddMediaToWatchlistUseCaseContract
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
// todo rename
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideServiceDetail(retrofit: Retrofit): DetailServiceContract {
        return retrofit.create(DetailService::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideRemoteDetailDataSource(
        api: DetailServiceContract
    ): DetailRemoteDataSourceContract {
        return DetailRemoteDataSource(api)
    }

    @Provides
    @ViewModelScoped
    fun provideRepositoryDetail(
        remote: DetailRemoteDataSourceContract,
        local: TrackerDatabase
    ): RepositoryDetailContract {
        return RepositoryDetail(remote, local)
    }

    @Provides
    @ViewModelScoped
    fun provideFetchShowDetailsUseCase(
        repo: RepositoryDetailContract
    ): FetchShowDetailsUseCaseContract = FetchShowDetailsUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideFetchMovieDetailsUseCase(
        repo: RepositoryDetailContract
    ): FetchMovieDetailsUseCaseContract = FetchMovieDetailsUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideAddMediaToWatchlistUseCase(
        repo: RepositoryDetailContract
    ): AddMediaToWatchlistUseCaseContract = AddMediaToWatchlistUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideUpdateMovieWatchedStatusUseCase(
        repo: RepositoryDetailContract
    ): UpdateMovieWatchedStatusUseCaseContract = UpdateMovieWatchedStatusUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideRemoveMediaFromWatchlistUseCase(
        repo: RepositoryDetailContract
    ): RemoveMediaFromWatchlistUseCaseContract = RemoveMediaFromWatchlistUseCase(repo)

}