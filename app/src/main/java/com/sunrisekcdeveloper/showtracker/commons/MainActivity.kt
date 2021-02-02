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

package com.sunrisekcdeveloper.showtracker.commons

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * Main activity Primary container for fragments that provide app functionality
 *
 * @constructor Create empty Main activity
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setup()
    }

    // TODO this bottom nav bar needs to be gone when navigating to movie details fragment
    private fun setup() {
        binding.bottomNavigationMain.setupWithNavController(findNavController(R.id.nav_host_fragment_main))
        // todo change toolbar text when fragment changes - this impl overrides navigation with navcontroller
//        val toolbar = supportActionBar
//        binding.bottomNavigationMain.setOnNavigationItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.watchlist_fragment_dest -> { toolbar?.title = "Watchlist"; true }
//                R.id.discover_fragment_dest -> { toolbar?.title = "Discover"; true }
//                R.id.search_fragment_dest -> { toolbar?.title = "Search"; true }
//                else -> false
//            }
//        }
    }

}