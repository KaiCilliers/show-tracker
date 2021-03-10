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

import com.sunrisekcdeveloper.showtracker.features.discover.presentation.adapter.MovieListAdapter
import com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.adapter.WatchlistMediaAdapter
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.presentation.HorizontalPosterListAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AdapterModule {

    @Provides
    fun provideHorizontalPosterListAdapter(): HorizontalPosterListAdapter {
        return HorizontalPosterListAdapter(mutableListOf())
    }

    @Provides
    fun provideMovieListAdapter(): MovieListAdapter {
        return MovieListAdapter(mutableListOf())
    }

    @Provides
    fun providesWatchListMediaAdapter(): WatchlistMediaAdapter {
        return WatchlistMediaAdapter(mutableListOf())
    }
}