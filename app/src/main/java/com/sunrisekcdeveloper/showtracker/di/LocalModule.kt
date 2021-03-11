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

import android.content.Context
import androidx.room.Room
import com.sunrisekcdeveloper.showtracker.commons.TrackerDatabase
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.WatchlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 * Local module
 *
 * @constructor Create empty Local module
 */
@Module
@InstallIn(ApplicationComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TrackerDatabase = Room.databaseBuilder(
        context,
        TrackerDatabase::class.java,
        "tracker_database")
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideWatchlistDao(db: TrackerDatabase): WatchlistDao = db.watchlistDao()
}