/*
 * Copyright © 2020. The Android Open Source Project
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

import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DiscoveryClient
import com.sunrisekcdeveloper.showtracker.features.detail.DetailDao
import com.sunrisekcdeveloper.showtracker.features.detail.client.DetailDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.detail.DetailRepository
import com.sunrisekcdeveloper.showtracker.features.detail.DetailRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.discover.application.LoadFeaturedMediaUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.discover.data.local.DiscoveryDao
import com.sunrisekcdeveloper.showtracker.features.discover.data.network.DiscoveryRemoteDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.discover.data.repository.DiscoveryRepository
import com.sunrisekcdeveloper.showtracker.features.discover.domain.repository.DiscoveryRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.discover.domain.usecase.LoadFeaturedMediaUseCase
import com.sunrisekcdeveloper.showtracker.features.watchlist.WatchListRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.WatchlistDao
import com.sunrisekcdeveloper.showtracker.features.watchlist.client.WatchlistDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.WatchlistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Qualifier

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DiscoveryRepo

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class WatchlistRepo

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DetailRepo

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UseCase

    @ActivityRetainedScoped
    @DiscoveryRepo
    @Provides
    fun provideDiscoveryRepository(
        dao: DiscoveryDao,
        @DiscoveryClient remote: DiscoveryRemoteDataSourceContract
    ): DiscoveryRepositoryContract =
        DiscoveryRepository(dao, remote)

    @ActivityRetainedScoped
    @UseCase
    @Provides
    fun provideTemp(
        @DiscoveryRepo repo: DiscoveryRepositoryContract
    ): LoadFeaturedMediaUseCaseContract =
        LoadFeaturedMediaUseCase(repo)

    @ActivityRetainedScoped
    @WatchlistRepo
    @Provides
    fun provideWatchlistRepository(
        dao: WatchlistDao,
        @NetworkModule.WatchlistClient remote: WatchlistDataSourceContract
    ): WatchListRepositoryContract =
        WatchlistRepository(dao, remote)

    @ActivityRetainedScoped
    @DetailRepo
    @Provides
    fun provideDetailRepository(
        dao: DetailDao,
        @NetworkModule.DetailClient remote: DetailDataSourceContract
    ): DetailRepositoryContract =
        DetailRepository(dao, remote)
}