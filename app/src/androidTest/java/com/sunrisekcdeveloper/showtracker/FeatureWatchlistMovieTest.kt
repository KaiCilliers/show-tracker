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

package com.sunrisekcdeveloper.showtracker

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.sunrisekcdeveloper.showtracker.common.ActivityMain
import com.sunrisekcdeveloper.showtracker.common.TrackerDatabase
import com.sunrisekcdeveloper.showtracker.di.ModuleLocal
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@LargeTest
@HiltAndroidTest
@UninstallModules(ModuleLocal::class)
@RunWith(AndroidJUnit4::class)
class FeatureWatchlistMovieTest{

    @get:Rule
    val rule = HiltAndroidRule(this)

    @Module
    @InstallIn(ApplicationComponent::class)
    object ModuleTest {
        @Singleton
        @Provides
        fun provideDatabase(
            @ApplicationContext context: Context
        ): TrackerDatabase = Room.inMemoryDatabaseBuilder(context, TrackerDatabase::class.java).build()
    }

    @Before
    fun setup() {
        rule.inject()
    }

    @Test
    fun mark_movie_as_watched() {
        // Launch application
        ActivityScenario.launch(ActivityMain::class.java)

        // Click a movie on start destination
        onView(withId(R.id.rc_popular_movies)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click())
        )

        // Add movie to watchlist
        onView(
            allOf(
                withId(R.id.btn_detail_movie_add),
                isDisplayed()
            )
        ).perform(click())

        // Wait for BottomSheet animations
        Thread.sleep(500)


        // Navigate to watchlist fragment
        onView(
            allOf(
                withId(R.id.destination_main_watchlist_fragment),
                withContentDescription("Watchlist"),
                isDisplayed()
            )
        ).perform(click())

        // Navigate to movies tab
        onView(
            allOf(
                withContentDescription("Movies"),
                isDisplayed()
            )
        ).perform(click())

        // Assert there is watchlist movies
        onView(withId(R.id.recyclerview_watchlist))
            .check(matches(isDisplayed()))
    }
}