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
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.databinding.FragmentWatchlistBinding
import com.sunrisekcdeveloper.showtracker.commons.util.subscribe
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.WatchListType
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.MediaModelSealed
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

    @Inject
    lateinit var inProgressMediaListAdapter: WatchlistMediaAdapter
    private lateinit var inProgressMediaLayoutManager: LinearLayoutManager

    @Inject
    lateinit var upComingMediaListAdapter: WatchlistMediaAdapter
    private lateinit var upComingMediaLayoutManager: LinearLayoutManager

    @Inject
    lateinit var completedMediaListAdapter: WatchlistMediaAdapter
    private lateinit var completedMediaLayoutManager: LinearLayoutManager

    @Inject
    lateinit var anticipatedMediaListAdapter: WatchlistMediaAdapter
    private lateinit var anticipatedMediaLayoutManager: LinearLayoutManager

    private val viewModel: WatchlistViewModel by viewModels()

    private lateinit var binding: FragmentWatchlistBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWatchlistBinding.inflate(inflater)
        binding.lifecycleOwner =
            viewLifecycleOwner // This removes observers when fragment is destroyed
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

    // todo detail UI should differ based on movie or show
    private fun showMovieDetails(media: MediaModelSealed) {
        findNavController().navigate(
            WatchlistFragmentDirections.actionWatchlistFragmentDestToDetailFragmentTMDB(
                movieBackdrop = media.backdropPath,
                moviePoster = media.posterPath,
                movieTitle = "TEMP",
                movieRating = media.rating,
                movieReleaseDate = "TEMP",
                movieOverview = media.overview,
                watchlistType = media.watchListType.name,
                movieId = media.id
            )
        )
    }

    private fun setupBinding() {
        recentlyAddedMediaListAdapter.onMediaClicked = { movie -> showMovieDetails(movie) }
        inProgressMediaListAdapter.onMediaClicked = { movie -> showMovieDetails(movie) }
        upComingMediaListAdapter.onMediaClicked = { movie -> showMovieDetails(movie) }
        completedMediaListAdapter.onMediaClicked = { movie -> showMovieDetails(movie) }
        anticipatedMediaListAdapter.onMediaClicked = { movie -> showMovieDetails(movie) }

        recentlyAddedMediaLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        inProgressMediaLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        upComingMediaLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        completedMediaLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        anticipatedMediaLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.rcRecentlyAdded.layoutManager = recentlyAddedMediaLayoutManager
        binding.rcRecentlyAdded.adapter = recentlyAddedMediaListAdapter

        binding.rcInProgress.layoutManager = inProgressMediaLayoutManager
        binding.rcInProgress.adapter = inProgressMediaListAdapter

        binding.rcUpcoming.layoutManager = upComingMediaLayoutManager
        binding.rcUpcoming.adapter = upComingMediaListAdapter

        binding.rcCompleted.layoutManager = completedMediaLayoutManager
        binding.rcCompleted.adapter = completedMediaListAdapter

        binding.rcAnticipated.layoutManager = anticipatedMediaLayoutManager
        binding.rcAnticipated.adapter = anticipatedMediaListAdapter
    }

    private fun updateList(
        adapter: WatchlistMediaAdapter,
        list: List<MediaModelSealed>
    ) {
        adapter.updateList(list)
    }

    private fun observeViewModel() {
        viewModel.watchListMedia.subscribe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> { }
                is Resource.Error -> { }
                is Resource.Success -> {
                    it.data.forEach { (key, value) ->
                        when (key) {
                            WatchListType.RECENTLY_ADDED -> {
                                updateList(recentlyAddedMediaListAdapter, value)
                            }
                            WatchListType.IN_PROGRESS -> {
                                updateList(inProgressMediaListAdapter, value)
                            }
                            WatchListType.UPCOMING -> {
                                updateList(upComingMediaListAdapter, value)
                            }
                            WatchListType.COMPLETED -> {
                                updateList(completedMediaListAdapter, value)
                            }
                            WatchListType.ANTICIPATED -> {
                                updateList(anticipatedMediaListAdapter, value)
                            }
                            WatchListType.NONE -> { }
                        }
                    }
                }
            }
        }
    }
}