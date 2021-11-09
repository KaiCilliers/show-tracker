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

package com.sunrisekcdeveloper.search

import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.network.NetworkContract
import com.sunrisekcdeveloper.search.domain.MovieRepository
import com.sunrisekcdeveloper.search.domain.TVShowRepository
import com.sunrisekcdeveloper.search.usecase.LoadUnwatchedMediaUseCaseContract
import com.sunrisekcdeveloper.search.usecase.SearchMediaByTitleUseCaseContract
import com.sunrisekcdeveloper.search.implementations.SearchRemoteDataSource
import com.sunrisekcdeveloper.search.implementations.SearchService
import com.sunrisekcdeveloper.search.implementations.SearchRepository
import com.sunrisekcdeveloper.search.implementations.LoadUnwatchedMediaUseCase
import com.sunrisekcdeveloper.search.implementations.SearchMediaByTitleUseCase
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
object SearchModule {

    @Provides
    @ViewModelScoped
    fun provideLoadUnwatchedMediaUseCase(
        repo: SearchRepositoryContract
    ): LoadUnwatchedMediaUseCaseContract = LoadUnwatchedMediaUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideSearchMediaByTitleUseCase(
        repo: SearchRepositoryContract
    ): SearchMediaByTitleUseCaseContract = SearchMediaByTitleUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideRepositorySearch(
        remote: SearchRemoteDataSourceContract,
        storage: TrackerDatabase
    ): SearchRepositoryContract = SearchRepository(
        remote,
        showRepo = TVShowRepository(storage),
        movieRepo = MovieRepository(storage)
    )

    @Provides
    @ViewModelScoped
    fun provideSearchService(
        retrofit: Retrofit
    ): SearchServiceContract = retrofit.create(SearchService::class.java)

    @Provides
    @ViewModelScoped
    fun provideRemoteDataSourceSearch(
        api: SearchServiceContract
    ): SearchRemoteDataSourceContract = SearchRemoteDataSource(api, NetworkContract.Impl())
}