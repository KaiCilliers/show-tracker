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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.databinding.BottomSheetMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FragmentBottomSheetMovieDetail : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetMovieDetailBinding
    private val arguments: FragmentBottomSheetMovieDetailArgs by navArgs()
    private val viewModel: ViewModelMovieDetail by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetMovieDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setup()
        observe()
    }

    private fun observe() {
        viewModel.movieDetails.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Glide.with(requireContext())
                        .load("https://image.tmdb.org/t/p/w342${it.data.posterPath}")
                        .transform(CenterCrop())
                        .into(binding.imgDetailMoviePoster)
                    binding.tvDetailMovieTitle.text = it.data.title
                    binding.tvDetailMovieDescription.text = it.data.overview
                    binding.tvDetailMovieRuntime.text = it.data.runtime
                    binding.tvDetailMovieReleaseYear.text = it.data.releaseYear
                    binding.tvDetailMovieCertification.text = it.data.certification

                    if (it.data.watchlisted) {
                        binding.btnDetailMovieAdd.text = "ADDED"
                        binding.btnDetailMovieAdd.click {
                            viewModel.removeMovieFromWatchlist(it.data.id)
                        }
                    } else {
                        binding.btnDetailMovieAdd.text = "+ ADD"
                        binding.btnDetailMovieAdd.click {
                            viewModel.addMovieToWatchlist(it.data.id)
                        }
                    }

                    if (it.data.watched) {
                        binding.btnDetailMovieWatchStatus.text = "YOU'VE WATCHED THIS"
                        binding.btnDetailMovieWatchStatus.click {
                            viewModel.markMovieAsUnWatched(it.data.id)
                        }
                    } else {
                        binding.btnDetailMovieWatchStatus.text = "MARK AS WATCHED"
                        binding.btnDetailMovieWatchStatus.click {
                            viewModel.markMovieAsWatched(it.data.id)
                        }
                    }
                }
                // todo impl other cases
                is Resource.Error -> { }
                Resource.Loading -> { }
            }
        }
    }

    private fun setup() {
        // Navigation - Close fragment
        binding.imgDetailMovieClose.setOnClickListener { dismissAllowingStateLoss() }
        viewModel.movieDetails(arguments.movieId)
    }

}