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

package com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunrisekcdeveloper.showtracker.commons.util.asDomainMovie
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.databinding.FragmentWatchlistBinding
import com.sunrisekcdeveloper.showtracker.commons.util.subscribe
import com.sunrisekcdeveloper.showtracker.commons.models.local.RecentlyAddedMediaEntity
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.MediaModel
import com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.adapter.WatchlistMediaAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Progress Fragment that displays upcoming movies and shows with the capability to filter
 * based on movie or show
 */
@AndroidEntryPoint
class WatchlistFragment : Fragment() {

    @Inject
    lateinit var recentlyAddedMediaListAdapter: WatchlistMediaAdapter
    private lateinit var recentlyAddedMediaLayoutManager: LinearLayoutManager

    private val viewModel: WatchlistViewModel by viewModels()

    private lateinit var binding: FragmentWatchlistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWatchlistBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner // This removes observers when fragment is destroyed
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

    private fun showMovieDetails(media: MediaModel) {
        findNavController().navigate(
            WatchlistFragmentDirections.actionWatchlistFragmentDestToDetailFragmentTMDB(
                movieBackdrop = media.backdropPath,
                moviePoster = media.posterPath,
                movieTitle = media.title,
                movieRating = media.rating,
                movieReleaseDate = media.releaseDate,
                movieOverview = media.overview
            )
        )
    }

    private fun setupBinding() {
        recentlyAddedMediaListAdapter.onMediaClicked = { movie -> showMovieDetails(movie) }
        recentlyAddedMediaLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rcFeaturedCategoriesWatchlist.layoutManager = recentlyAddedMediaLayoutManager
        binding.rcFeaturedCategoriesWatchlist.adapter = recentlyAddedMediaListAdapter
    }

    private fun updateList(
        adapter: WatchlistMediaAdapter,
        entityList: List<RecentlyAddedMediaEntity>
    ) {
        val list = entityList.map { it.asDomainMovie() }
        adapter.updateList(list)
    }

    private fun observeViewModel() {
        viewModel.recentlyAddedMovies.subscribe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> { }
                is Resource.Success -> {
                    updateList(recentlyAddedMediaListAdapter, it.data)
                }
                is Resource.Error -> { }
            }
        }
    }
}