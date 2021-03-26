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
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.getQueryTextChangedStateFlow
import com.sunrisekcdeveloper.showtracker.common.util.gone
import com.sunrisekcdeveloper.showtracker.common.util.observeInLifecycle
import com.sunrisekcdeveloper.showtracker.common.util.visible
import com.sunrisekcdeveloper.showtracker.databinding.FragmentWatchlistBinding
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.MovieWatchedStatus
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.SortMovies
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.SortShows
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
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
        viewModel.submitAction(ActionWatchlist.loadWatchlistData())
        setup()
        binding()
        observeViewModel()
    }

    private fun stateSuccess(
        movies: List<UIModelWatchlisMovie>,
        shows: List<UIModelWatchlistShow>
    ) {
        allShows = shows
        allMovies = movies
        watchlistMovieAdapter.submitList(movies)
        watchlistShowAdapter.submitList(shows)

        binding.tabBarWatchlist.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (it.position) {
                        0 -> {
                            binding.svWatchlist.queryHint = getString(R.string.search_shows_by_title)
                            binding.svWatchlist.setQuery("", true)
                            binding.recyclerviewWatchlist.adapter = watchlistShowAdapter
                        }
                        1 -> {
                            binding.svWatchlist.queryHint = getString(R.string.search_movie_by_title)
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

        binding.imgvFilterWatchlist.visible()
        binding.recyclerviewWatchlist.visible()
    }

    private fun stateLoading() {
        binding.layoutWatchlistSkeleton.visible()
    }

    private fun stateError() {
        viewModel.submitAction(ActionWatchlist.showToast("state Error"))
    }

    private fun cleanUi() {
        binding.layoutWatchlistSkeleton.gone()
        binding.imgvFilterWatchlist.gone()
        binding.recyclerviewWatchlist.gone()
    }

    private fun setup() {
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
                        ActionWatchlist.updateShowProgress(
                            UpdateShowAction.incrementEpisode(action.showId)
                        )
                    )
                }
                is ShowAdapterAction.MarkSeason -> {
                    viewModel.submitAction(
                        ActionWatchlist.updateShowProgress(
                            UpdateShowAction.completeSeason(action.showId)
                        )
                    )
                }
                is ShowAdapterAction.StartWatchingShow -> {
                    viewModel.submitAction(ActionWatchlist.startWatchingShow(action.showId))
                }
            }
        }

        // todo better onclick implementation needed
        watchlistMovieAdapter.onButtonClicked = OnMovieStatusClickListener { id, status ->
            when (status) {
                MovieWatchedStatus.Watched -> {
                    viewModel.submitAction(ActionWatchlist.markMovieAsUnwatched(id))
                }
                MovieWatchedStatus.NotWatched -> {
                    viewModel.submitAction(ActionWatchlist.markMovieAsWatched(id))
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
                                viewModel.updateShowSearchQuery(query)
                                watchlistShowAdapter.submitList(allShows)
                            }
                            else -> {
                                viewModel.updateMovieSearchQuery(query)
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
                            viewModel.updateShowSearchQuery(query)
                            val filtered = allShows.filter {
                                it.title.contains(query, true)
                            }
                            watchlistShowAdapter.submitList(filtered)
                        }
                        else -> {
                            viewModel.updateMovieSearchQuery(query)
                            val filtered = allMovies.filter {
                                it.title.contains(query, true)
                            }
                            watchlistMovieAdapter.submitList(filtered)
                        }
                    }
                }
        }

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
                            viewModel.updateShowSortBy(
                                when (showSortCheckedItem) {
                                    1 -> {
                                        SortShows.ByEpisodesLeftInSeason
                                    }
                                    2 -> {
                                        SortShows.ByRecentlyWatched
                                    }
                                    3 -> {
                                        SortShows.ByRecentlyWatched
                                    }
                                    4 -> {
                                        SortShows.ByNotStarted
                                    }
                                    else -> {
                                        SortShows.ByTitle
                                    }
                                }
                            )
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
                            viewModel.updateMovieSortBy(
                                when (movieSortCheckedItem) {
                                    1 -> {
                                        SortMovies.ByRecentlyAdded
                                    }
                                    2 -> {
                                        SortMovies.ByWatched
                                    }
                                    else -> {
                                        SortMovies.ByTitle
                                    }
                                }
                            )
                            dialog.dismiss()
                        }.show()
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