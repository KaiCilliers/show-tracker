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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.common.util.getQueryTextChangedStateFlow
import com.sunrisekcdeveloper.showtracker.databinding.FragmentSearchUpdatedBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaTypeUpdated
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.OnPosterClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragmentUpdated : Fragment() {

    private lateinit var binding: FragmentSearchUpdatedBinding

    private val viewModel: SearchViewModel by viewModels()

    // todo rename adapter to SimplePosterAdapter
    @Inject
    lateinit var gridAdapter: GridListAdapter
    private lateinit var gridLayoutManager: GridLayoutManager

    @Inject
    lateinit var linearAdapter: TitlePosterListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchUpdatedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setup()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.searchState.observe(viewLifecycleOwner) {
            when (it) {
                SearchState.SuggestedContent -> {
                    showSuggestedState()
                }
                SearchState.NoSearchResults -> {
                    showNoResultsState()
                }
                is SearchState.SearchResults -> {
                    showResultState(it.data)
                }
            }
        }
    }

    private fun setup() {
        val onClick = OnPosterClickListener { mediaId, mediaType ->
            when (mediaType) {
                MediaTypeUpdated.Movie -> {
                    Timber.d("movie")
                    findNavController().navigate(
                        SearchFragmentUpdatedDirections.actionSearchFragmentUpdatedToDetailMovieBottomSheet2(
                            mediaId
                        )
                    )

                }
                MediaTypeUpdated.Show -> {
                    Timber.d("show")
                    findNavController().navigate(
                        SearchFragmentUpdatedDirections.actionSearchFragmentUpdatedToDetailShowBottomSheet2(
                            mediaId
                        )
                    )
                }
            }
        }

        gridAdapter.onPosterClickListener = onClick
        linearAdapter.onPosterClickListener = onClick

        gridLayoutManager = GridLayoutManager(
            requireContext(),
            3,
            GridLayoutManager.VERTICAL,
            false
        )
        linearLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        attachOnScrollListenerGrid(
            binding.recyclerviewSearch,
            gridLayoutManager
        ) { viewModel.getSearchResults("Harry", false) }// todo

        lifecycleScope.launchWhenResumed {
            binding.svSearch.getQueryTextChangedStateFlow()
                .debounce(600)
                .filter { query ->
                    // simply fetch all the data from database
                    if (query.isEmpty()) {
                        viewModel.setState(SearchState.SuggestedContent)
                        return@filter false
                    }
                    return@filter true
                }
                .distinctUntilChanged()
                //  here you will make call to database with query which returns a flow
//                .flatMapLatest {  }
                .collect { query ->
                    viewModel.getSearchResults(query, true)
                }
        }
    }

    private fun showSuggestedState() {
        hideNoResultsState()
        binding.tvHeaderWatchlistContent.visibility = View.VISIBLE
        binding.tvHeaderWatchlistContent.text = "Your unwatched movies and TV shows"
        showSuggestedList()
    }

    private fun showResultState(data: List<SearchUIModel>) {
        hideNoResultsState()
        binding.tvHeaderWatchlistContent.visibility = View.VISIBLE
        binding.tvHeaderWatchlistContent.text = "Results"
        showResultsList()
        gridAdapter.updateList(data)
    }

    private fun showSuggestedList() {
        binding.recyclerviewSearch.visibility = View.VISIBLE
        binding.recyclerviewSearch.adapter = linearAdapter
        binding.recyclerviewSearch.layoutManager = linearLayoutManager
    }

    private fun showResultsList() {
        binding.recyclerviewSearch.visibility = View.VISIBLE
        binding.recyclerviewSearch.adapter = gridAdapter
        binding.recyclerviewSearch.layoutManager = gridLayoutManager
    }

    private fun hideList() {
        binding.recyclerviewSearch.visibility = View.GONE
    }

    private fun showNoResultsState() {
        hideList()
        binding.tvHeaderNoResults.visibility = View.VISIBLE
        binding.tvSubHeaderNoResults.visibility = View.VISIBLE
        binding.tvHeaderWatchlistContent.visibility = View.GONE
    }

    private fun hideNoResultsState() {
        binding.tvHeaderNoResults.visibility = View.GONE
        binding.tvSubHeaderNoResults.visibility = View.GONE
    }

    private fun attachOnScrollListenerGrid(
        recyclerView: RecyclerView,
        layoutManager: GridLayoutManager,
        fetchNextPage: suspend () -> Unit
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // total number of movies inside adapter
                val totalItems = layoutManager.itemCount
                // current number of child views attached to the RecyclerView that are currently
                // being recycled
                val visibleItemCount = layoutManager.childCount
                // position of the leftmost visible item in the list
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                // true if the user scrolls past halfway plus a buffered value of visibleItemCount
                if (firstVisibleItem + visibleItemCount >= totalItems / 2) {
                    // This is to limit network calls
                    recyclerView.removeOnScrollListener(this)
                    MainScope().launch { fetchNextPage() }
                }
            }
        })
    }
}