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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.databinding.FragmentDiscoveryBinding
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.commons.util.subscribe
import com.sunrisekcdeveloper.showtracker.features.discover.presentation.adapter.MovieListAdapter
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.ResponseMovieTMDB
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Home Fragment displays suggested movies and shows for the user to add to their watchlist,
 * categorised with appropriate headings
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DiscoveryFragment : Fragment() {

    @Inject
    lateinit var popularMovieListAdapter: MovieListAdapter
    private lateinit var popularMoviesLayoutManager: LinearLayoutManager

    @Inject
    lateinit var topRatedMovieListAdapter: MovieListAdapter
    private lateinit var topRatedMoviesLayoutManager: LinearLayoutManager

    @Inject
    lateinit var upcomingMovieListAdapter: MovieListAdapter
    private lateinit var upcomingMoviesLayoutManager: LinearLayoutManager

    private lateinit var binding: FragmentDiscoveryBinding

    private val viewModel: DiscoveryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoveryBinding.inflate(inflater)
        setupBinding()
        setupFilters()
        observeViewModel()
        return binding.root
    }

    private fun setupFilters() {
        binding.chipFilterMovie.setOnCheckedChangeListener { _, checked ->
            Toast.makeText(requireContext(), "Movie: $checked", Toast.LENGTH_SHORT).show()
        }
        binding.chipFilterShows.setOnCheckedChangeListener { _, checked ->
            Toast.makeText(requireContext(), "Show: $checked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showMovieDetails(movie: ResponseMovieTMDB) {
        findNavController().navigate(
            DiscoveryFragmentDirections.actionDiscoverFragmentDestToDetailFragmentTMDB(
                movieBackdrop = movie.backdropPath,
                moviePoster = movie.posterPath,
                movieTitle = movie.title,
                movieRating = movie.rating,
                movieReleaseDate = movie.releaseDate,
                movieOverview = movie.overview
            )
        )
    }

    private fun onAddMediaClicked(movie: ResponseMovieTMDB) {
        viewModel.addMediaToRecentlyAdded(movie)
    }

    private fun setupBinding() {
        popularMovieListAdapter.onMovieClick = { movie -> showMovieDetails(movie) }
        topRatedMovieListAdapter.onMovieClick = { movie -> showMovieDetails(movie) }
        upcomingMovieListAdapter.onMovieClick = { movie -> showMovieDetails(movie) }

        popularMovieListAdapter.onAddClicked = { movie -> onAddMediaClicked(movie) }
        topRatedMovieListAdapter.onAddClicked = { movie -> onAddMediaClicked(movie) }
        upcomingMovieListAdapter.onAddClicked = { movie -> onAddMediaClicked(movie) }

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
        binding.rcFeaturedCategoriesDiscover.adapter = popularMovieListAdapter

        binding.rcTopRatedMovies.layoutManager = topRatedMoviesLayoutManager
        binding.rcTopRatedMovies.adapter = topRatedMovieListAdapter

        binding.rcUpcomingMovies.layoutManager = upcomingMoviesLayoutManager
        binding.rcUpcomingMovies.adapter = upcomingMovieListAdapter

        attachOnScrollListener(
            binding.rcFeaturedCategoriesDiscover,
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

    private fun observeViewModel() {
        viewModel.popularMovies.subscribe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    updateList(popularMovieListAdapter, it.data.movies)
                    attachOnScrollListener(
                        binding.rcFeaturedCategoriesDiscover,
                        popularMoviesLayoutManager
                    ) { viewModel.getPopularMovies() }
                }
                is Resource.Error -> {
                }
            }
        }
        viewModel.topRatedMovies.subscribe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    updateList(topRatedMovieListAdapter, it.data.movies)
                    attachOnScrollListener(
                        binding.rcTopRatedMovies,
                        topRatedMoviesLayoutManager
                    ) { viewModel.getTopRatedMovies() }
                }
                is Resource.Error -> {
                }
            }
        }
        viewModel.upcomingMovies.subscribe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    updateList(upcomingMovieListAdapter, it.data.movies)
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

    private fun updateList(
        adapter: MovieListAdapter,
        list: List<ResponseMovieTMDB>
    ) {
        adapter.updateMovies(list)
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