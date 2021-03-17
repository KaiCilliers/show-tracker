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

package com.sunrisekcdeveloper.showtracker.features.progress.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.sunrisekcdeveloper.showtracker.databinding.ActivityProgressBinding
import com.sunrisekcdeveloper.showtracker.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint

// todo remove bottom nav bar when navigating to a new activity
//  i thought it did that automatically, hence using activities....
//  Search activity has the same issue :(
@AndroidEntryPoint
class ActivityProgress : AppCompatActivity() {
    private lateinit var binding: ActivityProgressBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}