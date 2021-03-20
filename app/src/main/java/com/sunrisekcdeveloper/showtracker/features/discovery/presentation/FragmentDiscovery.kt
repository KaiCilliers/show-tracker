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

package com.sunrisekcdeveloper.showtracker.features.discovery.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.asUIModelPosterList
import com.sunrisekcdeveloper.showtracker.databinding.FragmentDiscoveryBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ViewActionsDiscovery.FetchMovieAndShowData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FragmentDiscovery : Fragment() {

    private lateinit var binding: FragmentDiscoveryBinding
    private val viewModel: ViewModelDiscovery by viewModels()

    private val adapterPopularMovies = PagingAdapterSimplePoster()
    private val adapterPopularShows = PagingAdapterSimplePoster()
    private val adapterTopRatedMovies = PagingAdapterSimplePoster()
    private val adapterTopRatedShows = PagingAdapterSimplePoster()
    private val adapterUpcomingMovies = PagingAdapterSimplePoster()
    private val adapterAiringTodayShows = PagingAdapterSimplePoster()

    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoveryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setup()
        setupBinding()
        observeViewModel()
    }

    private fun observeViewModel() {
        job = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.streamPopularMovies.collectLatest {
                adapterPopularMovies.submitData(it)
            }
        }.also {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.streamPopularShows.collectLatest {
                    adapterPopularShows.submitData(it)
                }
            }
        }.also {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.streamTopRatedMovies.collectLatest {
                    adapterTopRatedMovies.submitData(it)
                }
            }
        }.also {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.streamTopRatedShows.collectLatest {
                    adapterTopRatedShows.submitData(it)
                }
            }
        }.also {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.streamUpcomingMovies.collectLatest {
                    adapterUpcomingMovies.submitData(it)
                }
            }
        }.also {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.streamAiringTodayShows.collectLatest {
                    adapterAiringTodayShows.submitData(it)
                }
            }
        }
    }

    private fun setupBinding() {
        val onClick = OnPosterClickListener { mediaId, mediaType ->
            when (mediaType) {
                MediaType.Movie -> {
                    findNavController().navigate(
                        // todo replace global navigation
                        FragmentDiscoveryDirections.navigateFromDiscoveryToBottomSheetDetailMovie(
                            mediaId
                        )
                    )
                }
                MediaType.Show -> {
                    findNavController().navigate(
                        FragmentDiscoveryDirections.navigateFromDiscoveryToBottomSheetDetailShow(
                            mediaId
                        )
                    )
                }
            }
        }

        adapterPopularMovies.onClick = onClick
        adapterPopularShows.onClick = onClick
        adapterTopRatedMovies.onClick = onClick
        adapterTopRatedShows.onClick = onClick
        adapterUpcomingMovies.onClick = onClick
        adapterAiringTodayShows.onClick = onClick

        binding.rcPopularMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcTopRatedMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcUpcomingMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcPopularShows.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcTopRatedShows.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcAiringTodayShows.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rcPopularMovies.adapter = adapterPopularMovies
        binding.rcTopRatedMovies.adapter = adapterTopRatedMovies
        binding.rcUpcomingMovies.adapter = adapterUpcomingMovies
        binding.rcPopularShows.adapter = adapterPopularShows
        binding.rcTopRatedShows.adapter = adapterTopRatedShows
        binding.rcAiringTodayShows.adapter = adapterAiringTodayShows

    }

    private fun setup() {
        // Navigation - Toolbar Search icon
        binding.toolbarDiscovery.menu.forEach {
            it.setOnMenuItemClickListener {
                findNavController().navigate(
                    FragmentDiscoveryDirections.navigateFromDiscoveryToNavGraphSearch()
                )
                true
            }
        }

        // Navigation - Tabs
        binding.tabsDiscovery.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    navigateToSelectedTab(it.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.let {
                    navigateToSelectedTab(it.position)
                }
            }
        })
    }

    private fun navigateToSelectedTab(position: Int) {
        // Movie Tab
        if (position == 0) {
            findNavController().navigate(
                // todo rename this awful action functions
                FragmentDiscoveryDirections.navigateFromDiscoveryToDiscoveryMoviesFragment()
            )
            // TV Show Tab
        } else if (position == 1) {
            findNavController().navigate(
                FragmentDiscoveryDirections.navigateFromDiscoveryToDiscoveryShowsFragment()
            )
        }
    }
}