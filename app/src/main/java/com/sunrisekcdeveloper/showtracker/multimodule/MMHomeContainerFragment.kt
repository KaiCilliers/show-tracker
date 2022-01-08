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

package com.sunrisekcdeveloper.showtracker.multimodule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.discovery.DiscFragment
import com.sunrisekcdeveloper.showtracker.MovieFragment
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.TVShowFragment
import com.sunrisekcdeveloper.showtracker.databinding.FragmentHomeBinding
import com.sunrisekcdeveloper.ui_components.TVShowViewHolder

class MMHomeContainerFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val myListMovieFragment: MovieFragment by lazy { MovieFragment() }
    private val myListTVShowFragment: TVShowFragment by lazy { TVShowFragment() }
    private val discoveryFragment: DiscFragment by lazy { DiscFragment() }
    private var activeFragment: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            addFragments()
            setupBottomNavItemSelectedListener()
        }
    }

    private fun addFragments() {
        if (childFragmentManager.fragments.isEmpty()) {
            // set active fragment
            activeFragment = myListMovieFragment

            // add fragments to the FragmentContainer
            childFragmentManager.beginTransaction()
                .add(R.id.homeContainerView, myListTVShowFragment, "MyListTVShowFragment")
                .hide(myListTVShowFragment)
                .add(R.id.homeContainerView, discoveryFragment, "DiscoveryFragment")
                .hide(discoveryFragment)
                .add(R.id.homeContainerView, myListMovieFragment, "MyListMovieFragment")
                .commit()
        }
    }

    private fun setupBottomNavItemSelectedListener() {
        binding.bottomNavHome.setOnItemSelectedListener { item ->
            return@setOnItemSelectedListener when(item.itemId) {
                R.id.primary_destination_movie -> {
                    childFragmentManager.beginTransaction()
                        .hide(activeFragment!!)
                        .show(myListMovieFragment)
                        .commit()
                    activeFragment = myListMovieFragment
                    true
                }
                R.id.primary_destination_tvshow -> {
                    childFragmentManager.beginTransaction()
                        .hide(activeFragment!!)
                        .show(myListTVShowFragment)
                        .commit()
                    activeFragment = myListTVShowFragment
                    true
                }
                R.id.primary_destination_discovery -> {
                    childFragmentManager.beginTransaction()
                        .hide(activeFragment!!)
                        .show(discoveryFragment)
                        .commit()
                    activeFragment = discoveryFragment
                    true
                }
                else -> false
            }
        }
    }
}