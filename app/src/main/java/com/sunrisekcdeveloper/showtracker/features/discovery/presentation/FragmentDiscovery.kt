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

package com.sunrisekcdeveloper.showtracker.features.discovery.presentation

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.forEach
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.sunrisekcdeveloper.models.navigation.InternalDeepLink
//import com.sunrisekcdeveloper.models.navigation.InternalDeepLink
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.TrackerDatabase
import com.sunrisekcdeveloper.showtracker.common.idk.ImageLoadingStandardGlide
import com.sunrisekcdeveloper.showtracker.common.util.KeyPersistenceStore
import com.sunrisekcdeveloper.showtracker.common.util.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.common.util.observeInLifecycle
import com.sunrisekcdeveloper.showtracker.databinding.FragmentDiscoveryBinding
import com.sunrisekcdeveloper.showtracker.features.detail.presentation.FragmentBottomSheetMovieDetail
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ActionDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.EventDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ListType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FragmentDiscovery : Fragment() {

    private lateinit var binding: FragmentDiscoveryBinding
    private val viewModel: ViewModelDiscovery by viewModels()

    private lateinit var adapterPopularMovies : PagingAdapterSimplePoster
    private lateinit var adapterPopularShows : PagingAdapterSimplePoster
    private lateinit var adapterTopRatedMovies : PagingAdapterSimplePoster
    private lateinit var adapterTopRatedShows : PagingAdapterSimplePoster
    private lateinit var adapterUpcomingMovies : PagingAdapterSimplePoster
    private lateinit var adapterAiringTodayShows : PagingAdapterSimplePoster

    private var job: Job? = null

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapterPopularMovies = PagingAdapterSimplePoster(ImageLoadingStandardGlide(this))
        adapterPopularShows = PagingAdapterSimplePoster(ImageLoadingStandardGlide(this))
        adapterTopRatedMovies = PagingAdapterSimplePoster(ImageLoadingStandardGlide(this))
        adapterTopRatedShows = PagingAdapterSimplePoster(ImageLoadingStandardGlide(this))
        adapterUpcomingMovies = PagingAdapterSimplePoster(ImageLoadingStandardGlide(this))
        adapterAiringTodayShows = PagingAdapterSimplePoster(ImageLoadingStandardGlide(this))
        binding = FragmentDiscoveryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setup()
        setupBinding()
        observeViewModel()
    }

    private suspend fun consumedSnackBarMessage(message: String) {
        dataStore.edit { settings ->
            settings[KeyPersistenceStore(getString(R.string.key_disc_prev_message)).asDataStoreKey()] = message
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
                viewModel.streamPopularShows.collectLatest {
                    adapterPopularShows.submitData(it)
                }
            }
        }.also {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.streamTopRatedMovies.collectLatest {
                    adapterTopRatedMovies.submitData(it)
                }
            }
        }.also {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.streamTopRatedShows.collectLatest {
                    adapterTopRatedShows.submitData(it)
                }
            }
        }.also {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.streamUpcomingMovies.collectLatest {
                    adapterUpcomingMovies.submitData(it)
                }
            }
        }.also {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.streamAiringTodayShows.collectLatest {
                    adapterAiringTodayShows.submitData(it)
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
                        if (it[KeyPersistenceStore(getString(R.string.key_disc_prev_message)).asDataStoreKey()] != event.message) {
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
                    FragmentDiscoveryDirections.navigateFromDiscoveryToBottomSheetFocused(1)
                }
                ListType.MovieTopRated -> {
                    FragmentDiscoveryDirections.navigateFromDiscoveryToBottomSheetFocused(3)
                }
                ListType.MovieUpcoming -> {
                    FragmentDiscoveryDirections.navigateFromDiscoveryToBottomSheetFocused(5)
                }
                ListType.ShowPopular -> {
                    FragmentDiscoveryDirections.navigateFromDiscoveryToBottomSheetFocused(2)
                }
                ListType.ShowTopRated -> {
                    FragmentDiscoveryDirections.navigateFromDiscoveryToBottomSheetFocused(4)
                }
                ListType.ShowAiringToday -> {
                    FragmentDiscoveryDirections.navigateFromDiscoveryToBottomSheetFocused(6)
                }
                ListType.NoList -> {
                    throw Exception("No list type associated with group")
                }
            }
        )
    }

    private fun setupBinding() {
        val onClick = OnPosterClickListener { mediaId, mediaTitle, posterPath, mediaType ->
            when (mediaType) {
                MediaType.Movie -> {
//                    <deepLink app:uri="showtracker://detail/movie_detail?id={id}&amp;movieTitle={movieTitle}&amp;posterPath={posterPath}"/>
//                    val intent = Uri.parse(
//                        "showtracker://detail/movie_detail?id=$mediaId&movieTitle=$mediaTitle&posterPath=$posterPath"
//                    )
//                    findNavController().navigate(intent)
//                    val intent = InternalDeepLink.moduleDetailMovie(
//                        id = mediaId,
//                        movieTittle = mediaTitle,
//                        posterPath = posterPath
//                    ).toUri()
//                    findNavController().navigate(intent)
//                    findNavController().navigate(
//                        FragmentDiscoveryDirections.navigateFromDiscoveryToBottomSheetDetailMovie(
//                            movieId = mediaId,
//                            movieTitle = mediaTitle,
//                            posterPath = posterPath
//                        )
//                    )

//                    Timber.d("args: ${arguments.movieId} and ${arguments.movieTitle} and ${arguments.posterPath} and $arguments")
                    // todo fak man, this damn bottomsheets
                    FragmentBottomSheetMovieDetail().apply {
                        val bundle = Bundle().apply {
                            putString("movieId", mediaId)
                            putString("movieTitle", mediaTitle)
                            putString("posterPath", posterPath)
                        }
                        arguments = bundle
                        show(childFragmentManager, "tagggg")
                    }

                }
                MediaType.Show -> {
                    findNavController().navigate(
                        FragmentDiscoveryDirections.navigateFromDiscoveryToBottomSheetDetailShow(
                            mediaId,
                            mediaTitle,
                            posterPath
                        )
                    )
                }
            }
        }

        adapterPopularMovies.setPosterClickAction(onClick)
        adapterPopularShows.setPosterClickAction(onClick)
        adapterTopRatedMovies.setPosterClickAction(onClick)
        adapterTopRatedShows.setPosterClickAction(onClick)
        adapterUpcomingMovies.setPosterClickAction(onClick)
        adapterAiringTodayShows.setPosterClickAction(onClick)

        binding.rcPopularMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcTopRatedMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcUpcomingMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcPopularShows.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcTopRatedShows.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcAiringTodayShows.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rcPopularMovies.adapter = adapterPopularMovies
        binding.rcTopRatedMovies.adapter = adapterTopRatedMovies
        binding.rcUpcomingMovies.adapter = adapterUpcomingMovies
        binding.rcPopularShows.adapter = adapterPopularShows
        binding.rcTopRatedShows.adapter = adapterTopRatedShows
        binding.rcAiringTodayShows.adapter = adapterAiringTodayShows

    }

    private fun setup() {
        binding.tvHeadingPopularMovies.click {
            viewModel.submitAction(ActionDiscovery.tapListHeading(ListType.moviePopular()))
        }
        binding.tvHeadingPopularShows.click {
            viewModel.submitAction(ActionDiscovery.tapListHeading(ListType.showPopular()))
        }
        binding.tvHeadingTopRatedMovies.click {
            viewModel.submitAction(ActionDiscovery.tapListHeading(ListType.movieTopRated()))
        }
        binding.tvHeadingTopRatedShows.click {
            viewModel.submitAction(ActionDiscovery.tapListHeading(ListType.showTopRated()))
        }
        binding.tvHeadingUpcomingMovies.click {
            viewModel.submitAction(ActionDiscovery.tapListHeading(ListType.movieUpcoming()))
        }
        binding.tvHeadingAiringTodayShows.click {
            viewModel.submitAction(ActionDiscovery.tapListHeading(ListType.showAiringToday()))
        }

        // Navigation - Toolbar Search icon
        binding.toolbarDiscovery.menu.forEach {
            it.setOnMenuItemClickListener {
                findNavController().navigate(
                    FragmentDiscoveryDirections.navigateFromDiscoveryToFragmentSearch()
                )
                true
            }
        }

        // Navigation - Tabs
        binding.tabsDiscovery.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    navigateToSelectedTab(it.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.let {
                    navigateToSelectedTab(it.position)
                }
            }
        })
    }

    private fun navigateToSelectedTab(position: Int) {
        // Movie Tab
        if (position == 0) {
            findNavController().navigate(
                FragmentDiscoveryDirections.navigateFromDiscoveryToDiscoveryMoviesFragment()
            )
            // TV Show Tab
        } else if (position == 1) {
            findNavController().navigate(
                FragmentDiscoveryDirections.navigateFromDiscoveryToDiscoveryShowsFragment()
            )
        }
    }
}