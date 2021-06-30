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

package com.sunrisekcdeveloper.progress.di

import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.progress.application.FetchShowSeasonAndEpisodeTotalsUseCaseContract
import com.sunrisekcdeveloper.progress.application.SetShowProgressUseCaseContract
import com.sunrisekcdeveloper.progress.data.network.RemoteDataSourceProgress
import com.sunrisekcdeveloper.progress.data.network.RemoteDataSourceProgressContract
import com.sunrisekcdeveloper.progress.data.network.ServiceProgress
import com.sunrisekcdeveloper.progress.data.network.ServiceProgressContract
import com.sunrisekcdeveloper.progress.data.repository.RepositoryProgress
import com.sunrisekcdeveloper.progress.domain.repository.RepositoryProgressContract
import com.sunrisekcdeveloper.progress.domain.usecase.FetchShowSeasonAndEpisodeTotalsUseCase
import com.sunrisekcdeveloper.progress.domain.usecase.SetShowProgressUseCase
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
object ModuleProgress {

    @Provides
    @ViewModelScoped
    fun provideServiceProgress(retrofit: Retrofit): ServiceProgressContract {
        return retrofit.create(ServiceProgress::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideRemoteDataSourceProgress(
        api: ServiceProgressContract
    ): RemoteDataSourceProgressContract = RemoteDataSourceProgress(api)

    @Provides
    @ViewModelScoped
    fun provideRepositoryProgress(
        remote: RemoteDataSourceProgressContract,
        local: TrackerDatabase
    ): RepositoryProgressContract = RepositoryProgress(remote,local)

    @Provides
    @ViewModelScoped
    fun provideFetchShowSeasonAndEpisodeTotalsUseCase(
        repo: RepositoryProgressContract
    ): FetchShowSeasonAndEpisodeTotalsUseCaseContract = FetchShowSeasonAndEpisodeTotalsUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideSetShowProgressUseCase(
        repo: RepositoryProgressContract
    ): SetShowProgressUseCaseContract = SetShowProgressUseCase(repo)

}