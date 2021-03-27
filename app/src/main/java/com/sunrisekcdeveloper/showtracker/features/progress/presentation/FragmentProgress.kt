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

package com.sunrisekcdeveloper.showtracker.features.progress.presentation

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
import com.google.android.material.snackbar.Snackbar
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.common.util.disabled
import com.sunrisekcdeveloper.showtracker.common.util.enabled
import com.sunrisekcdeveloper.showtracker.common.util.observeInLifecycle
import com.sunrisekcdeveloper.showtracker.databinding.FragmentSetProgressBinding
import com.sunrisekcdeveloper.showtracker.features.progress.domain.model.ActionProgress
import com.sunrisekcdeveloper.showtracker.features.progress.domain.model.EventProgress
import com.sunrisekcdeveloper.showtracker.features.progress.domain.model.StateProgress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class FragmentProgress : Fragment() {

    private lateinit var binding: FragmentSetProgressBinding

    private val viewModel: ViewModelProgress by viewModels()

    private val arguments: FragmentProgressArgs by navArgs()

    private var map: MutableMap<Int, Int> = mutableMapOf()

    private lateinit var adapterSeason: ArrayAdapter<Int>
    private lateinit var adapterEpisode: ArrayAdapter<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetProgressBinding.inflate(inflater)
        return binding.root
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
        viewModel.submitAction(ActionProgress.createToast("progress loading state"))
    }

    private fun cleanUI() {
        binding.btnProgressConfirm.disabled()
        binding.btnProgressUpToDate.disabled()
        binding.spinProgressEpisode.disabled()
        binding.spinProgressSeason.disabled()
    }

    private fun stateSuccess(data: Map<Int, Int>) {
        map = data.toMutableMap()

        adapterSeason.clear()
        adapterEpisode.clear()

        adapterSeason.addAll(*data.keys.toTypedArray())
        adapterSeason.notifyDataSetChanged()

        adapterEpisode.clear()
        adapterEpisode.addAll(
            *(1..map.getValue(1)).toList().toTypedArray()
        )

        binding.spinProgressSeason.enabled()
        binding.spinProgressEpisode.enabled()
        binding.btnProgressConfirm.enabled()
        binding.btnProgressUpToDate.enabled()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            cleanUI()
            when (state) {
                StateProgress.Loading -> { stateLoading() }
                is StateProgress.Success -> { stateSuccess(state.values) }
                is StateProgress.Error -> { stateError() }
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
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }

    private fun setup() {
        binding.btnProgressConfirm.disabled()
        binding.btnProgressUpToDate.disabled()

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

        binding.spinProgressSeason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (map.isNotEmpty()) {
                    Timber.e("Episodes for sesason ${position + 1}: ${map.getValue(position + 1)}")
                    adapterEpisode.clear()
                    adapterEpisode.addAll(
                        *(1..map.getValue(position+1)).toList().toTypedArray()
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Timber.e("nothing selected")
            }
        }

        binding.btnProgressConfirm.click {
            Timber.e("Selected: S${binding.spinProgressSeason.selectedItem}E${binding.spinProgressEpisode.selectedItem}")
            viewModel.submitAction(ActionProgress.SetShowProgress(
                arguments.showId,
                binding.spinProgressSeason.selectedItem.toString().toInt(),
                binding.spinProgressEpisode.selectedItem.toString().toInt()
            ))
        }

        binding.btnProgressUpToDate.click {
            viewModel.submitAction(ActionProgress.MarkShowUpToDate(arguments.showId))
        }
    }
}