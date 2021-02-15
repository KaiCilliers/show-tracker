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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunrisekcdeveloper.showtracker.databinding.FragmentWatchlistBinding
import com.sunrisekcdeveloper.showtracker.commons.util.subscribe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

//    @Inject lateinit var adapter: DiscoverListAdapter

    private val viewModel: WatchlistViewModel by viewModels()

    private lateinit var binding: FragmentWatchlistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWatchlistBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner // This removes observers when fragment is destroyed
        setupBinding()
        setupFilters()
        observeViewModel()
        return binding.root
    }

    private fun setupFilters() {
        binding.chipFilterMovie.setOnCheckedChangeListener { _, checked ->
            Toast.makeText(requireContext(), "Movie: $checked", Toast.LENGTH_SHORT).show()
        }
        binding.chipFilterShows.setOnCheckedChangeListener { _, checked ->
            Toast.makeText(requireContext(), "Show: $checked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBinding() {
        // Temporal Coupling
//        adapter.addOnClickAction(object : ClickActionContract {
//            override fun onClick(item: Movie) {
//                Timber.d("TITLE: $item")
//                findNavController().navigate(
//                    WatchlistFragmentDirections.actionWatchlistFragmentDestToDetailFragment(
//                        "FROM PROGRESS FRAGMENT"
//                    )
//                )
//            }
//        })
//        binding.rcFeaturedCategoriesWatchlist.adapter = adapter
        binding.rcFeaturedCategoriesWatchlist.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun observeViewModel() {
        viewModel.featured.subscribe(viewLifecycleOwner) {
//            adapter.submitList(it)
        }
    }
}