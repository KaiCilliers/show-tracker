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

package com.sunrisekcdeveloper.showtracker.features.discovery.presentation.shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.databinding.FragmentDiscoveryOnlyShowsBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.AdapterSimplePoster
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentDiscoveryShows : Fragment() {

    private lateinit var binding: FragmentDiscoveryOnlyShowsBinding

    private val viewModel: ViewModelDiscoveryShows by viewModels()

    @Inject
    lateinit var popularShowListAdapter: AdapterSimplePoster
    private lateinit var popularShowLayoutManager: LinearLayoutManager

    @Inject
    lateinit var topRatedShowsListAdapter: AdapterSimplePoster
    private lateinit var topRatedShowLayoutManager: LinearLayoutManager

    @Inject
    lateinit var airingTodayShowListAdapter: AdapterSimplePoster
    private lateinit var airingTodayShowLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoveryOnlyShowsBinding.inflate(inflater)
        renderSpinner()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setup()
        setupBinding()
        observeViewModel()
    }

    private fun setupBinding() {
        val onClick = OnPosterClickListener { mediaId, mediaType ->
            when (mediaType) {
                MediaType.Movie -> {
                    findNavController().navigate(
                        FragmentDiscoveryShowsDirections.actionGlobalDetailMovieBottomSheet(mediaId)
                    )
                }
                MediaType.Show -> {
                    findNavController().navigate(
                        FragmentDiscoveryShowsDirections.actionGlobalDetailShowBottomSheet(mediaId)
                    )
                }
            }
        }

        popularShowListAdapter.onPosterClickListener = onClick
        topRatedShowsListAdapter.onPosterClickListener = onClick
        airingTodayShowListAdapter.onPosterClickListener = onClick

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

        binding.rcPopularShows.layoutManager = popularShowLayoutManager
        binding.rcPopularShows.adapter = popularShowListAdapter

        binding.rcTopRatedShows.layoutManager = topRatedShowLayoutManager
        binding.rcTopRatedShows.adapter = topRatedShowsListAdapter

        binding.rcAiringTodayShows.layoutManager = airingTodayShowLayoutManager
        binding.rcAiringTodayShows.adapter = airingTodayShowListAdapter

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
    }

    private fun setup() {
        // Navigation - Toolbar Up button
        binding.toolbarDiscoveryShows.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // Navigation - Toolbar Search
        binding.toolbarDiscoveryShows.menu.forEach {
            it.setOnMenuItemClickListener {
                findNavController().navigate(
                    FragmentDiscoveryShowsDirections.actionDiscoveryShowsFragmentUpdatedToSearchActivityUpdated()
                )
                true
            }
        }

        // Navigation - Spinner
        binding.spinnerDiscoveryShows.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Discovery Screen
                    if (id == 1L) {
                        findNavController().navigate(
                            FragmentDiscoveryShowsDirections.actionDiscoveryShowsFragmentUpdatedToNavigationDiscoveryUpdated()
                        )
                        // Discovery Movies Screen
                    } else if (id == 2L) {
                        findNavController().navigate(
                            FragmentDiscoveryShowsDirections.actionDiscoveryShowsFragmentUpdatedToDiscoveryMoviesFragmentUpdated()
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
    }

    private fun renderSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.show_dropdown_array,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerDiscoveryShows.adapter = it
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