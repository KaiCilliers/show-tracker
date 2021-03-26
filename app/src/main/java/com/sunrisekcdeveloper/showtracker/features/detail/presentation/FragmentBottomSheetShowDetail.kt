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

package com.sunrisekcdeveloper.showtracker.features.detail.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.EndpointPoster
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.common.util.observeInLifecycle
import com.sunrisekcdeveloper.showtracker.common.util.setMaxLinesToEllipsize
import com.sunrisekcdeveloper.showtracker.databinding.BottomSheetShowDetailBinding
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.ActionDetailShow
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.EventDetailShow
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.StateDetailShow
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelShowDetail
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.shows.FragmentDiscoveryShowsDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class FragmentBottomSheetShowDetail : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetShowDetailBinding
    private val arguments: FragmentBottomSheetShowDetailArgs by navArgs()
    private val viewModel: ViewModelShowDetail by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetShowDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.submitAction(ActionDetailShow.Load(arguments.showId))
        setup()
        observe()
    }

    private fun observe() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            cleanUI()
            when (state) {
                StateDetailShow.Loading -> { stateLoading() }
                is StateDetailShow.Success -> { stateSuccess(state.data) }
                is StateDetailShow.Error -> { stateError() }
            }
        }
        viewModel.eventsFlow.onEach { event ->
            when (event) {
                is EventDetailShow.LaunchStartWatching -> {
                    findNavController().navigate(
                        FragmentBottomSheetShowDetailDirections
                            .navigateFromDetailShowToNavGraphProgress(event.showId)
                    )
                }
                is EventDetailShow.GoToShowInWatchlist -> {
                    findNavController().navigate(
                        FragmentBottomSheetShowDetailDirections
                            .navigateFromDetailShowToWatchlistFragment(event.showId)
                    )
                }
                EventDetailShow.Close -> { dismissAllowingStateLoss() }
                is EventDetailShow.ShowToast -> {
                    Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }

    private fun setup() {
        // Navigation - Close fragment
        binding.imgDetailShowClose.setOnClickListener { viewModel.submitAction(ActionDetailShow.Close) }
        bindPriorityData()
    }

    private fun bindPriorityData() {
        Glide.with(requireContext())
            .load(EndpointPoster.Standard.urlWithResource(arguments.posterPath))
            .transform(CenterCrop())
            .into(binding.imgDetailShowPoster)

        binding.tvDetailShowTitle.text = arguments.showTitle
    }

    private fun cleanUI() {
        binding.layoutDetailShowSkeleton.isGone = true
        binding.tvDetailShowDescription.isGone = true
        binding.tvDetailShowFirstAirDate.isGone = true
        binding.tvDetailShowCertification.isGone = true
        binding.tvDetailShowSeasons.isGone = true
        binding.tvShowSeparatorOne.isGone = true
        binding.tvShowSeparatorTwo.isGone = true
        binding.btnDetailShowAdd.isEnabled = false
        binding.btnDetailShowWatchStatus.isEnabled = false
    }
    private fun stateSuccess(data: UIModelShowDetail) {
        binding.tvDetailShowDescription.apply {
            text = data.overview
            setMaxLinesToEllipsize()
        }
        binding.tvDetailShowFirstAirDate.text = data.firstAirDate
        binding.tvDetailShowCertification.text = data.certification
        binding.tvDetailShowSeasons.text = getString(
            R.string.season_with_number, data.seasonsTotal.toString()
        )

        // todo this is a mess FIX up to date shows not showing
        if (data.watchlisted && !data.deleted) {
            binding.btnDetailShowAdd.text = "Remove"
            binding.btnDetailShowAdd.click {
                viewModel.submitAction(ActionDetailShow.Remove(data.id))
            }

            if (!data.startedWatching) {
                binding.btnDetailShowWatchStatus.text = "Start Watching"
                binding.btnDetailShowWatchStatus.click {
                    viewModel.submitAction(ActionDetailShow.StartWatching(data.id))
                }
            }

            if (data.upToDate && !data.deleted) {
                binding.btnDetailShowWatchStatus.text = "Up to date"
                binding.btnDetailShowWatchStatus.click {
                    Toast.makeText(requireContext(), "Show is up to date", Toast.LENGTH_SHORT).show()
                }
            }

            if ((data.startedWatching) && (!data.upToDate) && !data.deleted) {
                binding.btnDetailShowWatchStatus.text = "Update progress"
                binding.btnDetailShowWatchStatus.click {
                    viewModel.submitAction(ActionDetailShow.UpdateProgress(data.id))
                }
            } else {
                binding.btnDetailShowWatchStatus.text = "Start Watching"
                binding.btnDetailShowWatchStatus.click {
                    // todo note duplicated 3 time - not cool man
                    viewModel.submitAction(ActionDetailShow.StartWatching(data.id))
                }
            }
        } else {
            binding.btnDetailShowAdd.text = "+ Add"
            binding.btnDetailShowAdd.click {
                viewModel.submitAction(ActionDetailShow.Add(data.id))
            }
            binding.btnDetailShowWatchStatus.text = "Start Watching"
            binding.btnDetailShowWatchStatus.click {
                viewModel.submitAction(ActionDetailShow.StartWatching(data.id))
            }
        }
        binding.tvDetailShowDescription.isVisible = true
        binding.tvDetailShowFirstAirDate.isVisible = true
        binding.tvDetailShowCertification.isVisible = true
        binding.tvDetailShowSeasons.isVisible = true
        binding.tvShowSeparatorOne.isVisible = true
        binding.tvShowSeparatorTwo.isVisible = true
        binding.btnDetailShowAdd.isEnabled = true
        binding.btnDetailShowWatchStatus.isEnabled = true
    }
    private fun stateLoading() {
        binding.layoutDetailShowSkeleton.isVisible = true
    }
    private fun stateError() {
        Toast.makeText(requireContext(), "show error", Toast.LENGTH_SHORT).show()
    }
}