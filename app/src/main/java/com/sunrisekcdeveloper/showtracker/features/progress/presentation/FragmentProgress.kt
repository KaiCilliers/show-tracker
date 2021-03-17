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
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.databinding.FragmentSetProgressBinding
import dagger.hilt.android.AndroidEntryPoint
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
            findNavController().popBackStack()
        }
        viewModel.getShowSeasonAndEpisodeCount(arguments.showIdTest)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.showSeasonAndEpisodeCount.observe(viewLifecycleOwner) {
            Timber.e("from VM: $it")
            when (it) {
                is Resource.Success -> {
                    map = it.data.toMutableMap()
                    Timber.e("clearing spinner values")
                    adapterSeason.clear()
                    adapterEpisode.clear()

                    Timber.e("Repopulating spinners...")
                    Timber.e("Season spinner values: ${it.data.keys}")
                    adapterSeason.addAll(*it.data.keys.toTypedArray())
                    adapterSeason.notifyDataSetChanged()

                    adapterEpisode.clear()
                    adapterEpisode.addAll(
                        *(1..map.getValue(1)).toList().toTypedArray()
                    )
                }
                is Resource.Error -> {}
                Resource.Loading -> {}
            }
        }
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
            viewModel.setShowProgress(
                arguments.showIdTest,
                binding.spinProgressSeason.selectedItem.toString().toInt(),
                binding.spinProgressEpisode.selectedItem.toString().toInt()
            )
        }

        binding.btnProgressUpToDate.click {
            Timber.e("Last Season: ${adapterSeason.count}")
            Timber.e("Last Episode: ${map.getValue(adapterSeason.count)}")
            viewModel.setShowUpToDate(arguments.showIdTest)
        }
    }
}