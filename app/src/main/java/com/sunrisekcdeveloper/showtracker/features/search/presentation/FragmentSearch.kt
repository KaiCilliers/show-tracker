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
import android.view.InputQueue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.getQueryTextChangedStateFlow
import com.sunrisekcdeveloper.showtracker.databinding.FragmentSearchBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ListType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.PagingAdapterSimplePoster
import com.sunrisekcdeveloper.showtracker.features.search.domain.domain.UIModelSearch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class FragmentSearch : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: ViewModelSearch by viewModels()

    private val adapterSearchResults = PagingAdapterSimplePoster()

    var searchJob: Job? = null

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
            search(it)
        }
        setup()
        binding.toolbarSearch.setNavigationOnClickListener { findNavController().popBackStack() } // todo up button returns to discovery
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.svSearch.query.trim().toString())
    }

    private fun search(query: String) {
        searchJob?.cancel()

        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchMedia(query).collectLatest {
                adapterSearchResults.submitData(it)
            }
        }
    }

    private fun setup() {
        val onClick = OnPosterClickListener { mediaId, mediaTitle, posterPath, mediaType ->
            when (mediaType) {
                MediaType.Movie -> {
                    findNavController().navigate(
                        FragmentSearchDirections.navigateFromSearchToBottomSheetDetailMovie(mediaId)
                    )

                }
                MediaType.Show -> {
                    findNavController().navigate(
                        FragmentSearchDirections.navigateFromSearchToBottomSheetDetailShow(mediaId)
                    )
                }
            }
        }

        adapterSearchResults.onClick = onClick

        binding.recyclerviewSearch.layoutManager = GridLayoutManager(
            requireContext(),
            3,
            GridLayoutManager.VERTICAL,
            false
        )

        binding.recyclerviewSearch.adapter = adapterSearchResults

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            binding.svSearch.getQueryTextChangedStateFlow()
                .debounce(300)
                .filter { query ->
                    if (query.isEmpty()) {
                        adapterSearchResults.submitData(PagingData.empty())
                        return@filter false
                    }
                    return@filter true
                }
                .distinctUntilChanged()
                .collect { query ->
                    search(query)
                }
        }

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
                .collect { binding.recyclerviewSearch.scrollToPosition(0) }
        }
    }
    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
    }
}