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

import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DetClient
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DiscClient
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.SearchClientUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.detail.data.network.DetailRemoteDataSourceContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.detail.data.repository.DetailRepositoryUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.detail.domain.repository.DetailRepositoryContractUpdated
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.DiscoveryRemoteDataSourceContractUpdated
import com.sunrisekcdeveloper.showtracker.features.discovery.data.repository.DiscoveryRepositoryUpdated
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.repository.DiscoveryRepositoryContractUpdated
import com.sunrisekcdeveloper.showtracker.features.search.data.network.SearchRemoteDataSourceContractUpdated
import com.sunrisekcdeveloper.showtracker.features.search.data.repository.SearchRepositoryUpdated
import com.sunrisekcdeveloper.showtracker.features.search.domain.repository.SearchRepositoryContractUpdated
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
    annotation class DetRepo

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DiscRepo

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class WatchlistRepo

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SearchRepoUpdated

    @ActivityRetainedScoped
    @SearchRepoUpdated
    @Provides
    fun provideSearchRepositoryUpdated(
        @SearchClientUpdated remote: SearchRemoteDataSourceContractUpdated
    ): SearchRepositoryContractUpdated =
        SearchRepositoryUpdated(remote)

    @ActivityRetainedScoped
    @DetRepo
    @Provides
    fun provideDetailRepositoryUpdated(
        @DetClient remote: DetailRemoteDataSourceContractUpdated
    ): DetailRepositoryContractUpdated =
        DetailRepositoryUpdated(remote)

    @ActivityRetainedScoped
    @DiscRepo
    @Provides
    fun provideDiscoveryRepositoryUpdated(
        @DiscClient remote: DiscoveryRemoteDataSourceContractUpdated
    ): DiscoveryRepositoryContractUpdated =
        DiscoveryRepositoryUpdated(remote)
}