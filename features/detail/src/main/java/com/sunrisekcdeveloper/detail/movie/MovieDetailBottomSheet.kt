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

package com.sunrisekcdeveloper.detail.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sunrisekcdeveloper.common.timber
import com.sunrisekcdeveloper.detail.*
import com.sunrisekcdeveloper.detail.databinding.BottomSheetMovieDetailBinding
import com.sunrisekcdeveloper.detail.extras.model.*
import com.sunrisekcdeveloper.detail.extras.*
import com.sunrisekcdeveloper.network.EndpointPosterStandard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class MovieDetailBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetMovieDetailBinding
    private val arguments: MovieDetailBottomSheetArgs by navArgs()
    private val viewModel: MovieDetailViewModel by viewModels()
    private val log by timber()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setup()
        observe()
    }

    private fun init() {
        viewModel.submitAction(ActionDetailMovie.load(arguments.movieId))
    }

    private fun setup() {
        // Set properties
//        (requireDialog() as BottomSheetDialog).dismissWithAnimation = true

        // Navigation - Close fragment
        binding.imgDetailMovieClose.setOnClickListener {
            viewModel.submitAction(ActionDetailMovie.close())
        }

        // bind priority data
        binding.tvDetailMovieTitle.text = arguments.movieTitle
        ImageLoadingStandardGlide(this)
            .load(EndpointPosterStandard(arguments.posterPath).url(), binding.imgDetailMoviePoster)
    }

    private fun observe() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            cleanUI()
            when (state) {
                StateDetailMovie.Loading -> {
                    renderLoading()
                }
                is StateDetailMovie.Success -> {
                    renderSuccess(state.data)
                }
                is StateDetailMovie.Error -> {
                    log.e(state.exception)
                    renderError()
                }
            }
        }
        viewModel.eventsFlow.onEach { event ->
            when (event) {
                EventDetailMovie.Close -> {
                    dismiss()
                }
                is EventDetailMovie.ShowToast -> {
                    Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                }
                is EventDetailMovie.ShowConfirmationDialog -> {
                    showConfirmationDialog(event.movieId, event.title)
                }
                is EventDetailMovie.ShowConfirmationDialogUnwatch -> {
                    showConfirmationDialogUnwatch(event.movieId, event.title)
                }
                is EventDetailMovie.SaveSnackbarMessage -> {
                    findNavController().previousBackStackEntry?.savedStateHandle
                        ?.set(KeyPersistenceStore(getString(R.string.key_disc_snack_bar)).value(), event.message)
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }

    private fun renderSuccess(data: UIModelMovieDetail) {
        binding.tvDetailMovieDescription.apply {
            text = data.overview
            setMaxLinesToEllipsize()
        }

        val hours = data.runtime.toInt() / 60
        val minutes = data.runtime.toInt() % 60

        binding.tvDetailMovieRuntime.text = getString(R.string.runtime_with_value, hours, minutes)
        binding.tvDetailMovieReleaseYear.text = data.releaseYear
        binding.tvDetailMovieCertification.text = data.certification

        when (data.status) {
            MovieWatchlistStatus.Watchlisted -> {
                binding.btnDetailMovieAdd.setBackgroundColor(fetchErrorColor(requireContext()))
                binding.btnDetailMovieAdd.text = getString(R.string.remove)
                binding.btnDetailMovieAdd.setOnClickListener {
                    viewModel.submitAction(
                        ActionDetailMovie.attemptRemove(
                            data.id,
                            data.title
                        )
                    )
                }
            }
            MovieWatchlistStatus.NotWatchlisted -> {
                binding.btnDetailMovieAdd.setBackgroundColor(fetchPrimaryColor(requireContext()))
                // todo you can add drawables to buttons
                binding.btnDetailMovieAdd.text = getString(R.string.add_button)
                binding.btnDetailMovieAdd.setOnClickListener { viewModel.submitAction(ActionDetailMovie.add(data.id, data.title)) }
            }
        }

        // todo some business logic regarding the watched status of movies "deleted" and then re-added
        when (data.watched) {
            WatchedStatus.Watched -> {
                binding.btnDetailMovieWatchStatus.text = getString(R.string.watched)
                binding.btnDetailMovieWatchStatus.setOnClickListener {
                    viewModel.submitAction(
                        ActionDetailMovie.attemptUnwatch(data.id, data.title)
                    )
                }
            }
            WatchedStatus.NotWatched -> {
                binding.btnDetailMovieWatchStatus.text = getString(R.string.mark_watched)
                binding.btnDetailMovieWatchStatus.setOnClickListener {
                    viewModel.submitAction(
                        ActionDetailMovie.setWatched(
                            data.id, data.title
                        )
                    )
                }
            }
        }

        binding.tvDetailMovieDescription.visible()
        binding.tvDetailMovieRuntime.visible()
        binding.tvDetailMovieReleaseYear.visible()
        binding.tvSeparatorOne.visible()
        binding.tvSeparatorTwo.visible()
        binding.tvDetailMovieCertification.visible()
        binding.btnDetailMovieAdd.enabled()
        binding.btnDetailMovieWatchStatus.enabled()
    }

    private fun renderLoading() {
        binding.layoutDetailMovieSkeleton.visible()
    }

    private fun renderError() {
        viewModel.submitAction(ActionDetailMovie.showToast("error"))
    }

    private fun cleanUI() {
        binding.layoutDetailMovieSkeleton.gone()
        binding.tvSeparatorOne.gone()
        binding.tvSeparatorTwo.gone()
        binding.tvDetailMovieDescription.gone()
        binding.tvDetailMovieRuntime.gone()
        binding.tvDetailMovieReleaseYear.gone()
        binding.tvDetailMovieCertification.gone()
        binding.btnDetailMovieAdd.disabled()
        binding.btnDetailMovieWatchStatus.disabled()
    }

    private fun showConfirmationDialogUnwatch(movieId: String, title: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Unwatch Movie?")
            .setMessage("This will set \"$title\" as an unwatched movie in your watchlist")
            .setNegativeButton("Cancel") { _, _ -> }
            .setPositiveButton("Unwatch") { _, _ ->
                viewModel.submitAction(
                    ActionDetailMovie.setUnwatched(
                        movieId, title
                    )
                )
            }
            .show()
    }

    private fun showConfirmationDialog(movieId: String, title: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Remove Movie?")
            .setMessage("This will remove \"$title\" from your watchlist")
            .setNegativeButton("Keep") { _, _ -> }
            .setPositiveButton("Remove") { _, _ ->
                viewModel.submitAction(
                    ActionDetailMovie.remove(
                        movieId, title
                    )
                )
            }
            .show()
    }
}