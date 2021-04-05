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
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.util.*
import com.sunrisekcdeveloper.showtracker.databinding.BottomSheetShowDetailBinding
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class FragmentBottomSheetShowDetail : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetShowDetailBinding
    private val arguments: FragmentBottomSheetShowDetailArgs by navArgs()
    private val viewModel: ViewModelShowDetail by viewModels()

    @Inject
    lateinit var dataStore: DataStore<Preferences>

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
                            .navigateFromDetailShowToSetProgressFragment(event.showId, event.title)
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

    private fun setup() {
        binding.imgDetailShowClose.setOnClickListener { viewModel.submitAction(ActionDetailShow.close()) }
        bindPriorityData()
    }

    private fun bindPriorityData() {
        Glide.with(this)
            .load(EndpointPosterStandard(arguments.posterPath).url())
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
            if (data.seasonsTotal > 1) {
                R.string.season_plural_with_number
            } else {
                R.string.season_single_with_number
            }, data.seasonsTotal.toString()
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
        binding.btnDetailShowAdd.click {
            viewModel.submitAction(
                ActionDetailShow.add(
                    data.id,
                    data.name
                )
            )
        }
        stateNotStartedWatching(data)
    }

    private fun stateNotStartedWatching(data: UIModelShowDetail) {
        binding.btnDetailShowWatchStatus.text = getString(R.string.show_start_watching)
        binding.btnDetailShowWatchStatus.click {
            viewModel.submitAction(ActionDetailShow.startWatching(data.id, data.name))
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
        binding.btnDetailShowAdd.setBackgroundColor(fetchErrorColor(requireContext()))
        binding.btnDetailShowAdd.text = getString(R.string.show_remove)
        binding.btnDetailShowAdd.click {
            viewModel.submitAction(ActionDetailShow.attemptRemove(data.id, data.name))
        }
    }

    private fun stateLoading() {
        binding.layoutDetailShowSkeleton.visible()
    }

    private fun stateError() {
        viewModel.submitAction(ActionDetailShow.ShowToast("Show Detail Error"))
    }
}