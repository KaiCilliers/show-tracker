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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.commons.util.asDomainMovie
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.databinding.FragmentWatchlistBinding
import com.sunrisekcdeveloper.showtracker.commons.util.subscribe
import com.sunrisekcdeveloper.showtracker.commons.models.local.RecentlyAddedMediaEntity
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.MediaModel
import com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.adapter.WatchlistMediaAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
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

    private fun showMovieDetails(media: MediaModel, watchListType: String) {
        findNavController().navigate(
            WatchlistFragmentDirections.actionWatchlistFragmentDestToDetailFragmentTMDB(
                movieBackdrop = media.backdropPath,
                moviePoster = media.posterPath,
                movieTitle = media.title,
                movieRating = media.rating,
                movieReleaseDate = media.releaseDate,
                movieOverview = media.overview,
                watchlistType = watchListType,
                movieId = media.id
            )
        )
    }

    private fun setupBinding() {
        recentlyAddedMediaListAdapter.onMediaClicked =
            { movie -> showMovieDetails(movie, "recently") }
        inProgressMediaListAdapter.onMediaClicked = { movie -> showMovieDetails(movie, "progress") }
        upComingMediaListAdapter.onMediaClicked = { movie -> showMovieDetails(movie, "upcoming") }
        completedMediaListAdapter.onMediaClicked = { movie -> showMovieDetails(movie, "complete") }
        anticipatedMediaListAdapter.onMediaClicked =
            { movie -> showMovieDetails(movie, "anticipated") }

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
        list: List<MediaModel>
    ) {
        adapter.updateList(list)
    }

    private fun hideListCategory(
        header: TextView,
        subHeader: TextView,
        list: RecyclerView
    ) {
        header.visibility = View.GONE
        subHeader.visibility = View.GONE
        list.visibility = View.GONE
    }

    private fun showListCategory(
        header: TextView,
        subHeader: TextView,
        list: RecyclerView
    ) {
        header.visibility = View.VISIBLE
        subHeader.visibility = View.VISIBLE
        list.visibility = View.VISIBLE
    }

    private fun observeViewModel() {
        viewModel.recentlyAddedMedia.subscribe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    showListCategory(
                        binding.tvHeadingRecentlyAdded,
                        binding.tvSubHeadingRecentlyAdded,
                        binding.rcRecentlyAdded
                    )
                    updateList(recentlyAddedMediaListAdapter, it.data.map { entity ->
                        entity.asDomainMovie()
                    })
                }
                is Resource.Error -> {
                    hideListCategory(
                        binding.tvHeadingRecentlyAdded,
                        binding.tvSubHeadingRecentlyAdded,
                        binding.rcRecentlyAdded
                    )
                }
            }
        }
        viewModel.inProgressMedia.subscribe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    showListCategory(
                        binding.tvHeadingInProgress,
                        binding.tvSubHeadingInProgress,
                        binding.rcInProgress
                    )
                    updateList(inProgressMediaListAdapter, it.data.map { entity ->
                        entity.asDomainMovie()
                    })
                }
                is Resource.Error -> {
                    hideListCategory(
                        binding.tvHeadingInProgress,
                        binding.tvSubHeadingInProgress,
                        binding.rcInProgress
                    )
                }
            }
        }
        viewModel.upcomingMedia.subscribe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    showListCategory(
                        binding.tvHeadingUpcoming,
                        binding.tvSubHeadingUpcoming,
                        binding.rcUpcoming
                    )
                    updateList(upComingMediaListAdapter, it.data.map { entity ->
                        entity.asDomainMovie()
                    })
                }
                is Resource.Error -> {
                    hideListCategory(
                        binding.tvHeadingUpcoming,
                        binding.tvSubHeadingUpcoming,
                        binding.rcUpcoming
                    )
                }
            }
        }
        viewModel.completedMedia.subscribe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    showListCategory(
                        binding.tvHeadingCompleted,
                        binding.tvSubHeadingCompleted,
                        binding.rcCompleted
                    )
                    updateList(completedMediaListAdapter, it.data.map { entity ->
                        entity.asDomainMovie()
                    })
                }
                is Resource.Error -> {
                    hideListCategory(
                        binding.tvHeadingCompleted,
                        binding.tvSubHeadingCompleted,
                        binding.rcCompleted
                    )
                }
            }
        }
        viewModel.anticipatedMedia.subscribe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    showListCategory(
                        binding.tvHeadingAnticipated,
                        binding.tvSubHeadingAnticipated,
                        binding.rcAnticipated
                    )
                    updateList(anticipatedMediaListAdapter, it.data.map { entity ->
                        entity.asDomainMovie()
                    })
                }
                is Resource.Error -> {
                    hideListCategory(
                        binding.tvHeadingAnticipated,
                        binding.tvSubHeadingAnticipated,
                        binding.rcAnticipated
                    )
                }
            }
        }
    }
}