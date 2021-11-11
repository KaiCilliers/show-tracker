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

package com.sunrisekcdeveloper.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.sunrisekcdeveloper.common.timber
import com.sunrisekcdeveloper.common.viewBinding
import com.sunrisekcdeveloper.progress.databinding.FragmentSetProgressBinding
import com.sunrisekcdeveloper.progress.extras.KeyPersistenceStore
import com.sunrisekcdeveloper.progress.extras.gone
import com.sunrisekcdeveloper.progress.extras.model.ActionProgress
import com.sunrisekcdeveloper.progress.extras.model.EventProgress
import com.sunrisekcdeveloper.progress.extras.model.StateProgress
import com.sunrisekcdeveloper.progress.extras.observeInLifecycle
import com.sunrisekcdeveloper.progress.extras.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProgressFragment : Fragment() {

    private val binding by viewBinding(FragmentSetProgressBinding::bind)
    private val viewModel: ProgressViewModel by viewModels()
    private val arguments: ProgressFragmentArgs by navArgs()
    private val log by timber()

    private var map: MutableMap<Int, List<Int>> = mutableMapOf()

    private lateinit var adapterSeason: ArrayAdapter<Int>
    private lateinit var adapterEpisode: ArrayAdapter<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_set_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setup()
        binding.toolbarProgress.setNavigationOnClickListener {
            viewModel.submitAction(ActionProgress.navigateBack())
        }
        viewModel.submitAction(ActionProgress.load(arguments.showId))
        observeViewModel()
    }

    private fun stateError() {
        viewModel.submitAction(ActionProgress.createToast("progress error state"))
    }

    private fun stateLoading() {
        binding.progressIndicator.visible()
        binding.tvLoading.visible()
    }

    private fun cleanUI() {
        binding.tvLoading.gone()
        binding.progressIndicator.gone()
        binding.btnProgressConfirm.gone()
        binding.btnProgressUpToDate.gone()
        binding.spinProgressEpisode.gone()
        binding.spinProgressSeason.gone()
        binding.tvProgressEpisode.gone()
        binding.tvProgressSeason.gone()
    }

    private fun stateSuccess(data: Map<Int, List<Int>>) {
        map = data.toMutableMap()

        adapterSeason.clear()
        adapterEpisode.clear()

        adapterSeason.addAll(*data.keys.toTypedArray())
        adapterSeason.notifyDataSetChanged()

        adapterEpisode.clear()
        adapterEpisode.addAll(
            map.getValue(1)
//            *(1..map.getValue(1)).toList().toTypedArray()
        )

        binding.tvProgressSeason.visible()
        binding.tvProgressEpisode.visible()
        binding.spinProgressSeason.visible()
        binding.spinProgressEpisode.visible()
        binding.btnProgressConfirm.visible()
        binding.btnProgressUpToDate.visible()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            cleanUI()
            when (state) {
                StateProgress.Loading -> {
                    stateLoading()
                }
                is StateProgress.Success -> {
                    stateSuccess(state.values)
                }
                is StateProgress.Error -> {
                    stateError()
                }
            }
        }
        viewModel.eventsFlow.onEach { event ->
            when (event) {
                EventProgress.PopBackStack -> {
                    findNavController().popBackStack()
                }
                is EventProgress.ShowToast -> {
                    Snackbar.make(binding.root, event.msg, Snackbar.LENGTH_SHORT).show()
                }
                is EventProgress.ShowConfirmationDialogSetProgress -> {
                    showConfirmationDialogProgress(
                        event.showId,
                        event.seasonNumber,
                        event.episodeNumber,
                        event.title
                    )
                }
                is EventProgress.ShowConfirmationDialogUpToDate -> {
                    showConfirmationDialogUpToDate(event.showId, event.title)
                }
                is EventProgress.SaveSnackbarMessage -> {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        KeyPersistenceStore(getString(R.string.key_disc_snack_bar)).value(), event.message
                    )
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }

    private fun showConfirmationDialogProgress(
        showId: String,
        season: Int,
        episode: Int,
        title: String
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Set Show Progress")
            .setMessage("This will mark Episode $episode from Season $season from \"$title\"as the next episode you need to watch")
            .setNegativeButton("Cancel") { _, _ -> }
            .setPositiveButton("Confirm") { _, _ ->
                viewModel.submitAction(
                    ActionProgress.setShowProgress(
                        showId,
                        season,
                        episode,
                        title
                    )
                )
            }
            .show()
    }

    private fun showConfirmationDialogUpToDate(showId: String, title: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Mark Show up to date")
            .setMessage("This will set all currently available episodes from the show \"$title\" as watched")
            .setNegativeButton("Cancel") { _, _ -> }
            .setPositiveButton("Confirm") { _, _ ->
                viewModel.submitAction(
                    ActionProgress.markShowUpToDate(
                        showId,
                        title
                    )
                )
            }
            .show()
    }

    private fun setup() {
        adapterSeason = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf<Int>()
        )
        adapterEpisode = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf<Int>()
        )

        binding.spinProgressSeason.adapter = adapterSeason
        binding.spinProgressEpisode.adapter = adapterEpisode

        binding.spinProgressSeason.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (map.isNotEmpty()) {
                        log.e("Episodes for sesason ${position + 1}: ${map.getValue(position + 1)}")
                        adapterEpisode.clear()
                        adapterEpisode.addAll(
                            map.getValue(position+1)
//                            *(1..map.getValue(position + 1)).toList().toTypedArray()
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    log.e("nothing selected")
                }
            }

        binding.btnProgressConfirm.setOnClickListener {
            log.e("Selected: S${binding.spinProgressSeason.selectedItem}E${binding.spinProgressEpisode.selectedItem}")
            viewModel.submitAction(
                ActionProgress.attemptSetProgress(
                    arguments.showId,
                    binding.spinProgressSeason.selectedItem.toString().toInt(),
                    binding.spinProgressEpisode.selectedItem.toString().toInt(),
                    arguments.showTitle
                )
            )
        }

        binding.btnProgressUpToDate.setOnClickListener {
            viewModel.submitAction(
                ActionProgress.attemptMarkUpToDate(
                    arguments.showId,
                    arguments.showTitle
                )
            )
        }
    }
}