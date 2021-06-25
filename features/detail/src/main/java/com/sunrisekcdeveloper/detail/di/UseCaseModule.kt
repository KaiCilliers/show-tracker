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

package com.sunrisekcdeveloper.detail.di

import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.detail.application.*
import com.sunrisekcdeveloper.detail.data.network.RemoteDataSourceDetail
import com.sunrisekcdeveloper.detail.data.network.RemoteDataSourceDetailContract
import com.sunrisekcdeveloper.detail.data.network.ServiceDetail
import com.sunrisekcdeveloper.detail.data.network.ServiceDetailContract
import com.sunrisekcdeveloper.detail.data.repository.RepositoryDetail
import com.sunrisekcdeveloper.detail.domain.repository.RepositoryDetailContract
import com.sunrisekcdeveloper.detail.domain.usecase.*
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
    fun provideServiceDetail(retrofit: Retrofit): ServiceDetailContract {
        return retrofit.create(ServiceDetail::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideRemoteDetailDataSource(
        api: ServiceDetailContract
    ): RemoteDataSourceDetailContract {
        return RemoteDataSourceDetail(api)
    }

    @Provides
    @ViewModelScoped
    fun provideRepositoryDetail(
        remote: RemoteDataSourceDetailContract,
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