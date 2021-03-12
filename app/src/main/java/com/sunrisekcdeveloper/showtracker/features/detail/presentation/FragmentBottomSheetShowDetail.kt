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

package com.sunrisekcdeveloper.showtracker.updated.features.detail.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.databinding.BottomSheetShowDetailBinding
import com.sunrisekcdeveloper.showtracker.features.detail.presentation.ViewModelShowDetail
import dagger.hilt.android.AndroidEntryPoint
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
        setup()
        observe()
    }

    private fun observe() {
        viewModel.showDetails.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Glide.with(requireContext())
                        .load("https://image.tmdb.org/t/p/w342${it.data.posterPath}")
                        .transform(CenterCrop())
                        .into(binding.imgDetailShowPoster)

                    binding.tvDetailShowTitle.text = it.data.name
                    binding.tvDetailShowDescription.text = it.data.overview
                    binding.tvDetailShowFirstAirDate.text = it.data.firstAirDate
                    binding.tvDetailShowCertification.text = it.data.certification
                    binding.tvDetailShowSeasons.text = getString(
                        R.string.season_with_number, it.data.seasonsTotal.toString()
                    )
                }
                // todo impl other cases
                is Resource.Error -> {}
                Resource.Loading -> {}
            }
        }
    }

    private fun setup() {
        // Navigation - Close fragment
        binding.imgDetailShowClose.setOnClickListener { dismissAllowingStateLoss() }
        viewModel.showDetails(arguments.showId)
    }
}