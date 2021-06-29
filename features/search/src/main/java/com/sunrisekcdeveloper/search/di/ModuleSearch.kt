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

package com.sunrisekcdeveloper.search.di

import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.search.application.LoadUnwatchedMediaUseCaseContract
import com.sunrisekcdeveloper.search.application.SearchMediaByTitleUseCaseContract
import com.sunrisekcdeveloper.search.data.network.RemoteDataSourceSearch
import com.sunrisekcdeveloper.search.data.network.RemoteDataSourceSearchContract
import com.sunrisekcdeveloper.search.data.network.ServiceSearch
import com.sunrisekcdeveloper.search.data.network.ServiceSearchContract
import com.sunrisekcdeveloper.search.data.repository.RepositorySearch
import com.sunrisekcdeveloper.search.domain.repository.RepositorySearchContract
import com.sunrisekcdeveloper.search.domain.usecase.LoadUnwatchedMediaUseCase
import com.sunrisekcdeveloper.search.domain.usecase.SearchMediaByTitleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Retrofit
import javax.inject.Inject

@ExperimentalCoroutinesApi
@Module
@InstallIn(ViewModelComponent::class)
object ModuleSearch {

    @Provides
    @ViewModelScoped
    fun provideLoadUnwatchedMediaUseCase(
        repo: RepositorySearchContract
    ): LoadUnwatchedMediaUseCaseContract = LoadUnwatchedMediaUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideSearchMediaByTitleUseCase(
        repo: RepositorySearchContract
    ): SearchMediaByTitleUseCaseContract = SearchMediaByTitleUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideRepositorySearch(
        db: TrackerDatabase,
        remote: RemoteDataSourceSearchContract
    ): RepositorySearchContract = RepositorySearch(remote, db)

    @Provides
    @ViewModelScoped
    fun provideSearchService(
        retrofit: Retrofit
    ): ServiceSearchContract = retrofit.create(ServiceSearch::class.java)

    @Provides
    @ViewModelScoped
    fun provideRemoteDataSourceSearch(
        api: ServiceSearchContract
    ): RemoteDataSourceSearchContract = RemoteDataSourceSearch(api)
}