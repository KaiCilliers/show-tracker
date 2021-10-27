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

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.get
import androidx.navigation.ui.setupWithNavController
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ActivityMain : AppCompatActivity() {

    // change to test scripts
    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedLong = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    private fun setup() {
        binding.botnavHousing.setupWithNavController(
            findNavController(R.id.nav_host_fragment_main)
        )
    }

    override fun onBackPressed() {
        Timber.e("${findNavController(R.id.nav_host_fragment_main).currentBackStackEntry?.destination}")
        Timber.e("${findNavController(R.id.nav_host_fragment_main).currentDestination}")
        if (findNavController(R.id.nav_host_fragment_main).currentDestination != findNavController(R.id.nav_host_fragment_main).graph[R.id.destination_main_discovery_fragment]
            || doubleBackToExitPressedLong + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            Toast.makeText(baseContext, "Press back again to leave", Toast.LENGTH_SHORT).show()
            doubleBackToExitPressedLong = System.currentTimeMillis()
        }
    }
}