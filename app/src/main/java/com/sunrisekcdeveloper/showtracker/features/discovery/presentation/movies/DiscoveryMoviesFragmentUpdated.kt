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

package com.sunrisekcdeveloper.showtracker.features.discovery.presentation.movies

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
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.databinding.FragmentDiscoveryMoviesUpdatedBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.DiscoveryUIModel
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaTypeUpdated
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.DiscoveryFragmentUpdatedDirections
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.HorizontalPosterListAdapter
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.OnPosterClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DiscoveryMoviesFragmentUpdated : Fragment() {

    private lateinit var binding: FragmentDiscoveryMoviesUpdatedBinding

    private val viewModel: DiscoveryMoviesFragmentViewModel by viewModels()

    @Inject
    lateinit var popularMovieListAdapter: HorizontalPosterListAdapter
    private lateinit var popularMoviesLayoutManager: LinearLayoutManager

    @Inject
    lateinit var topRatedMovieListAdapter: HorizontalPosterListAdapter
    private lateinit var topRatedMoviesLayoutManager: LinearLayoutManager

    @Inject
    lateinit var upcomingMovieListAdapter: HorizontalPosterListAdapter
    private lateinit var upcomingMoviesLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoveryMoviesUpdatedBinding.inflate(inflater)
        renderSpinner()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setup()
        setupBinding()
        observeViewModel()
    }

    private fun setup() {
        // Navigation - Toolbar Up button
        binding.toolbarDiscoveryMovies.setNavigationOnClickListener { findNavController().popBackStack() }

        // Navigation - Toolbar Search
        binding.toolbarDiscoveryMovies.menu.forEach {
            it.setOnMenuItemClickListener {
                findNavController().navigate(
                    DiscoveryMoviesFragmentUpdatedDirections.actionDiscoveryMoviesFragmentUpdatedToSearchActivityUpdated()
                )
                true
            }
        }

        // Navigation - Spinner
        binding.spinnerDiscoveryMovies.onItemSelectedListener =
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
                            DiscoveryMoviesFragmentUpdatedDirections.actionDiscoveryMoviesFragmentUpdatedToNavigationDiscoveryUpdated()
                        )
                        // TV Show Discovery Screen
                    } else if (id == 2L) {
                        findNavController().navigate(
                            DiscoveryMoviesFragmentUpdatedDirections.actionDiscoveryMoviesFragmentUpdatedToDiscoveryShowsFragmentUpdated()
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
    }

    private fun renderSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.movie_dropdown_array,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerDiscoveryMovies.adapter = it
        }
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
    }

    private fun setupBinding() {
        val onClick = OnPosterClickListener { mediaId, mediaType ->
            when (mediaType) {
                MediaTypeUpdated.Movie -> {
                    findNavController().navigate(
                        DiscoveryFragmentUpdatedDirections.actionGlobalDetailMovieBottomSheet(mediaId)
                    )
                }
                MediaTypeUpdated.Show -> {
                    findNavController().navigate(
                        DiscoveryFragmentUpdatedDirections.actionGlobalDetailShowBottomSheet(mediaId)
                    )
                }
            }
        }

        popularMovieListAdapter.onPosterClickListener = onClick
        topRatedMovieListAdapter.onPosterClickListener = onClick
        upcomingMovieListAdapter.onPosterClickListener = onClick

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

        binding.rcPopularMovies.layoutManager = popularMoviesLayoutManager
        binding.rcPopularMovies.adapter = popularMovieListAdapter

        binding.rcTopRatedMovies.layoutManager = topRatedMoviesLayoutManager
        binding.rcTopRatedMovies.adapter = topRatedMovieListAdapter

        binding.rcUpcomingMovies.layoutManager = upcomingMoviesLayoutManager
        binding.rcUpcomingMovies.adapter = upcomingMovieListAdapter

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
    }

    private fun updateList(
        adapter: HorizontalPosterListAdapter,
        list: List<DiscoveryUIModel>
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