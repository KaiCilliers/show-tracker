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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.databinding.FragmentDiscoveryBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ViewActionsDiscovery.FetchMovieAndShowData
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ViewStateDiscovery
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FragmentDiscovery : Fragment() {

    @Inject
    lateinit var popularMovieListAdapter: AdapterSimplePoster
    private lateinit var popularMoviesLayoutManager: LinearLayoutManager

    @Inject
    lateinit var topRatedMovieListAdapter: AdapterSimplePoster
    private lateinit var topRatedMoviesLayoutManager: LinearLayoutManager

    @Inject
    lateinit var upcomingMovieListAdapter: AdapterSimplePoster
    private lateinit var upcomingMoviesLayoutManager: LinearLayoutManager

    @Inject
    lateinit var popularShowListAdapter: AdapterSimplePoster
    private lateinit var popularShowLayoutManager: LinearLayoutManager

    @Inject
    lateinit var topRatedShowsListAdapter: AdapterSimplePoster
    private lateinit var topRatedShowLayoutManager: LinearLayoutManager

    @Inject
    lateinit var airingTodayShowListAdapter: AdapterSimplePoster
    private lateinit var airingTodayShowLayoutManager: LinearLayoutManager

    private lateinit var binding: FragmentDiscoveryBinding
    private val viewModel: ViewModelDiscovery by viewModels()

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
        viewModel.performAction(FetchMovieAndShowData)
    }

    private fun setupBinding() {
        val onClick = OnPosterClickListener { mediaId, mediaType ->
            when (mediaType) {
                MediaType.Movie -> {
                    findNavController().navigate(
                        // todo replace global navigation
                        FragmentDiscoveryDirections.actionGlobalDetailMovieBottomSheet(mediaId)
                    )
                }
                MediaType.Show -> {
                    findNavController().navigate(
                        FragmentDiscoveryDirections.actionGlobalDetailShowBottomSheet(mediaId)
                    )
                }
            }
        }

        popularMovieListAdapter.onPosterClickListener = onClick
        topRatedMovieListAdapter.onPosterClickListener = onClick
        upcomingMovieListAdapter.onPosterClickListener = onClick
        popularShowListAdapter.onPosterClickListener = onClick
        topRatedShowsListAdapter.onPosterClickListener = onClick
        airingTodayShowListAdapter.onPosterClickListener = onClick

        popularMoviesLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        topRatedMoviesLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        upcomingMoviesLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        popularShowLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        topRatedShowLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        airingTodayShowLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.rcPopularMovies.layoutManager = popularMoviesLayoutManager
        binding.rcPopularMovies.adapter = popularMovieListAdapter

        binding.rcTopRatedMovies.layoutManager = topRatedMoviesLayoutManager
        binding.rcTopRatedMovies.adapter = topRatedMovieListAdapter

        binding.rcUpcomingMovies.layoutManager = upcomingMoviesLayoutManager
        binding.rcUpcomingMovies.adapter = upcomingMovieListAdapter

        binding.rcPopularShows.layoutManager = popularShowLayoutManager
        binding.rcPopularShows.adapter = popularShowListAdapter

        binding.rcTopRatedShows.layoutManager = topRatedShowLayoutManager
        binding.rcTopRatedShows.adapter = topRatedShowsListAdapter

        binding.rcAiringTodayShows.layoutManager = airingTodayShowLayoutManager
        binding.rcAiringTodayShows.adapter = airingTodayShowListAdapter

        attachOnScrollListener(
            binding.rcPopularMovies,
            popularMoviesLayoutManager
        ) { viewModel.getPopularMovies() }
        attachOnScrollListener(
            binding.rcTopRatedMovies,
            topRatedMoviesLayoutManager
        ) { viewModel.getTopRatedMovies() }
        attachOnScrollListener(
            binding.rcUpcomingMovies,
            upcomingMoviesLayoutManager
        ) { viewModel.getUpcomingMovies() }
        attachOnScrollListener(
            binding.rcPopularShows,
            popularShowLayoutManager
        ) { viewModel.getPopularShows() }
        attachOnScrollListener(
            binding.rcTopRatedShows,
            topRatedShowLayoutManager
        ) { viewModel.getTopRatedShows() }
        attachOnScrollListener(
            binding.rcAiringTodayShows,
            airingTodayShowLayoutManager
        ) { viewModel.getAiringTodayShows() }
    }

    private fun observeViewModel() {
        viewModel.popularMovies.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    updateList(popularMovieListAdapter, it.data)
                    attachOnScrollListener(
                        binding.rcPopularMovies,
                        popularMoviesLayoutManager
                    ) { viewModel.getPopularMovies() }
                }
                is Resource.Error -> {
                }
            }
        }
        viewModel.topRatedMovies.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    updateList(topRatedMovieListAdapter, it.data)
                    attachOnScrollListener(
                        binding.rcTopRatedMovies,
                        topRatedMoviesLayoutManager
                    ) { viewModel.getTopRatedMovies() }
                }
                is Resource.Error -> {
                }
            }
        }
        viewModel.upcomingMovies.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    updateList(upcomingMovieListAdapter, it.data)
                    attachOnScrollListener(
                        binding.rcUpcomingMovies,
                        upcomingMoviesLayoutManager
                    ) { viewModel.getUpcomingMovies() }
                }
                is Resource.Error -> {
                }
            }
        }
        viewModel.popularShows.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    updateList(popularShowListAdapter, it.data)
                    attachOnScrollListener(
                        binding.rcPopularShows,
                        popularShowLayoutManager
                    ) { viewModel.getPopularShows() }
                }
                is Resource.Error -> {
                }
            }
        }
        viewModel.topRatedShows.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    updateList(topRatedShowsListAdapter, it.data)
                    attachOnScrollListener(
                        binding.rcTopRatedShows,
                        topRatedShowLayoutManager
                    ) { viewModel.getTopRatedShows() }
                }
                is Resource.Error -> {
                }
            }
        }
        viewModel.airingTodayShows.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    updateList(airingTodayShowListAdapter, it.data)
                    attachOnScrollListener(
                        binding.rcAiringTodayShows,
                        airingTodayShowLayoutManager
                    ) { viewModel.getAiringTodayShows() }
                }
                is Resource.Error -> {
                }
            }
        }
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                ViewStateDiscovery.Loading -> { }
                ViewStateDiscovery.Error ->  { }
                ViewStateDiscovery.Success -> { }
            }
        }
    }

    private fun setup() {
        // Navigation - Toolbar Search icon
        binding.toolbarDiscovery.menu.forEach {
            it.setOnMenuItemClickListener {
                findNavController().navigate(
                    FragmentDiscoveryDirections.actionNavigationDiscoveryUpdatedToSearchActivityUpdated()
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
                FragmentDiscoveryDirections.actionNavigationDiscoveryUpdatedToDiscoveryMoviesFragmentUpdated()
            )
            // TV Show Tab
        } else if (position == 1) {
            findNavController().navigate(
                FragmentDiscoveryDirections.actionNavigationDiscoveryUpdatedToDiscoveryShowsFragmentUpdated()
            )
        }
    }

    private fun updateList(
        adapter: AdapterSimplePoster,
        list: List<UIModelDiscovery>
    ) {
        adapter.updateList(list)
    }

    private fun attachOnScrollListener(
        recyclerView: RecyclerView,
        layoutManager: LinearLayoutManager,
        fetchNextPage: suspend () -> Unit
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // total number of movies inside adapter
                val totalItems = layoutManager.itemCount
                // current number of child views attached to the RecyclerView that are currently
                // being recycled
                val visibleItemCount = layoutManager.childCount
                // position of the leftmost visible item in the list
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                // true if the user scrolls past halfway plus a buffered value of visibleItemCount
                if (firstVisibleItem + visibleItemCount >= totalItems / 2) {
                    // This is to limit network calls
                    recyclerView.removeOnScrollListener(this)
                    MainScope().launch { fetchNextPage() }
                }
            }
        })
    }
}