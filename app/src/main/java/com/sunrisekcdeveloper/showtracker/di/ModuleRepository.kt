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

import com.sunrisekcdeveloper.showtracker.common.*
import com.sunrisekcdeveloper.showtracker.common.util.*
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.RemoteDataSourceDiscoveryContract
import com.sunrisekcdeveloper.showtracker.features.discovery.data.repository.RepositoryDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.repository.RepositoryDiscoveryContract
import com.sunrisekcdeveloper.showtracker.features.search.data.network.RemoteDataSourceSearchContract
import com.sunrisekcdeveloper.showtracker.features.search.data.repository.RepositorySearch
import com.sunrisekcdeveloper.showtracker.features.search.domain.repository.RepositorySearchContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(ActivityRetainedComponent::class)
object ModuleRepository {

    @ActivityRetainedScoped
    @RepoSearch
    @Provides
    fun provideSearchRepository(
        @SourceSearch remote: RemoteDataSourceSearchContract,
        db: TrackerDatabase
    ): RepositorySearchContract =
        RepositorySearch(remote, db)

    @ActivityRetainedScoped
    @RepoDiscovery
    @Provides
    fun provideRepositoryDiscovery(
        @SourceDiscovery remote: RemoteDataSourceDiscoveryContract,
        database: TrackerDatabase
    ): RepositoryDiscoveryContract =
        RepositoryDiscovery(remote, database)
}