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

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.sunrisekcdeveloper.showtracker.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class MainActivityModule {
    @Provides
    // TODO Useful injection that can be used later
    //  https://stackoverflow.com/questions/63426685/android-dagger-hilt-inject-navigation-component
    fun provideNavController(activity: Activity): NavController {
        return activity.findNavController(R.id.nav_host_fragment_main)
    }
}