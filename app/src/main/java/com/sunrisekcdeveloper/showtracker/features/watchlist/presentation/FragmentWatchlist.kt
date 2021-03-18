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

package com.sunrisekcdeveloper.showtracker.features.watchlist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.WindowDecorActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.databinding.FragmentWatchlistBinding
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.MovieWatchedStatus
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FragmentWatchlist : Fragment() {

    private lateinit var binding: FragmentWatchlistBinding

    private val viewModel: ViewModelWatchlist by viewModels()

    @Inject
    lateinit var watchlistMovieAdapter: AdapterWatchlistMovie
    @Inject
    lateinit var watchlistShowAdapter: AdapterWatchlistShow

    private val arguments: FragmentWatchlistArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWatchlistBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setup()
        binding()
        observeViewModel()
    }

    private fun setup() {
        watchlistShowAdapter.onButtonClicked = OnShowStatusClickListener { action ->
            when (action) {
                is ShowAdapterAction.MarkEpisode -> {
                    viewModel.updateShowProgress(
                        UpdateShowAction.IncrementEpisode(action.showId)
                    )
                }
                is ShowAdapterAction.MarkSeason -> {
                    viewModel.updateShowProgress(
                        UpdateShowAction.CompleteSeason(action.showId)
                    )
                }
                is ShowAdapterAction.StartWatchingShow -> {
                    findNavController().navigate(
                        FragmentWatchlistDirections.navigateFromWatchlistToNavGraphProgress(action.showId)
                    )
                }
            }
        }

        // todo better onclick implementation needed
        watchlistMovieAdapter.onButtonClicked = OnMovieStatusClickListener { id, status ->
            when (status) {
                MovieWatchedStatus.Watched -> {
                    viewModel.markMovieAsUnWatched(id)
                }
                MovieWatchedStatus.NotWatched -> {
                    viewModel.markMovieAsWatched(id)
                }
            }
        }

        when (binding.tabBarWatchlist.selectedTabPosition) {
            0 -> {
                binding.recyclerviewWatchlist.adapter = watchlistShowAdapter
            }
            else -> {
                binding.recyclerviewWatchlist.adapter = watchlistMovieAdapter
            }
        }
        binding.tabBarWatchlist.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (it.position) {
                        0 -> {
                            binding.svWatchlist.queryHint = "Search TV shows by title"
                            binding.svWatchlist.setQuery("", true)
                            binding.recyclerviewWatchlist.adapter = watchlistShowAdapter
                        }
                        1 -> {
                            binding.svWatchlist.queryHint = "Search movies by title"
                            binding.svWatchlist.setQuery("", true)
                            binding.recyclerviewWatchlist.adapter = watchlistMovieAdapter
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun observeViewModel() {
        viewModel.watchlistMovies.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    watchlistMovieAdapter.refreshList(it.data)
                }
                is Resource.Error -> {}
                Resource.Loading -> {}
            }
        }
        viewModel.watchlistShows.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    watchlistShowAdapter.refreshData(it.data)
                    // from show detail scroll to position of show viewed in detail to update progress
                    arguments.showId?.let {
                        if (it != "none") {// default
                            Timber.e("showID: $it")
                            val position = watchlistShowAdapter.positionOfItem(it)
                            Timber.e("position: $position")
                            binding.recyclerviewWatchlist.scrollToPosition(position)
                        }
                    }
                }
                is Resource.Error -> {

                }
                Resource.Loading -> {

                }
            }
        }
    }

    private fun binding() {
        binding.recyclerviewWatchlist.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }
}