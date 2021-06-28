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

package com.sunrisekcdeveloper.detail.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sunrisekcdeveloper.detail.*
import com.sunrisekcdeveloper.detail.databinding.BottomSheetShowDetailBinding
import com.sunrisekcdeveloper.detail.domain.model.*
import com.sunrisekcdeveloper.detail.domain.util.ActionButton
import com.sunrisekcdeveloper.detail.ImageLoadingStandardGlide
import com.sunrisekcdeveloper.models.EndpointPosterStandard
import com.sunrisekcdeveloper.models.navigation.InternalDeepLink
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
        init()
        setup()
        observe()
    }

    private fun init() {
        viewModel.submitAction(ActionDetailShow.load(arguments.showId))
    }

    private fun setup() {
        (requireDialog() as BottomSheetDialog).dismissWithAnimation = true
        binding.imgDetailShowClose.setOnClickListener { viewModel.submitAction(ActionDetailShow.close()) }
        binding.tvDetailShowTitle.text = arguments.showTitle
        ImageLoadingStandardGlide(this)
            .load(EndpointPosterStandard(arguments.posterPath).url(), binding.imgDetailShowPoster)
    }

    private fun observe() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            cleanUI()
            when (state) {
                StateDetailShow.Loading -> {
                    renderLoading()
                }
                is StateDetailShow.Success -> {
                    renderSuccess(state.data)
                }
                is StateDetailShow.Error -> {
                    Timber.e(state.exception)
                    renderError()
                }
            }
        }
        viewModel.eventsFlow.onEach { event ->
            when (event) {
                is EventDetailShow.LaunchStartWatching -> {
                    val intent = InternalDeepLink.moduleProgress(
                        event.showId,
                        event.title
                    ).toUri()
                    findNavController().navigate(intent)
//                    findNavController().navigate(
//                        FragmentBottomSheetShowDetailDirections
//                            .navigateFromDetailShowToSetProgressFragment(event.showId, event.title)
//                    )
                }
                is EventDetailShow.GoToShowInWatchlist -> {
                    val intent = InternalDeepLink.moduleWatchlist(event.showId).toUri()
                    findNavController().navigate(intent)
//                    findNavController().navigate(
//                        FragmentBottomSheetShowDetailDirections
//                            .navigateFromDetailShowToWatchlistFragment(event.showId)
//                    )
                }
                EventDetailShow.Close -> {
                    dismiss()
                }
                is EventDetailShow.ShowToast -> {
                    Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                }
                is EventDetailShow.ShowConfirmationDialog -> {
                    showConfirmationDialog(event.showId, event.title)
                }
                is EventDetailShow.SaveSnackbarMessage -> {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        KeyPersistenceStore(getString(R.string.key_disc_snack_bar)).value(), event.message
                    )
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
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

    private fun renderSuccess(data: UIModelShowDetail) {
        binding.tvDetailShowDescription.apply {
            text = data.overview
            setMaxLinesToEllipsize()
        }
        binding.tvDetailShowFirstAirDate.text = data.firstAirDate
        binding.tvDetailShowCertification.text = data.certification
        binding.tvDetailShowSeasons.text = getString(
            if (data.seasonsTotal > 1) {
                R.string.season_plural_with_number
            } else {
                R.string.season_single_with_number
            }, data.seasonsTotal.toString()
        )

        when (data.watchlist) {
            ShowWatchlistStatus.Watchlisted -> {
                ActionButton(binding.btnDetailShowAdd).paint(
                    fetchErrorColor(requireContext()),
                    getString(R.string.show_remove)
                ) {
                    viewModel.submitAction(ActionDetailShow.attemptRemove(data.id, data.name))
                }
            }
            ShowWatchlistStatus.NotWatchlisted -> {
                ActionButton(binding.btnDetailShowAdd).paint(
                    fetchPrimaryColor(requireContext()),
                    getString(R.string.show_add)
                ) {
                    viewModel.submitAction(
                        ActionDetailShow.add(
                            data.id,
                            data.name
                        )
                    )
                }
            }
        }

        when (data.status) {
            ShowStatus.NotStarted -> {
                ActionButton(binding.btnDetailShowWatchStatus).paint(
                    text = getString(R.string.show_start_watching)
                ) {
                    viewModel.submitAction(ActionDetailShow.startWatching(data.id, data.name))
                }
            }
            ShowStatus.Started -> {
//                if (!arguments.fromWatchlist) {
                if(true) {
                    ActionButton(binding.btnDetailShowWatchStatus).paint(
                        text = getString(R.string.show_update_progress)
                    ) {
                        viewModel.submitAction(ActionDetailShow.updateProgress(data.id))
                    }
                } else {
                    binding.btnDetailShowWatchStatus.isGone = true
                }
            }
            ShowStatus.UpToDate -> {
                ActionButton(binding.btnDetailShowWatchStatus).paint(
                    text = getString(R.string.show_up_to_date)
                ) {
                    viewModel.submitAction(ActionDetailShow.showToast("Show is up to date"))
                }
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

    private fun renderLoading() {
        binding.layoutDetailShowSkeleton.visible()
    }

    private fun renderError() {
        viewModel.submitAction(ActionDetailShow.ShowToast("Show Detail Error"))
    }

    private fun showConfirmationDialog(showId: String, title: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Remove TV Show?")
            .setMessage("This will remove \"$title\" from your watchlist")
            .setNegativeButton("Keep") { _, _ -> }
            .setPositiveButton("Remove") { _, _ ->
                viewModel.submitAction(
                    ActionDetailShow.remove(
                        showId, title
                    )
                )
            }
            .show()
    }

}