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
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.sunrisekcdeveloper.showtracker.databinding.ActivityPrimaryDestinationBinding

class PrimaryDestinationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrimaryDestinationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPrimaryDestinationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        setTitle("")

        setCurrentFragment(TVShowFragment())



        binding.bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.primary_destination_tvshow -> { setCurrentFragment(TVShowFragment()) }
                R.id.primary_destination_movie -> { setCurrentFragment(MovieFragment()) }
                R.id.primary_destination_discovery -> { setCurrentFragment(DiscFragment()) }
            }
            true
        }

        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_favorite_36)
        binding.toolbar.setNavigationOnClickListener {
            toaster(33)
        }
    }
    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment_content_primary_destination, fragment)
            commit()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.primary_action_search -> {toaster(2);true}
            else -> {super.onOptionsItemSelected(item)}
        }
    }

    fun toaster(k: Int) {
        Toast.makeText(this, "tttttt $k", Toast.LENGTH_SHORT).show()
    }
}