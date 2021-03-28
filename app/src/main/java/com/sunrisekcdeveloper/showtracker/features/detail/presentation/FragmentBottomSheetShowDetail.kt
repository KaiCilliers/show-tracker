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

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.EndpointPoster
import com.sunrisekcdeveloper.showtracker.common.util.*
import com.sunrisekcdeveloper.showtracker.databinding.BottomSheetShowDetailBinding
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.ActionDetailShow
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.EventDetailShow
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.StateDetailShow
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelShowDetail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

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
        viewModel.submitAction(ActionDetailShow.load(arguments.showId))
        setup()
        observe()
    }

    private fun observe() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            cleanUI()
            when (state) {
                StateDetailShow.Loading -> {
                    stateLoading()
                }
                is StateDetailShow.Success -> {
                    stateSuccess(state.data)
                }
                is StateDetailShow.Error -> {
                    stateError()
                }
            }
        }
        viewModel.eventsFlow.onEach { event ->
            when (event) {
                is EventDetailShow.LaunchStartWatching -> {
                    findNavController().navigate(
                        FragmentBottomSheetShowDetailDirections
                            .navigateFromDetailShowToSetProgressFragment(event.showId)
                    )
                }
                is EventDetailShow.GoToShowInWatchlist -> {
                    findNavController().navigate(
                        FragmentBottomSheetShowDetailDirections
                            .navigateFromDetailShowToWatchlistFragment(event.showId)
                    )
                }
                EventDetailShow.Close -> {
                    // todo this property needs to be set somewhere else
                    (requireDialog() as BottomSheetDialog).dismissWithAnimation = true
                    dismissAllowingStateLoss()
                }
                is EventDetailShow.ShowToast -> {
                    Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }

    private fun setup() {
        binding.imgDetailShowClose.setOnClickListener { viewModel.submitAction(ActionDetailShow.close()) }
        bindPriorityData()
    }

    private fun bindPriorityData() {
        Glide.with(this)
            .load(EndpointPoster.Standard.urlWithResource(arguments.posterPath))
            .centerCrop()
            .error(R.drawable.error_poster)
            .transition(DrawableTransitionOptions.withCrossFade(100))
            .into(binding.imgDetailShowPoster)

        binding.tvDetailShowTitle.text = arguments.showTitle
    }

    private fun cleanUI() {
        binding.layoutDetailShowSkeleton.gone()
        binding.tvDetailShowDescription.gone()
        binding.tvDetailShowFirstAirDate.gone()
        binding.tvDetailShowCertification.gone()
        binding.tvDetailShowSeasons.gone()
        binding.tvShowSeparatorOne.gone()
        binding.tvShowSeparatorTwo.gone()
        binding.btnDetailShowAdd.disabled()
        binding.btnDetailShowWatchStatus.disabled()
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

        if (data.deleted || !data.watchlisted) {
            stateNotOnWatchlist(data)
        } else {
            stateAddedToWatchlist(data)
            if (!data.startedWatching) {
                stateNotStartedWatching(data)
            } else if (data.upToDate) {
                stateUpToDate()
            } else {
                stateInProgress(data)
            }
        }

        binding.tvDetailShowDescription.visible()
        binding.tvDetailShowFirstAirDate.visible()
        binding.tvDetailShowCertification.visible()
        binding.tvDetailShowSeasons.visible()
        binding.tvShowSeparatorOne.visible()
        binding.tvShowSeparatorTwo.visible()
        binding.btnDetailShowAdd.enabled()
        binding.btnDetailShowWatchStatus.enabled()
    }

    private fun stateNotOnWatchlist(data: UIModelShowDetail) {
        binding.btnDetailShowAdd.setBackgroundColor(fetchPrimaryColor(requireContext()))
        binding.btnDetailShowAdd.text = getString(R.string.show_add)
        binding.btnDetailShowAdd.click { viewModel.submitAction(ActionDetailShow.add(data.id)) }
        stateNotStartedWatching(data)
    }

    private fun stateNotStartedWatching(data: UIModelShowDetail) {
        binding.btnDetailShowWatchStatus.text = getString(R.string.show_start_watching)
        binding.btnDetailShowWatchStatus.click {
            viewModel.submitAction(ActionDetailShow.startWatching(data.id))
        }
    }

    private fun stateUpToDate() {
        binding.btnDetailShowWatchStatus.text = getString(R.string.show_up_to_date)
        binding.btnDetailShowWatchStatus.click {
            viewModel.submitAction(ActionDetailShow.showToast("Show is up to date"))
        }
    }

    private fun stateInProgress(data: UIModelShowDetail) {
        // i dont want to show this button on the detail screen when user navigates from watchlist
        // better implementation would be to just dismiss detail screen and flash/highlight the item
        if (!arguments.fromWatchlist) {
            binding.btnDetailShowWatchStatus.text = getString(R.string.show_update_progress)
            binding.btnDetailShowWatchStatus.click {
                viewModel.submitAction(ActionDetailShow.updateProgress(data.id))
            }
        } else {
            binding.btnDetailShowWatchStatus.isVisible = false
        }
    }

    private fun stateAddedToWatchlist(data: UIModelShowDetail) {
        binding.btnDetailShowAdd.setBackgroundColor(Color.RED)
        binding.btnDetailShowAdd.text = getString(R.string.show_remove)
        binding.btnDetailShowAdd.click {
            viewModel.submitAction(ActionDetailShow.remove(data.id))
        }
    }

    private fun stateLoading() {
        binding.layoutDetailShowSkeleton.visible()
    }

    private fun stateError() {
        viewModel.submitAction(ActionDetailShow.ShowToast("Show Detail Error"))
    }
}