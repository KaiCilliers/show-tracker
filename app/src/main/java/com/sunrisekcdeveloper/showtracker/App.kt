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

package com.sunrisekcdeveloper.showtracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * App Extension of [Application]
 *
 * TODO - Reasoning for decision made regarding using Hilt to inject ViewHolder and Adapters
 *  .
 *  Firstly I had a problem injecting Adapters due to them taking an interface with an onClick
 *  which is to be passed to the ViewHolder to be used to navigate to destinations when a View
 *  is clicked. I tried to move that logic straight to the ViewHolder itself posed a new issue
 *  of obtaining a NavController. After obtaining the NavController by passing Context and
 *  casting it to an Activity, I had to obtain the Fragment current on screen and use its
 *  autogenerated Directions class for navigation (manual navigation posed other maintenance issues)
 *  .
 *  Secondly, injecting a ViewHolder also presented its own problems as it requires a View as a
 *  dependency which is only created at runtime. Also, ViewHolders are not something you'd be
 *  swapping out in testing.
 *  .
 *  I ended up choosing to make my adapter constructors empty and giving them a private property
 *  which can be set via a function. This function must be called before adding the adapter to
 *  a recyclerview. I chose this option, because I want to test out the Hilt framework more than
 *  I want to avoid some temporal coupling.
 */
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
