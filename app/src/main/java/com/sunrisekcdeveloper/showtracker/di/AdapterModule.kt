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

import com.sunrisekcdeveloper.showtracker.features.detail.adapters.MediumPosterAdapterDetail
import com.sunrisekcdeveloper.showtracker.features.search.adapters.MediumPosterAdapter
import com.sunrisekcdeveloper.showtracker.features.watchlist.adapters.MovieSummaryAdapter
import com.sunrisekcdeveloper.showtracker.features.watchlist.adapters.SmallPosterAdapter
import com.sunrisekcdeveloper.showtracker.features.discover.adapters.SuggestionListAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Qualifier

@Module
@InstallIn(ActivityComponent::class)
object AdapterModule {

    @ActivityScoped
    @Provides
    fun provideMediumPosterAdapterSearch(): MediumPosterAdapter {
        return MediumPosterAdapter()
    }

    @ActivityScoped
    @Provides
    fun provideMediumPosterAdapterDetail(): MediumPosterAdapterDetail {
        return MediumPosterAdapterDetail()
    }

    @ActivityScoped
    @Provides
    fun provideSmallPosterAdapter(): SmallPosterAdapter {
        return SmallPosterAdapter()
    }

    @ActivityScoped
    @Provides
    fun provideMovieSummaryAdapter(): MovieSummaryAdapter {
        return MovieSummaryAdapter()
    }

    @ActivityScoped
    @Provides
    fun provideSuggestionListAdapter(): SuggestionListAdapter {
        return SuggestionListAdapter()
    }
}