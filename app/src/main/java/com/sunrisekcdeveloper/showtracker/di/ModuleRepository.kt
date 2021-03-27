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

import com.sunrisekcdeveloper.showtracker.common.TrackerDatabase
import com.sunrisekcdeveloper.showtracker.di.ModuleNetwork.SourceDetail
import com.sunrisekcdeveloper.showtracker.di.ModuleNetwork.SourceDiscovery
import com.sunrisekcdeveloper.showtracker.di.ModuleNetwork.SourceProgress
import com.sunrisekcdeveloper.showtracker.di.ModuleNetwork.SourceSearch
import com.sunrisekcdeveloper.showtracker.updated.features.detail.data.network.RemoteDataSourceDetailContract
import com.sunrisekcdeveloper.showtracker.features.detail.data.repository.RepositoryDetail
import com.sunrisekcdeveloper.showtracker.features.detail.domain.repository.RepositoryDetailContract
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.RemoteDataSourceDiscoveryContract
import com.sunrisekcdeveloper.showtracker.features.discovery.data.repository.RepositoryDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.repository.RepositoryDiscoveryContract
import com.sunrisekcdeveloper.showtracker.features.progress.data.network.RemoteDataSourceProgressContract
import com.sunrisekcdeveloper.showtracker.features.progress.data.repository.RepositoryProgress
import com.sunrisekcdeveloper.showtracker.features.progress.domain.repository.RepositoryProgressContract
import com.sunrisekcdeveloper.showtracker.features.search.data.network.RemoteDataSourceSearchContract
import com.sunrisekcdeveloper.showtracker.features.search.data.repository.RepositorySearch
import com.sunrisekcdeveloper.showtracker.features.search.domain.repository.RepositorySearchContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.repository.RepositoryWatchlist
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository.RepositoryWatchlistContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Qualifier

@ExperimentalCoroutinesApi
@Module
@InstallIn(ActivityRetainedComponent::class)
object ModuleRepository {

    @ActivityRetainedScoped
    @RepoProgress
    @Provides
    fun provideRepositoryProgress(
        @SourceProgress remote: RemoteDataSourceProgressContract,
        database: TrackerDatabase
    ) : RepositoryProgressContract =
        RepositoryProgress(remote, database)

    @ActivityRetainedScoped
    @RepoWatchlist
    @Provides
    fun provideRepositoryWatchlist(
        database: TrackerDatabase
    ) : RepositoryWatchlistContract =
        RepositoryWatchlist(database)

    @ActivityRetainedScoped
    @RepoSearch
    @Provides
    fun provideSearchRepositoryUpdated(
        @SourceSearch remote: RemoteDataSourceSearchContract,
        db: TrackerDatabase
    ): RepositorySearchContract =
        RepositorySearch(remote, db)

    @ActivityRetainedScoped
    @RepoDetail
    @Provides
    fun provideRepositoryDetail(
        @SourceDetail remote: RemoteDataSourceDetailContract,
        database: TrackerDatabase
    ): RepositoryDetailContract =
        RepositoryDetail(remote, database)

    @ActivityRetainedScoped
    @RepoDiscovery
    @Provides
    fun provideRepositoryDiscovery(
        @SourceDiscovery remote: RemoteDataSourceDiscoveryContract,
        database: TrackerDatabase
    ): RepositoryDiscoveryContract =
        RepositoryDiscovery(remote, database)

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RepoProgress

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RepoDetail

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RepoDiscovery

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RepoWatchlist

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RepoSearch

}