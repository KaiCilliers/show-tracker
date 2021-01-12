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

package com.sunrisekcdeveloper.showtracker.features.watchlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunrisekcdeveloper.showtracker.databinding.FragmentWatchlistBinding
import com.sunrisekcdeveloper.showtracker.commons.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie
import com.sunrisekcdeveloper.showtracker.features.watchlist.adapters.MovieSummaryAdapter
import com.sunrisekcdeveloper.showtracker.features.watchlist.adapters.SmallPosterAdapter
import com.sunrisekcdeveloper.showtracker.commons.util.subscribe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Progress Fragment that displays upcoming movies and shows with the capability to filter
 * based on movie or show
 */
@AndroidEntryPoint
class WatchlistFragment : Fragment(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main


    fun foo() {
        launch {

        }
    }

    @Inject lateinit var adapter: SmallPosterAdapter

    @Inject lateinit var upComingAdapter: MovieSummaryAdapter

    private val viewModel: WatchlistViewModel by viewModels()

    private lateinit var binding: FragmentWatchlistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWatchlistBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner // This removes observers when fragment is destroyed
        setupBinding()
        observeViewModel()
        return binding.root
    }

    private fun setupBinding() {
        // Temporal Coupling
        adapter.addOnClickAction(object : ClickActionContract {
            override fun onClick(item: Movie) {
                Timber.d("TITLE: $item")
                findNavController().navigate(
                    WatchlistFragmentDirections.actionWatchlistFragmentDestToDetailFragment(
                        "FROM PROGRESS FRAGMENT"
                    )
                )
            }
        })
        binding.rcFilterOptions.adapter = adapter
        binding.rcFilterOptions.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        // Temporal Coupling
        upComingAdapter.addOnClickAction(object : ClickActionContract {
            override fun onClick(item: Movie) {
                Timber.d("SPECIAL click: $item")
            }
        })
        binding.rcUpcoming.adapter = upComingAdapter
        binding.rcUpcoming.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewModel.movieListData.subscribe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.suggestionListData.subscribe(viewLifecycleOwner) {
            upComingAdapter.submitList(it)
        }
    }
}