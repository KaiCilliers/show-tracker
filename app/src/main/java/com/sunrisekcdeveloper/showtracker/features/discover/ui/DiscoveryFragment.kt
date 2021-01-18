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

package com.sunrisekcdeveloper.showtracker.features.discover.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunrisekcdeveloper.showtracker.databinding.FragmentDiscoveryBinding
import com.sunrisekcdeveloper.showtracker.commons.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.models.roomresults.FeaturedList
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie
import com.sunrisekcdeveloper.showtracker.features.discover.adapters.SuggestionListAdapter
import com.sunrisekcdeveloper.showtracker.commons.util.subscribe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

/**
 * Home Fragment displays suggested movies and shows for the user to add to their watchlist,
 * categorised with appropriate headings
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DiscoveryFragment : Fragment() {

    @Inject
    lateinit var adapter: SuggestionListAdapter

    private lateinit var binding: FragmentDiscoveryBinding

    private val viewModel: DiscoveryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoveryBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        setupBinding()
        observeViewModel()
        return binding.root
    }

    private fun setupBinding() {
        // Temporal Coupling
        adapter.addOnClickAction(object : ClickActionContract {
            override fun onClick(item: Movie) {
                Timber.d("Featured: $item")
                findNavController().navigate(
                    DiscoveryFragmentDirections.actionDiscoverFragmentDestToDetailFragment(item.slug)
                )
            }
        })
        binding.rcFeaturedCategories.adapter = adapter
        binding.rcFeaturedCategories.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
    }

    private fun update(list: List<FeaturedList>) {
        adapter.submitList(list)
    }

    private fun observeViewModel() {
        // TODO i need a single subscription for the list of data
        //  the list building logic needs to happen in the viewmodel
        //  that might keep the list data in memory instead of
        //  fetching it from the db each time the fragment is resumed
        viewModel.featured.subscribe(viewLifecycleOwner) { map ->
            val done = arrayListOf<FeaturedList>()
            map.forEach {
                done.add(
                    FeaturedList(
                        heading = it.key,
                        results = it.value
                    )
                )
            }
            update(done)
        }
    }
}