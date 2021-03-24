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
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.getQueryTextChangedStateFlow
import com.sunrisekcdeveloper.showtracker.common.util.observeInLifecycle
import com.sunrisekcdeveloper.showtracker.databinding.FragmentWatchlistBinding
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.MovieWatchedStatus
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
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

    private var showSortCheckedItem = 0
    private var movieSortCheckedItem = 0

    private var allMovies: List<UIModelWatchlisMovie> = listOf()
    private var allShows: List<UIModelWatchlistShow> = listOf()

    private val sortOptionsShow = arrayOf(
        "Title",
        "Episodes left in season",
        "Recently Watched",
        "Recently Added",
        "Not Started"
    )

    private val sortOptionsMovie = arrayOf(
        "Title",
        "Recently Added",
        "Watched"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWatchlistBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.submitAction(ActionWatchlist.LoadWatchlistData)
//        setup()
        setupv2()
        binding()
        observeViewModel()
    }

    private fun stateSuccess(
        movies: List<UIModelWatchlisMovie>,
        shows: List<UIModelWatchlistShow>
    ) {
        watchlistMovieAdapter.submitList(movies)
        watchlistShowAdapter.submitList(shows)

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

        binding.imgvFilterWatchlist.isVisible = true
        binding.recyclerviewWatchlist.isVisible = true
    }

    private fun stateLoading() {
        viewModel.submitAction(ActionWatchlist.ShowToast("state Loading"))
    }

    private fun stateError() {
        viewModel.submitAction(ActionWatchlist.ShowToast("state Error"))
    }

    private fun cleanUi() {
        binding.imgvFilterWatchlist.isGone = true
        binding.recyclerviewWatchlist.isGone = true
    }

    private fun setupv2() {
        when (binding.tabBarWatchlist.selectedTabPosition) {
            0 -> {
                binding.recyclerviewWatchlist.adapter = watchlistShowAdapter
            }
            else -> {
                binding.recyclerviewWatchlist.adapter = watchlistMovieAdapter
            }
        }

        watchlistShowAdapter.onButtonClicked = OnShowStatusClickListener { action ->
            when (action) {
                is ShowAdapterAction.MarkEpisode -> {
                    viewModel.submitAction(
                        ActionWatchlist.UpdateShowProgress(
                            UpdateShowAction.IncrementEpisode(action.showId)
                        )
                    )
                }
                is ShowAdapterAction.MarkSeason -> {
                    viewModel.submitAction(
                        ActionWatchlist.UpdateShowProgress(
                            UpdateShowAction.CompleteSeason(action.showId)
                        )
                    )
                }
                is ShowAdapterAction.StartWatchingShow -> {
                    viewModel.submitAction(ActionWatchlist.StartWatchingShow(action.showId))
                }
            }
        }

        // todo better onclick implementation needed
        watchlistMovieAdapter.onButtonClicked = OnMovieStatusClickListener { id, status ->
            when (status) {
                MovieWatchedStatus.Watched -> {
                    viewModel.submitAction(ActionWatchlist.MarkMovieUnWatched(id))
                }
                MovieWatchedStatus.NotWatched -> {
                    viewModel.submitAction(ActionWatchlist.MarkMovieWatched(id))
                }
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

    private fun setup() {

        binding.imgvFilterWatchlist.setOnClickListener {
            when (binding.tabBarWatchlist.selectedTabPosition) {
                0 -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Sort TV Shows by:")
                        .setSingleChoiceItems(
                            sortOptionsShow,
                            showSortCheckedItem
                        ) { dialog, which ->
                            Timber.e("Sort chosen: ${sortOptionsShow[which]}")
                            showSortCheckedItem = which
                            when (showSortCheckedItem) {
                                1 -> {
//                                    viewModel.watchlistShows(SortShows.ByEpisodesLeftInSeason)
                                }
                                2 -> {
//                                    viewModel.watchlistShows(SortShows.ByRecentlyWatched)
                                }
                                3 -> {
//                                    viewModel.watchlistShows(SortShows.ByRecentlyAdded)
                                }
                                4 -> {
//                                    viewModel.watchlistShows(SortShows.ByNotStarted)
                                }
                                else -> {
//                                    viewModel.watchlistShows(SortShows.ByTitle)
                                }
                            }
                            dialog.dismiss()
                        }.show()
                }
                else -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Sort Movies by:")
                        .setSingleChoiceItems(
                            sortOptionsMovie,
                            movieSortCheckedItem
                        ) { dialog, which ->
                            Timber.e("Sort chosen: ${sortOptionsMovie[which]}")
                            movieSortCheckedItem = which
                            when (movieSortCheckedItem) {
                                1 -> {
                                    Timber.e("frag: recently added")
//                                    viewModel.watchlistMovies(SortMovies.ByRecentlyAdded)
                                }
                                2 -> {
                                    Timber.e("frag: watched")
//                                    viewModel.watchlistMovies(SortMovies.ByWatched)
                                }
                                else -> {
                                    Timber.e("frag: title")
//                                    viewModel.watchlistMovies(SortMovies.ByTitle)
                                }
                            }
                            dialog.dismiss()
                        }.show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            binding.svWatchlist.getQueryTextChangedStateFlow()
                .debounce(400)
                .filter { query ->
                    if (query.isEmpty()) {
                        when (binding.tabBarWatchlist.selectedTabPosition) {
                            0 -> {
                                watchlistShowAdapter.submitList(allShows)
                            }
                            else -> {
                                watchlistMovieAdapter.submitList(allMovies)
                            }
                        }
                        false
                    } else {
                        true
                    }
                }
                .distinctUntilChanged()
                .collectLatest { query ->
                    when (binding.tabBarWatchlist.selectedTabPosition) {
                        0 -> {
                            val filtered = allShows.filter {
                                it.title.contains(query, true)
                            }
                            watchlistShowAdapter.submitList(filtered)
                        }
                        else -> {
                            val filtered = allMovies.filter {
                                it.title.contains(query, true)
                            }
                            watchlistMovieAdapter.submitList(filtered)
                        }
                    }
                }
        }

    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            cleanUi()
            Timber.e("state: $state")
            when (state) {
                StateWatchlist.Loading -> {
                    stateLoading()
                }
                is StateWatchlist.Success -> {
                    stateSuccess(state.movies, state.shows)
                }
                is StateWatchlist.Error -> {
                    stateError()
                }
            }
        }
        viewModel.eventsFlow.onEach { event ->
            when (event) {
                is EventWatchlist.ShowToast -> {
                    Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                }
                is EventWatchlist.LoadMediaDetails -> {
                    findNavController().navigate(
                        when (event.type) {
                            MediaType.Movie -> FragmentWatchlistDirections.navigateFromWatchlistToBottomSheetDetailMovie(
                                event.mediaId,
                                event.title,
                                event.posterPath
                            )
                            MediaType.Show -> FragmentWatchlistDirections.navigateFromWatchlistToBottomSheetDetailShow(
                                event.mediaId,
                                event.title,
                                event.posterPath
                            )
                        }
                    )
                }
                is EventWatchlist.ConfigureShow -> {
                    findNavController().navigate(
                        FragmentWatchlistDirections.navigateFromWatchlistToNavGraphProgress(event.showId)
                    )
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }

    private fun binding() {
        val onClick = OnPosterClickListener { mediaId, mediaTitle, posterPath, mediaType ->
            viewModel.submitAction(
                ActionWatchlist.LoadMediaDetails(
                    mediaId, mediaTitle, posterPath, mediaType
                )
            )
        }

        watchlistShowAdapter.onPosterClickListener = onClick
        watchlistMovieAdapter.onPosterClickListener = onClick

        binding.recyclerviewWatchlist.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }
}