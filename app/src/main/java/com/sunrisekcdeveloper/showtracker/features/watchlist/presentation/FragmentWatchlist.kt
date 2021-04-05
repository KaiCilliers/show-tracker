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

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.idk.ImageLoadingStandardGlide
import com.sunrisekcdeveloper.showtracker.common.util.*
import com.sunrisekcdeveloper.showtracker.databinding.FragmentWatchlistBinding
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.MovieWatchedStatus
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.FilterMovies
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.FilterShows
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FragmentWatchlist : Fragment() {

    private lateinit var binding: FragmentWatchlistBinding
    private val viewModel: ViewModelWatchlist by viewModels()
    private val arguments: FragmentWatchlistArgs by navArgs()

    private lateinit var watchlistMovieAdapter : AdapterWatchlistMovie
    private lateinit var watchlistShowAdapter : AdapterWatchlistShow

    private var scrolledOnce = false
    private var showSortCheckedItem = 0
    private var movieSortCheckedItem = 0

    private var allMovies: List<UIModelWatchlisMovie> = listOf()
    private var allShows: List<UIModelWatchlistShow> = listOf()

    // todo extract array
    private val sortOptionsShow = arrayOf(
        "No Filters",
        "Added Today",
        "Watched Today",
        "Started",
        "Not Started"
    )
    private val filterOptionMovie = arrayOf(
        "No Filters",
        "Watched",
        "Not Watched",
        "Added Today"
    )

    @Inject
    lateinit var dataStore: DataStore<Preferences>

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
        watchlistMovieAdapter = AdapterWatchlistMovie(ImageLoadingStandardGlide(this))
        watchlistShowAdapter = AdapterWatchlistShow(ImageLoadingStandardGlide(this))
        viewModel.submitAction(ActionWatchlist.loadWatchlistData())
        setup()
        binding()
        observeViewModel()
    }

    private suspend fun consumedSnackBarMessage(message: String) {
        dataStore.edit { settings ->
            settings[KeyPersistenceStore(getString(R.string.key_watchlist_previous_message)).asDataStoreKey()] = message
        }
    }

    private fun stateEmptyList() {
        binding.layoutEmpty.visible()
        binding.imgEmptyList.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_list_24))
        binding.emptyListHeading.text = getString(R.string.empty_list)
        binding.emptyListSubHeading.text = getString(R.string.empty_list_sub_heading)
        binding.imgvFilterWatchlist.gone()
    }

    private fun stateSuccess(
        movies: List<UIModelWatchlisMovie>,
        shows: List<UIModelWatchlistShow>
    ) {
        Timber.d("999999999 success")

        allShows = shows
        allMovies = movies
        watchlistMovieAdapter.submitList(movies)
        watchlistShowAdapter.submitList(shows)

        binding.tabBarWatchlist.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (it.position) {
                        0 -> {
                            binding.svWatchlist.queryHint =
                                getString(R.string.search_shows_by_title)
                            binding.recyclerviewWatchlist.adapter = watchlistShowAdapter
                            Timber.e("#### shows: ${allShows.size}, filterOption: ${viewModel.showFilterOption}, query: \"${binding.svWatchlist.query}\"")
                            if (allShows.isEmpty() && viewModel.showFilterOption is FilterShows.NoFilters && binding.svWatchlist.query.isNullOrEmpty()) {
                                viewModel.submitAction(ActionWatchlist.showEmptyState())
                            } else if (allShows.isEmpty() && viewModel.showFilterOption !is FilterShows.NoFilters && binding.svWatchlist.query.isNotEmpty()) {
                                viewModel.submitAction(ActionWatchlist.noFilterResults())
                            } else {
                                // this is so hacky
                                cleanUi()
                                stateSuccess(allMovies, allShows)
                            }
                        }
                        1 -> {
                            binding.svWatchlist.queryHint =
                                getString(R.string.search_movie_by_title)
                            binding.recyclerviewWatchlist.adapter = watchlistMovieAdapter
                            if (allMovies.isEmpty() && (viewModel.movieFilterOption is FilterMovies.NoFilters || binding.svWatchlist.query.isNullOrEmpty())) {
                                viewModel.submitAction(ActionWatchlist.showEmptyState())
                            } else if (allMovies.isEmpty() && (viewModel.movieFilterOption !is FilterMovies.NoFilters || binding.svWatchlist.query.isNotEmpty())) {
                                viewModel.submitAction(ActionWatchlist.noFilterResults())
                            } else {
                                // hacky - bypasses route of Action >>> State / Event
                                cleanUi()
                                stateSuccess(allMovies, allShows)
                            }
                        }
                        else -> { viewModel.submitAction(ActionWatchlist.showEmptyState()) }
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
        if (arguments.showId != "none") {
            if (!scrolledOnce) scrollToShow()
        }

        Timber.d("999999999 shows: ${allShows.size}, movies: ${allMovies.size}, showFilter: ${viewModel.showFilterOption.javaClass.simpleName}, movieFilter: ${viewModel.movieFilterOption.javaClass.simpleName}, Query: \"${binding.svWatchlist.query}\"")
        // initial empty list state (ugly yes :() or when removing last item from list
        if (binding.tabBarWatchlist.selectedTabPosition == 0) {

            if (allShows.isEmpty() && viewModel.showFilterOption is FilterShows.NoFilters && binding.svWatchlist.query.isNullOrEmpty()) {
                viewModel.submitAction(ActionWatchlist.showEmptyState())
            } else if (allShows.isEmpty() && (viewModel.showFilterOption !is FilterShows.NoFilters || binding.svWatchlist.query.isNotEmpty())) {
                viewModel.submitAction(ActionWatchlist.noFilterResults())
            }

        } else {

            if (allMovies.isEmpty() && viewModel.movieFilterOption is FilterMovies.NoFilters && binding.svWatchlist.query.isNullOrEmpty()) {
                viewModel.submitAction(ActionWatchlist.showEmptyState())
            } else if (allMovies.isEmpty() && (viewModel.movieFilterOption !is FilterMovies.NoFilters || binding.svWatchlist.query.isNotEmpty())) {
                viewModel.submitAction(ActionWatchlist.noFilterResults())
            }

        }
    }

    private fun scrollToShow() = viewLifecycleOwner.lifecycleScope.launch {
        delay(1000)
        scrolledOnce = true
        val position = watchlistShowAdapter.positionOfItem(arguments.showId)
        binding.recyclerviewWatchlist.getChildAt(position)?.let {
            val y = binding.recyclerviewWatchlist.y + binding.recyclerviewWatchlist.getChildAt(
                position
            ).y
            binding.scrollWatchlist.smoothScrollTo(0, y.toInt())
        }
    }

    private fun stateLoading() {
        binding.layoutWatchlistSkeleton.visible()
    }

    private fun stateError() {
        viewModel.submitAction(ActionWatchlist.showToast("state Error"))
    }

    private fun stateNoFilterResults() {
        binding.layoutEmpty.visible()
        binding.imgvFilterWatchlist.visible()
        binding.imgEmptyList.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_outlet_24))
        binding.emptyListHeading.text = getString(R.string.no_matches)
        binding.emptyListSubHeading.text = getString(R.string.no_matches_sub_heading)
    }

    private fun cleanUi() {
        binding.layoutEmpty.gone()
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
                    viewModel.submitAction(
                        ActionWatchlist.startWatchingShow(
                            action.showId,
                            action.title
                        )
                    )
                }
            }
        }

        // todo better onclick implementation needed
        watchlistMovieAdapter.onButtonClicked = OnMovieStatusClickListener { id, title, status ->
            when (status) {
                MovieWatchedStatus.Watched -> {
                    viewModel.submitAction(ActionWatchlist.attemptUnwatch(id, title))
                }
                MovieWatchedStatus.NotWatched -> {
                    viewModel.submitAction(ActionWatchlist.markMovieAsWatched(id))
                    viewModel.submitAction(ActionWatchlist.showSnackbar("You've watched \"$title\"!"))
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
                                // ugh, this is so not good
                                viewModel._state.value = StateWatchlist.success(allMovies, allShows)
                            }
                            else -> {
                                viewModel.updateMovieSearchQuery(query)
                                viewModel._state.value = StateWatchlist.success(allMovies, allShows)
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
                            viewModel._state.value = StateWatchlist.success(allMovies, filtered)
                        }
                        else -> {
                            viewModel.updateMovieSearchQuery(query)
                            val filtered = allMovies.filter {
                                it.title.contains(query, true)
                            }
                            viewModel._state.value = StateWatchlist.success(filtered, allShows)
                        }
                    }
                }
        }

        binding.imgvFilterWatchlist.setOnClickListener {
            when (binding.tabBarWatchlist.selectedTabPosition) {
                0 -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Filter TV Shows by:")
                        .setSingleChoiceItems(
                            sortOptionsShow,
                            showSortCheckedItem
                        ) { dialog, which ->
                            Timber.e("Sort chosen: ${sortOptionsShow[which]}")
                            showSortCheckedItem = which
                            viewModel.updateShowSortBy(
                                when (showSortCheckedItem) {
                                    1 -> {
                                        FilterShows.AddedToday
                                    }
                                    2 -> {
                                        FilterShows.WatchedToday
                                    }
                                    3 -> {
                                        FilterShows.Started
                                    }
                                    4 -> {
                                        FilterShows.NotStarted
                                    }
                                    else -> {
                                        FilterShows.NoFilters
                                    }
                                }
                            )
                            dialog.dismiss()
                        }.show()
                }
                else -> {
                    MaterialAlertDialogBuilder(requireContext())
                        // todo extract string
                        .setTitle("Filter Movies by:")
                        .setSingleChoiceItems(
                            filterOptionMovie,
                            movieSortCheckedItem
                        ) { dialog, which ->
                            Timber.e("Sort chosen: ${filterOptionMovie[which]}")
                            movieSortCheckedItem = which
                            viewModel.updateMovieSortBy(
                                when (movieSortCheckedItem) {
                                    1 -> {
                                        FilterMovies.Watched
                                    }
                                    2 -> {
                                        FilterMovies.Unwatched
                                    }
                                    3 -> {
                                        FilterMovies.AddedToday
                                    }
                                    else -> {
                                        FilterMovies.NoFilters
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
        viewLifecycleOwner.lifecycleScope.launch {
            findNavController().currentBackStackEntry?.savedStateHandle?.apply {
                getLiveData<String>(KeyPersistenceStore(getString(R.string.key_disc_snack_bar)).value()).asFlow()
                    .collect {
                        delay(300)
                        viewModel.submitAction(ActionWatchlist.showSnackbar(it))
                    }
            }
        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            cleanUi()
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
                StateWatchlist.EmptyList -> {
                    stateEmptyList()
                }
                StateWatchlist.NoFilterResults -> {
                    stateNoFilterResults()
                }
            }
        }
        viewModel.eventsFlow.onEach { event ->
            when (event) {
                is EventWatchlist.ShowToast -> {
                    Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                }
                is EventWatchlist.LoadMediaDetails -> {
                    hideKeyboard(binding.svWatchlist, requireContext(), binding.root)
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
                                event.posterPath,
                                fromWatchlist = true
                            )
                        }
                    )
                }
                is EventWatchlist.ConfigureShow -> {
                    findNavController().navigate(
                        FragmentWatchlistDirections.navigateFromWatchlistFragmentToSetProgressFragment(
                            event.showId, event.title
                        )
                    )
                }
                is EventWatchlist.ShowSnackbar -> {
                    dataStore.data.take(1).collect {
                        if (it[KeyPersistenceStore(getString(R.string.key_watchlist_previous_message)).asDataStoreKey()] != event.msg) {
                            showSnackBar(event.msg)
                            consumedSnackBarMessage(event.msg)
                        }
                    }
                }
                is EventWatchlist.ShowConfirmationDialog -> {
                    showConfirmationDialogUnwatch(event.movieId, event.title)
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showConfirmationDialogUnwatch(movieId: String, title: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Unwatch Movie?")
            .setMessage("This will set \"$title\" as an unwatched movie in your watchlist")
            .setNegativeButton("Cancel") { _, _ -> }
            .setPositiveButton("Unwatch") { _, _ ->
                viewModel.submitAction(ActionWatchlist.markMovieAsUnwatched(movieId))
                viewModel.submitAction(ActionWatchlist.showSnackbar("\"$title\" set as unwatched"))
            }
            .show()
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