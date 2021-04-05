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

package com.sunrisekcdeveloper.showtracker.features.search.presentation

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.util.*
import com.sunrisekcdeveloper.showtracker.databinding.FragmentSearchBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ActionDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.movies.FragmentDiscoveryMovies
import com.sunrisekcdeveloper.showtracker.features.search.domain.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class FragmentSearch : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: ViewModelSearch by viewModels()

    private val adapterSearchResults = PagingAdapterSimplePosterMedium()
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
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        savedInstanceState?.getString(LAST_SEARCH_QUERY)?.let {
            binding.svSearch.setQuery(it, false)
            viewModel.submitAction(ActionSearch.searchForMedia(it))
        }
        setup()
        observeViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.svSearch.query.trim().toString())
    }

    private suspend fun consumedSnackBarMessage(message: String) {
        dataStore.edit { settings ->
            settings[KeyPersistenceStore(getString(R.string.key_search_prev_message)).asDataStoreKey()] = message
        }
    }

    private fun isConnected() {

        try {
            val cm =
                requireContext().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)

            capabilities?.let {
                if (
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                ) {
                    viewModel.submitAction(ActionSearch.deviceIsOnline())
                } else {
                    viewModel.submitAction(ActionSearch.deviceIsOffline())
                }
            }
        } catch (exception: Exception) {
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            findNavController().currentBackStackEntry?.savedStateHandle?.apply {
                getLiveData<String>(KeyPersistenceStore(getString(R.string.key_disc_snack_bar)).value()).asFlow()
                    .collect {
                        delay(300)
                        viewModel.submitAction(ActionSearch.showSnackBar(it))
                    }
            }
        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            // todo this is bad, this causes UI flashing :(
            cleanUI()
            when (state) {
                is StateSearch.EmptySearch -> {
                    stateEmpty(state.data)
                }
                StateSearch.NoResultsFound -> {
                    stateNoResults()
                }
                StateSearch.Loading -> {
                    stateLoading()
                }
                is StateSearch.Success -> {
                    viewLifecycleOwner.lifecycleScope.launch {
                        stateSuccess(state.data)
                    }
                }
                is StateSearch.Error -> {
                    stateError()
                }
                StateSearch.EmptyWatchlist -> {
                    stateEmptyWatchlist()
                }
            }
        }
        viewModel.eventsFlow.onEach { event ->
            when (event) {
                is EventSearch.LoadMediaDetails -> {
                    hideKeyboard(binding.svSearch, requireContext(), binding.root)
                    findNavController().navigate(
                        when (event.type) {
                            MediaType.Movie -> {
                                FragmentSearchDirections.navigateFromSearchToBottomSheetDetailMovie(
                                    event.mediaId,
                                    event.title,
                                    event.posterPath
                                )
                            }
                            MediaType.Show -> {
                                FragmentSearchDirections.navigateFromSearchToBottomSheetDetailShow(
                                    event.mediaId,
                                    event.title,
                                    event.posterPath
                                )
                            }
                        }
                    )
                }
                is EventSearch.ShowToast -> {
                    Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                }
                EventSearch.PopBackStack -> {
                    findNavController().popBackStack()
                }
                is EventSearch.ShowSnackBar -> {
                    dataStore.data.take(1).collect {
                        showSnackBar(event.message)
                        consumedSnackBarMessage(event.message)
                    }
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun stateError() {
        binding.imageView.visible()
    }

    private fun stateLoading() {

    }

    private suspend fun stateSuccess(page: PagingData<UIModelDiscovery>) {
        binding.tvHeaderWatchlistContent.text = getString(R.string.results)
        binding.recyclerviewSearch.layoutManager = gridLayoutManager
        binding.recyclerviewSearch.adapter = adapterSearchResults
        binding.recyclerviewSearch.visible()
        binding.tvHeaderWatchlistContent.visible()

        adapterSearchResults.submitData(page)

        // Here we launch a coroutine which is responsible for scrolling the list to the top
        // when the list is refreshed from the network
        viewLifecycleOwner.lifecycleScope.launch {

            // PagingAdapter exposes a Flow`which emits changes in the adapter's loads state
            // via a CombinedLoadState object
            adapterSearchResults.loadStateFlow
                // Only emit when the REFRESH LoadState changes
                .distinctUntilChangedBy { it.refresh }
                // Only emit when REFRESH completes i.e., NotLoading
                .filter { it.refresh is LoadState.NotLoading }
                // Scroll to the top of the list
                .collect {
                    binding.recyclerviewSearch.scrollToPosition(0)
                    if (adapterSearchResults.itemCount == 0) {
                        viewModel.submitAction(ActionSearch.notifyNoSearchResults())
                    }
                }
        }
    }

    // todo handle case where unwatched movies and shows are empty
    //  perhaps show some image that says user should add stuff to their watchlist (along with same header as below)
    private fun stateEmpty(list: List<UIModelUnwatchedSearch>) {
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

    private fun stateEmptyWatchlist() {
        binding.tvHeaderWatchlistContent.text = getString(R.string.unwatched_shows_movies)
        binding.tvHeaderWatchlistContent.visible()
        binding.layoutEmpty.visible()
    }

    private fun stateNoResults() {
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


    private fun setup() {
        isConnected()
        // todo consider injecting Glide to get the fragment context, thus glide attaching to its lifecycle
        adapterUnwatchedContent = AdapterSimplePosterTitle(Glide.with(this)) { _, _, _, _ -> }
        linearLayoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        gridLayoutManager = GridLayoutManager(
            requireContext(),
            3,
            GridLayoutManager.VERTICAL,
            false
        )
        // todo programatically set searchview attributes due to it not working in layout for some reason
        binding.svSearch.setIconifiedByDefault(false)
        binding.svSearch.queryHint = getString(R.string.search_movie_show_hint)

        val onClick = OnPosterClickListener { mediaId, mediaTitle, posterPath, mediaType ->
            viewModel.submitAction(
                ActionSearch.loadMediaDetails(
                    mediaId, mediaTitle, posterPath, mediaType
                )
            )
        }

        adapterSearchResults.setPosterClickAction(onClick)

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
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
        binding.toolbarSearch.setNavigationOnClickListener { viewModel.submitAction(ActionSearch.backButtonPressed()) }
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
    }
}