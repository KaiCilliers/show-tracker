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

package com.sunrisekcdeveloper.showtracker.features.detail.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.chip.Chip
import com.sunrisekcdeveloper.showtracker.databinding.FragmentMovieDetailBinding
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.WatchListType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding

    private val arguments: MovieDetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels()

    private lateinit var selectedChip: Chip

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater)
        bind()
        setup()
        return binding.root
    }

    private fun setup() {
        binding.cgChoiceSaveInList.setOnCheckedChangeListener { group, checkedId ->
            val chip = requireActivity().findViewById<Chip?>(checkedId)
            val fromList = when (selectedChip) {
                this.binding.chipChoiceRecentlyAdded ->  WatchListType.RECENTLY_ADDED
                this.binding.chipChoiceAnticipated ->  WatchListType.ANTICIPATED
                this.binding.chipChoiceCompleted ->  WatchListType.COMPLETED
                this.binding.chipChoiceUpcoming ->  WatchListType.UPCOMING
                this.binding.chipChoiceInProgress ->  WatchListType.IN_PROGRESS
                else -> WatchListType.NONE
            }
            val choice = chip?.let {
                when (chip) {
                    this.binding.chipChoiceRecentlyAdded ->  WatchListType.RECENTLY_ADDED
                    this.binding.chipChoiceAnticipated ->  WatchListType.ANTICIPATED
                    this.binding.chipChoiceCompleted ->  WatchListType.COMPLETED
                    this.binding.chipChoiceUpcoming ->  WatchListType.UPCOMING
                    this.binding.chipChoiceInProgress ->  WatchListType.IN_PROGRESS
                    else -> WatchListType.NONE
                }
            }
            viewModel.updateWatchList(
                arguments.movieId,
                fromList,
                choice!!
            )
        }
    }

    private fun bind() {
        val baseUrl = "https://image.tmdb.org/t/p/"
        val backdropSize = "w1280"
        val posterSize = "w342"

        Glide.with(this)
            .load(baseUrl + backdropSize + arguments.movieBackdrop)
            .transform(CenterCrop())
            .into(binding.movieBackdrop)

        Glide.with(this)
            .load(baseUrl + posterSize + arguments.moviePoster)
            .transform(CenterCrop())
            .into(binding.moviePoster)

        // todo utilize databinding here
        binding.movieTitle.text = arguments.movieTitle
        binding.movieRating.rating = arguments.movieRating / 2f
        binding.movieReleaseDate.text = arguments.movieReleaseDate
        binding.movieOverview.text = arguments.movieOverview

        determineList()
    }

    private fun determineList() {
        val list = arguments.watchlistType
        if (list == "recently") {
            binding.chipChoiceRecentlyAdded.isChecked = true
            selectedChip = binding.chipChoiceRecentlyAdded
        } else if (list == "progress") {
            binding.chipChoiceInProgress.isChecked = true
            selectedChip = binding.chipChoiceInProgress
        } else if (list == "upcoming") {
            binding.chipChoiceUpcoming.isChecked = true
            selectedChip = binding.chipChoiceUpcoming
        } else if (list == "completed") {
            binding.chipChoiceCompleted.isChecked = true
            selectedChip = binding.chipChoiceCompleted
        } else if (list == "anticipated") {
            binding.chipChoiceAnticipated.isChecked = true
            selectedChip = binding.chipChoiceAnticipated
        } else {
            binding.chipChoiceNone.isChecked = true
            selectedChip = binding.chipChoiceNone
        }
    }

}