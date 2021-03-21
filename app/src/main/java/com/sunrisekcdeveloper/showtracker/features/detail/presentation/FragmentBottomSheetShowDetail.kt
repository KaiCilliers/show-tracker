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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.databinding.BottomSheetShowDetailBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.shows.FragmentDiscoveryShowsDirections
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

                    Timber.e("watchlisted: ${it.data.watchlisted} " +
                            "started: ${it.data.startedWatching}" +
                            " uptodate: ${it.data.upToDate} ")

                    Timber.e("${it.data}")

                    // todo this is a mess FIX up to date shows not showing
                    if (it.data.watchlisted && !it.data.deleted) {
                        binding.btnDetailShowAdd.text = "Added"
                        binding.btnDetailShowAdd.click {
                            Timber.e("removing show from watchlist...")
                            viewModel.removeShowFromWatchlist(it.data.id)
                        }

                        if (!it.data.startedWatching) {
                            binding.btnDetailShowWatchStatus.text = "Start Watching"
                            binding.btnDetailShowWatchStatus.click {
                                findNavController().navigate(
                                    FragmentBottomSheetShowDetailDirections.navigateFromDetailShowToNavGraphProgress(it.data.id)
                                )
                                Timber.e("button is start watching")
                            }
                        }

                        if (it.data.upToDate && !it.data.deleted) {
                            binding.btnDetailShowWatchStatus.text = "Up to date"
                            binding.btnDetailShowWatchStatus.click {
                                Timber.e("button is up to date")
                            }
                        }

                        if ((it.data.startedWatching) && (!it.data.upToDate) && !it.data.deleted) {
                            binding.btnDetailShowWatchStatus.text = "Update progress"
                            binding.btnDetailShowWatchStatus.click {
                                Timber.e("button is update progress")
                                findNavController().navigate(
                                    FragmentBottomSheetShowDetailDirections.navigateFromDetailShowToWatchlistFragment(it.data.id)
                                )
                            }
                        } else {
                            binding.btnDetailShowWatchStatus.text = "Start Watching"
                            binding.btnDetailShowWatchStatus.click {
                                // todo note duplicated 3 time - not cool man
                                findNavController().navigate(
                                    FragmentBottomSheetShowDetailDirections.navigateFromDetailShowToNavGraphProgress(it.data.id)
                                )
                                Timber.e("button is start watching")
                            }
                        }
                    } else {
                        binding.btnDetailShowAdd.text = "+ Add"
                        binding.btnDetailShowAdd.click {
                            Timber.e("adding show to watchlist")
                            viewModel.addShowToWatchlist(it.data.id)
                        }
                        binding.btnDetailShowWatchStatus.text = "Start Watching"
                        binding.btnDetailShowWatchStatus.click {
                            findNavController().navigate(
                                FragmentBottomSheetShowDetailDirections.navigateFromDetailShowToNavGraphProgress(it.data.id)
                            )
                            Timber.e("button is start watching")
                        }
                    }

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