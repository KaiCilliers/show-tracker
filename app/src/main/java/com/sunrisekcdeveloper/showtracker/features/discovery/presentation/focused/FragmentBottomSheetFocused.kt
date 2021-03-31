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
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sunrisekcdeveloper.showtracker.common.util.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.databinding.BottomSheetFocusedDiscoveryBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.FragmentDiscoveryDirections
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.ViewModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.search.presentation.PagingAdapterSimplePosterMedium
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FragmentBottomSheetFocused : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetFocusedDiscoveryBinding
    private val viewModel: ViewModelDiscovery by viewModels()

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
        val onClick = OnPosterClickListener { mediaId, mediaTitle, posterPath, mediaType ->
            when (mediaType) {
                MediaType.Movie -> {
                    findNavController().navigate(
                        FragmentDiscoveryDirections.navigateFromDiscoveryToBottomSheetDetailMovie(
                            movieId = mediaId,
                            movieTitle = mediaTitle,
                            posterPath = posterPath
                        )
                    )
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
        pagingAdapter.setPosterClickAction(onClick)
        binding.rcFocusedDiscovery.layoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        binding.rcFocusedDiscovery.adapter = pagingAdapter
    }

    private fun observeViewModel() {
        job = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.streamPopularMovies.collectLatest {
                Timber.e("gotit: $it")
                pagingAdapter.submitData(it)
            }
        }
    }
}