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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.sunrisekcdeveloper.showtracker.databinding.FragmentMovieDetailBinding

class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding

    private val arguments: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater)
        bind()
        return binding.root
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
        } else if (list == "progress") {
            binding.chipChoiceInProgress.isChecked = true
        } else if (list == "upcoming") {
            binding.chipChoiceUpcoming.isChecked = true
        } else if (list == "completed") {
            binding.chipChoiceCompleted.isChecked = true
        } else if (list == "anticipated") {
            binding.chipChoiceAnticipated.isChecked = true
        } else {
            binding.chipChoiceNone.isChecked = true
        }
    }

}