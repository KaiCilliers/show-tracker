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

package com.sunrisekcdeveloper.showtracker.features.discovery.presentation.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.forEach
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.idk.ImageLoadingStandardGlide
import com.sunrisekcdeveloper.showtracker.common.util.KeyPersistenceStore
import com.sunrisekcdeveloper.showtracker.common.util.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.common.util.observeInLifecycle
import com.sunrisekcdeveloper.showtracker.databinding.FragmentDiscoveryOnlyMoviesBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ActionDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.EventDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ListType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.PagingAdapterSimplePoster
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentDiscoveryMovies : Fragment() {

    private lateinit var binding: FragmentDiscoveryOnlyMoviesBinding
    private val viewModel: ViewModelDiscoveryMovies by viewModels()

    private lateinit var adapterPopularMovies : PagingAdapterSimplePoster
    private lateinit var adapterTopRatedMovies : PagingAdapterSimplePoster
    private lateinit var adaptedUpcomingMovies : PagingAdapterSimplePoster

    private var job: Job? = null

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapterPopularMovies = PagingAdapterSimplePoster(ImageLoadingStandardGlide(this))
        adapterTopRatedMovies = PagingAdapterSimplePoster(ImageLoadingStandardGlide(this))
        adaptedUpcomingMovies = PagingAdapterSimplePoster(ImageLoadingStandardGlide(this))
        binding = FragmentDiscoveryOnlyMoviesBinding.inflate(inflater)
        renderSpinner()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setup()
        setupBinding()
        observeViewModel()
    }

    private suspend fun consumedSnackBarMessage(message: String) {
        dataStore.edit { settings ->
            settings[KeyPersistenceStore(getString(R.string.key_disc_movie_prev_message)).asDataStoreKey()] = message
        }
    }

    private fun setup() {

        binding.tvHeadingPopularMovies.click {
            viewModel.submitAction(ActionDiscovery.tapListHeading(ListType.moviePopular()))
        }
        binding.tvHeadingTopRatedMovies.click {
            viewModel.submitAction(ActionDiscovery.tapListHeading(ListType.movieTopRated()))
        }
        binding.tvHeadingUpcomingMovies.click {
            viewModel.submitAction(ActionDiscovery.tapListHeading(ListType.movieUpcoming()))
        }

        // Navigation - Toolbar Up button
        binding.toolbarDiscoveryMovies.setNavigationOnClickListener { findNavController().popBackStack() }

        // Navigation - Toolbar Search
        binding.toolbarDiscoveryMovies.menu.forEach {
            it.setOnMenuItemClickListener {
                findNavController().navigate(
                    FragmentDiscoveryMoviesDirections.navigateFromDiscoveryMoviesToFragmentSearch()
                )
                true
            }
        }

        // Navigation - Spinner
        binding.spinnerDiscoveryMovies.onItemSelectedListener =
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
                        // TV Show Discovery Screen
                    } else if (id == 2L) {
                        findNavController().navigate(
                            FragmentDiscoveryMoviesDirections.navigateFromDiscoveryMoviesToDiscoveryShowsFragment()
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun renderSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.items_movie_filter,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerDiscoveryMovies.adapter = it
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            findNavController().currentBackStackEntry?.savedStateHandle?.apply {
                getLiveData<String>(KeyPersistenceStore(getString(R.string.key_disc_snack_bar)).value()).asFlow()
                    .collect {
                        delay(300)
                        viewModel.submitAction(ActionDiscovery.showSnackBar(it))
                    }
            }
        }
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.streamPopularMovies.collectLatest {
                adapterPopularMovies.submitData(it)
            }
        }.also {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.streamTopRatedMovies.collectLatest {
                    adapterTopRatedMovies.submitData(it)
                }
            }
        }.also {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.streamUpcomingMovies.collectLatest {
                    adaptedUpcomingMovies.submitData(it)
                }
            }
        }
        viewModel.eventsFlow.onEach { event ->
            when (event) {
                is EventDiscovery.ShowFocusedContent -> {
                    navigateToFocusedContent(event.listType)
                }
                is EventDiscovery.ShowSnackBar -> {
                    dataStore.data.take(1).collect {
                        if (it[KeyPersistenceStore(getString(R.string.key_disc_movie_prev_message)).asDataStoreKey()] != event.message) {
                            showSnackBar(event.message)
                            consumedSnackBarMessage(event.message)
                        }
                    }
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun navigateToFocusedContent(listType: ListType) {
        findNavController().navigate(
            when (listType) {
                ListType.MoviePopular -> {
                    FragmentDiscoveryMoviesDirections.navigateFromDiscoveryMoviesToBottomSheetFocused(1)
                }
                ListType.MovieTopRated -> {
                    FragmentDiscoveryMoviesDirections.navigateFromDiscoveryMoviesToBottomSheetFocused(3)
                }
                ListType.MovieUpcoming -> {
                    FragmentDiscoveryMoviesDirections.navigateFromDiscoveryMoviesToBottomSheetFocused(5)
                }
                ListType.ShowPopular -> {
                    FragmentDiscoveryMoviesDirections.navigateFromDiscoveryMoviesToBottomSheetFocused(2)
                }
                ListType.ShowTopRated -> {
                    FragmentDiscoveryMoviesDirections.navigateFromDiscoveryMoviesToBottomSheetFocused(4)
                }
                ListType.ShowAiringToday -> {
                    FragmentDiscoveryMoviesDirections.navigateFromDiscoveryMoviesToBottomSheetFocused(6)
                }
                ListType.NoList -> {
                    throw Exception("No list type associated with group")
                }
            }
        )
    }

    private fun setupBinding() {
        val onClick = OnPosterClickListener { mediaId, mediaTitle, posterPath, mediaType ->
            if (mediaType == MediaType.Movie) {
                findNavController().navigate(
                    FragmentDiscoveryMoviesDirections.navigateFromDiscoveryMoviesToBottomSheetDetailMovie(
                        mediaId, mediaTitle, posterPath
                    )
                )
            }
        }

        adapterPopularMovies.setPosterClickAction(onClick)
        adapterTopRatedMovies.setPosterClickAction(onClick)
        adaptedUpcomingMovies.setPosterClickAction(onClick)

        binding.rcPopularMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcTopRatedMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcUpcomingMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rcPopularMovies.adapter = adapterPopularMovies
        binding.rcTopRatedMovies.adapter = adapterTopRatedMovies
        binding.rcUpcomingMovies.adapter = adaptedUpcomingMovies
    }
}