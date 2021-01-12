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

package com.sunrisekcdeveloper.showtracker.features.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sunrisekcdeveloper.showtracker.databinding.FragmentSearchBinding
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie
import com.sunrisekcdeveloper.showtracker.commons.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.features.search.adapters.MediumPosterAdapter
import com.sunrisekcdeveloper.showtracker.commons.util.getQueryTextChangedStateFlow
import com.sunrisekcdeveloper.showtracker.commons.util.subscribe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Search Fragment that provides a search bar that filters through all movies and shows and presents
 * a list of results based on the user input
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {

    @Inject lateinit var adapter: MediumPosterAdapter

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by viewModels()

    private val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        setupBinding()
        observeViewModel()
        setupSearch()
        return binding.root
    }

    @FlowPreview
    private fun setupSearch() {
        CoroutineScope(coroutineContext).launch {
            binding.svFilterAll.getQueryTextChangedStateFlow()
                .debounce(300)
                .filter { query ->
                    return@filter query.isNotEmpty()
                }
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .collect { result ->
                    viewModel.search(result)
                    Timber.d("$result")
                }
        }
    }

    private fun setupBinding() {
        // Temporal Coupling
        adapter.addOnClickAction(object : ClickActionContract {
            override fun onClick(item: Movie) {
                Timber.d("Search Filter: $item")
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentDestToDetailFragment(
                        item.slug
                    )
                )
            }
        })
        binding.rcSearchResults.adapter = adapter
        binding.rcSearchResults.layoutManager = GridLayoutManager(
            requireContext(), 3, GridLayoutManager.VERTICAL, false
        )
    }
    private fun observeViewModel() {
        // TODO replace test data
        viewModel.movieListData.subscribe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}