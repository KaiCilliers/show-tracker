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
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.TraktApi
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

    @ActivityRetainedScoped
    @MainRepo
    @Provides
    fun provideMainRepository(
        dao: MovieDao,
        @DataSourceTrakt remote: NetworkDataSourceContract
    ): RepositoryContract =
        MainRepository(dao, remote)
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