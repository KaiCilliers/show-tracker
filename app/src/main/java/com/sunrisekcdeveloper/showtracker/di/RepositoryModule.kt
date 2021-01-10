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

import com.sunrisekcdeveloper.showtracker.data.local.MovieDao
import com.sunrisekcdeveloper.showtracker.data.network.ApiServiceContract
import com.sunrisekcdeveloper.showtracker.data.network.NetworkDataSourceContract
import com.sunrisekcdeveloper.showtracker.data.network.TraktDataSource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DataSourceTrakt
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DiscoveryClient
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.TraktApi
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.SearchRepo
import com.sunrisekcdeveloper.showtracker.features.discover.DiscoveryDao
import com.sunrisekcdeveloper.showtracker.features.discover.DiscoveryDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.discover.DiscoveryRepository
import com.sunrisekcdeveloper.showtracker.features.discover.DiscoveryRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.search.SearchDao
import com.sunrisekcdeveloper.showtracker.features.search.SearchDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.search.SearchRepository
import com.sunrisekcdeveloper.showtracker.features.search.SearchRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.WatchListRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.WatchlistDao
import com.sunrisekcdeveloper.showtracker.features.watchlist.WatchlistDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.WatchlistRepository
import com.sunrisekcdeveloper.showtracker.repository.MainRepository
import com.sunrisekcdeveloper.showtracker.repository.RepositoryContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainRepo

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DiscoveryRepo

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SearchRepo

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class WatchlistRepo

    @ActivityRetainedScoped
    @MainRepo
    @Provides
    fun provideMainRepository(
        dao: MovieDao,
        @DataSourceTrakt remote: NetworkDataSourceContract
    ): RepositoryContract =
        MainRepository(dao, remote)

    @ActivityRetainedScoped
    @DiscoveryRepo
    @Provides
    fun provideDiscoveryRepository(
        dao: DiscoveryDao,
        @DiscoveryClient remote: DiscoveryDataSourceContract
    ): DiscoveryRepositoryContract =
        DiscoveryRepository(dao, remote)

    @ActivityRetainedScoped
    @SearchRepo
    @Provides
    fun provideSearchRepository(
        dao: SearchDao,
        @NetworkModule.SearchClient remote: SearchDataSourceContract
    ): SearchRepositoryContract =
        SearchRepository(dao, remote)

    @ActivityRetainedScoped
    @WatchlistRepo
    @Provides
    fun provideWatchlistRepository(
        dao: WatchlistDao,
        @NetworkModule.WatchlistClient remote: WatchlistDataSourceContract
    ): WatchListRepositoryContract =
        WatchlistRepository(dao, remote)
}

@Module
@InstallIn(ActivityComponent::class)
object TempMod {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Different

    @ActivityScoped
    @Different
    @Provides
    fun provideTraktDataSource(
        @TraktApi api: ApiServiceContract
    ) : NetworkDataSourceContract {
        return TraktDataSource(api)
    }
}