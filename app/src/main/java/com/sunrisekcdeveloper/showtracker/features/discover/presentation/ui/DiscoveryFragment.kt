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

package com.sunrisekcdeveloper.showtracker.features.discover.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.databinding.FragmentDiscoveryBinding
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.features.discover.presentation.adapter.DiscoverListAdapter
import com.sunrisekcdeveloper.showtracker.commons.util.subscribe
import com.sunrisekcdeveloper.showtracker.tmdb.model.MoviesAdapterTMDB
import com.sunrisekcdeveloper.showtracker.tmdb.model.ResponseMovieTMDB
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Home Fragment displays suggested movies and shows for the user to add to their watchlist,
 * categorised with appropriate headings
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DiscoveryFragment : Fragment() {

    private lateinit var adapterTMDB: MoviesAdapterTMDB
    private lateinit var popularMoviesLayoutManager: LinearLayoutManager

    private lateinit var topRatedMoviesAdapter: MoviesAdapterTMDB
    private lateinit var topRatedMoviesLayoutManager: LinearLayoutManager

    private lateinit var upcomingMoviesAdapter: MoviesAdapterTMDB
    private lateinit var upcomingMoviesLayoutManager: LinearLayoutManager

    @Inject
    lateinit var adapter: DiscoverListAdapter

    private lateinit var binding: FragmentDiscoveryBinding

    private val viewModel: DiscoveryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoveryBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        setupBinding()
        setupFilters()
        observeViewModel()
        MainScope().launch { setupTMDB() }
        return binding.root
    }

    private suspend fun setupTMDB() {
        attachPopularMoviesOnScrollListener()
        attachTopRatedMoviesOnScrollListener()
        attachUpcomingMoviesOnScrollListener()
        viewModel.getPopularMovies()
        viewModel.getTopRatedMovies()
        viewModel.getUpcomingMovies()
    }

    private fun setupFilters() {
        binding.chipFilterMovie.setOnCheckedChangeListener { _, checked ->
            Toast.makeText(requireContext(), "Movie: $checked", Toast.LENGTH_SHORT).show()
        }
        binding.chipFilterShows.setOnCheckedChangeListener { _, checked ->
            Toast.makeText(requireContext(), "Show: $checked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBinding() {
        adapterTMDB = MoviesAdapterTMDB(mutableListOf())
        topRatedMoviesAdapter = MoviesAdapterTMDB(mutableListOf())
        upcomingMoviesAdapter = MoviesAdapterTMDB(mutableListOf())

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

        binding.rcFeaturedCategoriesDiscover.layoutManager = popularMoviesLayoutManager
        binding.rcFeaturedCategoriesDiscover.adapter = adapterTMDB

        binding.rcTopRatedMovies.layoutManager = topRatedMoviesLayoutManager
        binding.rcTopRatedMovies.adapter = topRatedMoviesAdapter

        binding.rcUpcomingMovies.layoutManager = upcomingMoviesLayoutManager
        binding.rcUpcomingMovies.adapter = upcomingMoviesAdapter
//        // Temporal Coupling
//        adapter.addOnClickAction(object : ClickActionContract {
//            override fun onClick(item: Movie) {
//                Timber.d("Featured: $item")
//                findNavController().navigate(
//                    DiscoveryFragmentDirections.actionDiscoverFragmentDestToDetailFragment(item.slug)
//                )
//            }
//        })
//        binding.rcFeaturedCategoriesDiscover.adapter = adapter
//        binding.rcFeaturedCategoriesDiscover.layoutManager = LinearLayoutManager(
//            requireContext(), LinearLayoutManager.VERTICAL, false
//        )
    }

    private fun updatePopularMovies(list: List<ResponseMovieTMDB>) {
//        adapter.submitList(list)
        adapterTMDB.updateMovies(list)
    }
    private fun updateTopRatedMovies(list: List<ResponseMovieTMDB>) {
        topRatedMoviesAdapter.updateMovies(list)
    }
    private fun updateUpcomingMovies(list: List<ResponseMovieTMDB>) {
        upcomingMoviesAdapter.updateMovies(list)
    }

    private fun attachPopularMoviesOnScrollListener() {
        binding.rcFeaturedCategoriesDiscover.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // total number of movies inside adapter - this wil continuously increase the more
                // we call adapter.appendMovies()
                val totalItems = popularMoviesLayoutManager.itemCount
                // current number of child views attached to the RecyclerView that are currently
                // being recycled over and over again
                val visibleItemCount = popularMoviesLayoutManager.childCount
                // position of the leftmost visible item in the list
                val firstVisibleItem = popularMoviesLayoutManager.findFirstVisibleItemPosition()

                // true if the user scrolls past halfway plus a buffered value of visibleItemCount
                if (firstVisibleItem + visibleItemCount >= totalItems / 2) {
                    // reattached in viewmodel observer - reason is to prevent too many calls to
                    // fetch new movies, whether it helps or no is unclear
//                    binding.rcFeaturedCategoriesDiscover.removeOnScrollListener(this)
                    MainScope().launch { viewModel.getPopularMovies() }
                }
            }
        })
    }

    private fun attachTopRatedMoviesOnScrollListener() {
        binding.rcTopRatedMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // total number of movies inside adapter - this wil continuously increase the more
                // we call adapter.appendMovies()
                val totalItems = topRatedMoviesLayoutManager.itemCount
                // current number of child views attached to the RecyclerView that are currently
                // being recycled over and over again
                val visibleItemCount = topRatedMoviesLayoutManager.childCount
                // position of the leftmost visible item in the list
                val firstVisibleItem = topRatedMoviesLayoutManager.findFirstVisibleItemPosition()

                // true if the user scrolls past halfway plus a buffered value of visibleItemCount
                if (firstVisibleItem + visibleItemCount >= totalItems / 2) {
                    // reattached in viewmodel observer - reason is to prevent too many calls to
                    // fetch new movies, whether it helps or no is unclear
//                    binding.rcTopRatedMovies.removeOnScrollListener(this)
                    MainScope().launch { viewModel.getTopRatedMovies() }
                }
            }
        })
    }

    private fun attachUpcomingMoviesOnScrollListener() {
        binding.rcUpcomingMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // total number of movies inside adapter - this wil continuously increase the more
                // we call adapter.appendMovies()
                val totalItems = upcomingMoviesLayoutManager.itemCount
                // current number of child views attached to the RecyclerView that are currently
                // being recycled over and over again
                val visibleItemCount = upcomingMoviesLayoutManager.childCount
                // position of the leftmost visible item in the list
                val firstVisibleItem = upcomingMoviesLayoutManager.findFirstVisibleItemPosition()

                // true if the user scrolls past halfway plus a buffered value of visibleItemCount
                if (firstVisibleItem + visibleItemCount >= totalItems / 2) {
                    // reattached in viewmodel observer - reason is to prevent too many calls to
                    // fetch new movies, whether it helps or no is unclear
//                    binding.rcTopRatedMovies.removeOnScrollListener(this)
                    MainScope().launch { viewModel.getUpcomingMovies() }
                }
            }
        })
    }

    private fun observeViewModel() {
        viewModel.tmdb2.subscribe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    updatePopularMovies(it.data.movies)
//                    attachPopularMoviesOnScrollListener()
                }
                is Resource.Error -> {
                }
            }
        }
        viewModel.tmdbTopRated.subscribe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    updateTopRatedMovies(it.data.movies)
//                    attachTopRatedMoviesOnScrollListener()
                }
                is Resource.Error -> {
                }
            }
        }
        viewModel.tmdbUpcoming.subscribe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    updateUpcomingMovies(it.data.movies)
                }
                is Resource.Error -> {
                }
            }
        }
    }
}