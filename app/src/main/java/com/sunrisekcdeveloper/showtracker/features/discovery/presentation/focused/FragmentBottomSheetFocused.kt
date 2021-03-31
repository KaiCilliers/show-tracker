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

package com.sunrisekcdeveloper.showtracker.features.discovery.presentation.focused

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.util.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.common.util.observeInLifecycle
import com.sunrisekcdeveloper.showtracker.databinding.BottomSheetFocusedDiscoveryBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.ViewModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.search.presentation.PagingAdapterSimplePosterMedium
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentBottomSheetFocused : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetFocusedDiscoveryBinding
    private val viewModel: ViewModelDiscovery by viewModels()
    private val arguments: FragmentBottomSheetFocusedArgs by navArgs()

    private val pagingAdapter = PagingAdapterSimplePosterMedium()
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetFocusedDiscoveryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observeViewModel()
    }

    private fun setup() {
        binding.tvHeading.click {
            viewModel.submitAction(ActionFocused.tapHeading())
        }

        val onClick = OnPosterClickListener { mediaId, mediaTitle, posterPath, mediaType ->
            when (mediaType) {
                MediaType.Movie -> {
                    findNavController().navigate(
                        FragmentBottomSheetFocusedDirections.navigateFromFocusedToBottomSheetDetailMovie(
                            movieId = mediaId,
                            movieTitle = mediaTitle,
                            posterPath = posterPath
                        )
                    )
                }
                MediaType.Show -> {
                    findNavController().navigate(
                        FragmentBottomSheetFocusedDirections.navigateFromFocusedToBottomSheetDetailShow(
                            showId = mediaId,
                            showTitle = mediaTitle,
                            posterPath = posterPath
                        )
                    )
                }
            }
        }
        pagingAdapter.setPosterClickAction(onClick)
        binding.rcFocusedDiscovery.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        binding.rcFocusedDiscovery.adapter = pagingAdapter
    }

    private fun observeViewModel() {
        job = viewLifecycleOwner.lifecycleScope.launch {
            when (arguments.listType) {
                1 -> {
                    binding.tvHeading.text = getString(R.string.heading_popular_movies)
                    viewModel.streamPopularMovies.collectLatest {
                        pagingAdapter.submitData(it)
                    }
                }
                2 -> {
                    binding.tvHeading.text = getString(R.string.heading_popular_shows)
                    viewModel.streamPopularShows.collectLatest {
                        pagingAdapter.submitData(it)
                    }
                }
                3 -> {
                    viewModel.streamTopRatedMovies.collectLatest {
                        binding.tvHeading.text = getString(R.string.heading_top_rated_movies)
                        pagingAdapter.submitData(it)
                    }
                }
                4 -> {
                    viewModel.streamTopRatedShows.collectLatest {
                        binding.tvHeading.text = getString(R.string.heading_top_rated_shows)
                        pagingAdapter.submitData(it)
                    }
                }
                5 -> {
                    viewModel.streamUpcomingMovies.collectLatest {
                        binding.tvHeading.text = getString(R.string.heading_upcoming_movies)
                        pagingAdapter.submitData(it)
                    }
                }
                6 -> {
                    viewModel.streamAiringTodayShows.collectLatest {
                        binding.tvHeading.text = getString(R.string.heading_airing_today_shows)
                        pagingAdapter.submitData(it)
                    }
                }
            }
        }
        viewModel.eventsFlowFocused.onEach { event ->
            when (event) {
                EventFocused.Close -> {
                    // todo this property needs to be set somewhere else
                    (requireDialog() as BottomSheetDialog).dismissWithAnimation = true
                    dismiss()
                }
                EventFocused.ScrollToTop -> {
                    binding.rcFocusedDiscovery.smoothScrollToPosition(0)
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }
}