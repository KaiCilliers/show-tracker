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

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import android.view.InputQueue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.getQueryTextChangedStateFlow
import com.sunrisekcdeveloper.showtracker.common.util.observeInLifecycle
import com.sunrisekcdeveloper.showtracker.databinding.FragmentSearchBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ListType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.PagingAdapterSimplePoster
import com.sunrisekcdeveloper.showtracker.features.search.domain.domain.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class FragmentSearch : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: ViewModelSearch by viewModels()

    private val adapterSearchResults = PagingAdapterSimplePoster()
    @Inject
    lateinit var adapterUnwatchedContent: AdapterSimplePosterTitle

    lateinit var linearLayoutManager: LinearLayoutManager

    lateinit var gridLayoutManager: GridLayoutManager

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
            viewModel.submitAction(ActionSearch.SearchForMedia(it))
        }
        setup()
        observeViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.svSearch.query.trim().toString())
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            // todo this is bad, this causes UI flashing :(
            cleanUI()
            when (state) {
                is StateSearch.EmptySearch -> {
                    viewModel.submitAction(ActionSearch.ShowToast("Here is your unwatched shit"))
                    stateEmpty(state.data)
                }
                StateSearch.NoResultsFound -> {
                    viewModel.submitAction(ActionSearch.ShowToast("No results found"))
                    stateNoResults()
                }
                StateSearch.Loading -> {
                    viewModel.submitAction(ActionSearch.ShowToast("search Loading"))
                    stateLoading()
                }
                is StateSearch.Success -> {
                    viewModel.submitAction(ActionSearch.ShowToast("Success"))
                    viewLifecycleOwner.lifecycleScope.launch {
                        stateSuccess(state.data)
                    }
                }
                is StateSearch.Error -> {
                    viewModel.submitAction(ActionSearch.ShowToast("Eorrror"))
                    stateError()
                }
            }
        }
        viewModel.eventsFlow.onEach { event ->
            when (event) {
                is EventSearch.LoadMediaDetails -> {
                    findNavController().navigate(
                        when (event.type) {
                            MediaType.Movie -> { FragmentSearchDirections.navigateFromSearchToBottomSheetDetailMovie(event.mediaId, event.title, event.posterPath) }
                            MediaType.Show -> { FragmentSearchDirections.navigateFromSearchToBottomSheetDetailShow(event.mediaId, event.title, event.posterPath) }
                        }
                    )
                }
                is EventSearch.ShowToast -> {
                    Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                }
                EventSearch.PopBackStack -> {
                    findNavController().popBackStack()
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }

    private fun stateError() {

    }
    private fun stateLoading() {

    }
    private suspend fun stateSuccess(page: PagingData<UIModelDiscovery>) {
        binding.recyclerviewSearch.layoutManager = gridLayoutManager
        binding.recyclerviewSearch.adapter = adapterSearchResults
        binding.recyclerviewSearch.isVisible = true
        binding.tvHeaderWatchlistContent.isVisible = true

        adapterSearchResults.submitData(page)
        binding.tvHeaderWatchlistContent.text = "Results"

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
                        viewModel.submitAction(ActionSearch.NotifyNoSearchResults)
                    }
                }
        }
    }
    // todo handle case where unwatched movies and shows are empty
    //  perhaps show some image that says user should add stuff to their watchlist (along with same header as below)
    private fun stateEmpty(list: List<UIModelUnwatchedSearch>) {
        binding.tvHeaderWatchlistContent.text = "Your Unwatched Movies and TV Shows"
        binding.tvHeaderWatchlistContent.isVisible = true

        binding.recyclerviewSearch.layoutManager = linearLayoutManager
        binding.recyclerviewSearch.adapter = adapterUnwatchedContent
        adapterUnwatchedContent.updateList(list)
        binding.recyclerviewSearch.isVisible = true

    }
    private fun stateNoResults() {
        binding.tvHeaderNoResults.isVisible = true
        binding.tvSubHeaderNoResults.isVisible = true
    }
    private fun cleanUI() {
        binding.recyclerviewSearch.isGone = true
        binding.tvHeaderNoResults.isGone = true
        binding.tvSubHeaderNoResults.isGone = true
        binding.tvHeaderWatchlistContent.isGone = true
    }


    private fun setup() {
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
            viewModel.submitAction(ActionSearch.LoadMediaDetails(
                mediaId, mediaTitle, posterPath, mediaType
                )
            )
        }

        adapterSearchResults.onClick = onClick

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            binding.svSearch.getQueryTextChangedStateFlow()
//                .debounce(100)
                .filter { query ->
                    if (query.isEmpty()) {
                        adapterSearchResults.submitData(PagingData.empty())
                        viewModel.submitAction(ActionSearch.LoadUnwatchedContent)
                        return@filter false
                    }
                    return@filter true
                }
                .distinctUntilChanged()
                .collect { query ->
                    viewModel.submitAction(ActionSearch.SearchForMedia(query))
                }
        }
        binding.toolbarSearch.setNavigationOnClickListener { viewModel.submitAction(ActionSearch.BackButtonPress) } // todo up button returns to discovery
    }
    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
    }
}