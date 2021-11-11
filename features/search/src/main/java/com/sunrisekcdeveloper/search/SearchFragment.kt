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

package com.sunrisekcdeveloper.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sunrisekcdeveloper.cache.MediaType
import com.sunrisekcdeveloper.common.viewBinding
import com.sunrisekcdeveloper.network.InternalDeepLink
import com.sunrisekcdeveloper.search.*
import com.sunrisekcdeveloper.search.databinding.FragmentSearchBinding
import com.sunrisekcdeveloper.search.extras.*
import com.sunrisekcdeveloper.search.extras.model.ActionSearch
import com.sunrisekcdeveloper.search.extras.model.EventSearch
import com.sunrisekcdeveloper.search.extras.model.StateSearch
import com.sunrisekcdeveloper.search.extras.model.UIModelPoster
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel: SearchViewModel by viewModels()

    private lateinit var adapterSearchResults: PagingAdapterSimplePosterMedium
    lateinit var adapterUnwatchedContent: AdapterSimplePosterTitle
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var gridLayoutManager: GridLayoutManager

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        setup()
        observeSearchInput()
        observeSnackBarEvents()
        observeResultsState()
        observeViewModel()
    }

    private fun init() {
        viewModel.submitAction(
            if (requireContext().hasConnection()) ActionSearch.deviceIsOnline()
            else ActionSearch.deviceIsOffline()
        )
    }

    private fun setup() {
        adapterSearchResults = PagingAdapterSimplePosterMedium(ImageLoadingStandardGlide(this))
        adapterUnwatchedContent =
            AdapterSimplePosterTitle(ImageLoadingStandardGlide(this)) { _, _, _, _ -> }

        adapterSearchResults.setPosterClickAction { mediaId, mediaTitle, posterPath, mediaType ->
            viewModel.submitAction(
                ActionSearch.loadMediaDetails(
                    mediaId, mediaTitle, posterPath, mediaType
                )
            )
        }

        linearLayoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        gridLayoutManager = GridLayoutManager(
            requireContext(),
            3,
            GridLayoutManager.VERTICAL,
            false
        )

        binding.toolbarSearch.setNavigationOnClickListener { viewModel.submitAction(ActionSearch.backButtonPressed()) }

        // programmatically setting SearchView attributes due to it not working in layout for some reason
        binding.svSearch.setIconifiedByDefault(false)
        binding.svSearch.queryHint = getString(R.string.search_movie_show_hint)
    }

    private fun observeSearchInput() = viewLifecycleOwner.lifecycleScope.launchWhenResumed {
        binding.svSearch.getQueryTextChangedStateFlow()
            .debounce(300)
            .filter { query ->
                if (query.isEmpty()) {
                    adapterSearchResults.submitData(PagingData.empty())
                    viewModel.submitAction(ActionSearch.loadUnwatchedContent())
                    return@filter false
                }
                return@filter true
            }
            .distinctUntilChanged()
            .collect { query ->
                viewModel.submitAction(ActionSearch.searchForMedia(query))
            }
    }

    private fun observeSnackBarEvents() = viewLifecycleOwner.lifecycleScope.launchWhenResumed {
        findNavController().currentBackStackEntry?.savedStateHandle?.apply {
            getLiveData<String>(KeyPersistenceStore(getString(R.string.key_disc_snack_bar)).value()).asFlow()
                .collect {
                    delay(300)
                    viewModel.submitAction(ActionSearch.showSnackBar(it))
                }
        }
    }

    private fun observeResultsState() = viewLifecycleOwner.lifecycleScope.launchWhenResumed {
        adapterSearchResults.loadStateFlow
            .distinctUntilChangedBy { it.refresh }
            .filter { it.refresh is LoadState.NotLoading }
            .collect {
                binding.recyclerviewSearch.scrollToPosition(0)
                if (adapterSearchResults.itemCount == 0) {
                    viewModel.submitAction(ActionSearch.notifyNoSearchResults())
                }
            }
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            cleanUI()
            when (state) {
                is StateSearch.EmptySearch -> {
                    renderEmpty(state.data)
                }
                StateSearch.NoResultsFound -> {
                    renderNoResults()
                }
                StateSearch.Loading -> {
                    renderLoading()
                }
                is StateSearch.Success -> {
                    renderSuccess(state.data)
                }
                is StateSearch.Error -> {
                    renderError()
                }
                StateSearch.EmptyWatchlist -> {
                    renderEmptyWatchlist()
                }
            }
        }
        viewModel.eventsFlow.onEach { event ->
            when (event) {
                is EventSearch.LoadMediaDetails -> {
                    binding.svSearch.hideKeyboard(requireContext(), binding.root)
                    val intent = when(event.type) {
                        MediaType.Movie -> {
                            InternalDeepLink.moduleDetailMovie(
                                id = event.mediaId,
                                movieTitle = event.title,
                                posterPath = event.posterPath
                            ).toUri()
                        }
                        MediaType.Show -> {
                            InternalDeepLink.moduleDetailShow(
                                id = event.mediaId,
                                showTitle = event.title,
                                posterPath = event.posterPath
                            ).toUri()
                        }
                    }
                    findNavController().navigate(intent)
                }
                is EventSearch.ShowToast -> {
                    Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                }
                EventSearch.PopBackStack -> {
                    findNavController().popBackStack()
                }
                is EventSearch.ShowSnackBar -> {
                    dataStore.data.take(1).collect {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                        consumedSnackBarMessage(event.message)
                    }
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }

    private fun renderError() {
        binding.imageView.visible()
    }

    // TODO: 06-04-2021 loading state search
    private fun renderLoading() {

    }

    private fun renderSuccess(page: PagingData<UIModelPoster>) =
        viewLifecycleOwner.lifecycleScope.launch {
            binding.tvHeaderWatchlistContent.text = getString(R.string.results)
            binding.recyclerviewSearch.layoutManager = gridLayoutManager
            binding.recyclerviewSearch.adapter = adapterSearchResults
            binding.recyclerviewSearch.visible()
            binding.tvHeaderWatchlistContent.visible()

            adapterSearchResults.submitData(page)
        }

    // todo handle case where unwatched movies and shows are empty
    //  perhaps show some image that says user should add stuff to their watchlist (along with same header as below)
    private fun renderEmpty(list: List<UIModelPoster>) {
        binding.tvHeaderWatchlistContent.text = getString(R.string.unwatched_shows_movies)
        binding.tvHeaderWatchlistContent.visible()

        val onClick = OnPosterClickListener { mediaId, mediaTitle, posterPath, mediaType ->
            viewModel.submitAction(
                ActionSearch.loadMediaDetails(
                    mediaId, mediaTitle, posterPath, mediaType
                )
            )
        }

        adapterUnwatchedContent.setPosterClickAction(onClick)

        binding.recyclerviewSearch.layoutManager = linearLayoutManager
        binding.recyclerviewSearch.adapter = adapterUnwatchedContent
        adapterUnwatchedContent.submitList(list)
        binding.recyclerviewSearch.visible()

    }

    private fun renderEmptyWatchlist() {
        binding.tvHeaderWatchlistContent.text = getString(R.string.unwatched_shows_movies)
        binding.tvHeaderWatchlistContent.visible()
        binding.layoutEmpty.visible()
    }

    private fun renderNoResults() {
        binding.tvHeaderNoResults.visible()
        binding.tvSubHeaderNoResults.visible()
    }

    private fun cleanUI() {
        binding.layoutEmpty.gone()
        binding.imageView.gone()
        binding.recyclerviewSearch.gone()
        binding.tvHeaderNoResults.gone()
        binding.tvSubHeaderNoResults.gone()
        binding.tvHeaderWatchlistContent.gone()
    }

    private suspend fun consumedSnackBarMessage(message: String) {
        dataStore.edit { settings ->
            settings[KeyPersistenceStore(getString(R.string.key_search_prev_message)).asDataStoreKey()] =
                message
        }
    }
}