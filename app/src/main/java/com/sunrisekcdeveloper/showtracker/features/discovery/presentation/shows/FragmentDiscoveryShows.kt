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

package com.sunrisekcdeveloper.showtracker.features.discovery.presentation.shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.databinding.FragmentDiscoveryOnlyShowsBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.PagingAdapterSimplePoster
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FragmentDiscoveryShows : Fragment() {

    private lateinit var binding: FragmentDiscoveryOnlyShowsBinding

    @ExperimentalCoroutinesApi
    private val viewModel: ViewModelDiscoveryShows by viewModels()

    private val adapterPopularShows = PagingAdapterSimplePoster()
    private val adapterTopRatedShows = PagingAdapterSimplePoster()
    private val adapterAiringShows = PagingAdapterSimplePoster()

    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoveryOnlyShowsBinding.inflate(inflater)
        renderSpinner()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setup()
        setupBinding()
        observeViewModel()
    }

    private fun setupBinding() {
        val onClick = OnPosterClickListener { mediaId, mediaTitle, posterPath, mediaType ->
            if (mediaType == MediaType.Show) {
                findNavController().navigate(
                    FragmentDiscoveryShowsDirections.navigateFromDiscoveryShowsToBottomSheetDetailShow(
                        mediaId
                    )
                )
            }
        }

        adapterPopularShows.onClick = onClick
        adapterTopRatedShows.onClick = onClick
        adapterAiringShows.onClick = onClick

        binding.rcPopularShows.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcTopRatedShows.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcAiringTodayShows.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rcPopularShows.adapter = adapterPopularShows
        binding.rcTopRatedShows.adapter = adapterTopRatedShows
        binding.rcAiringTodayShows.adapter = adapterAiringShows
    }

    private fun observeViewModel() {
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.streamPopularShows.collectLatest {
                adapterPopularShows.submitData(it)
            }
        }.also {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.streamTopRatedShows.collectLatest {
                    adapterTopRatedShows.submitData(it)
                }
            }
        }.also {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.streamAiringTodayShows.collectLatest {
                    adapterAiringShows.submitData(it)
                }
            }
        }
    }

    private fun setup() {
        // Navigation - Toolbar Up button
        binding.toolbarDiscoveryShows.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // Navigation - Toolbar Search
        binding.toolbarDiscoveryShows.menu.forEach {
            it.setOnMenuItemClickListener {
                findNavController().navigate(
                    FragmentDiscoveryShowsDirections.navigateFromDiscoveryShowsToNavGraphSearch()
                )
                true
            }
        }

        // Navigation - Spinner
        binding.spinnerDiscoveryShows.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Discovery Screen
                    if (id == 1L) {
                        findNavController().popBackStack()
                        // Discovery Movies Screen
                    } else if (id == 2L) {
                        findNavController().navigate(
                            FragmentDiscoveryShowsDirections.navigateFromDiscoveryShowsToDiscoveryMoviesFragment()
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun renderSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.show_dropdown_array,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerDiscoveryShows.adapter = it
        }
    }
}