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

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.EndpointPoster
import com.sunrisekcdeveloper.showtracker.common.util.*
import com.sunrisekcdeveloper.showtracker.databinding.BottomSheetMovieDetailBinding
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.ActionDetailMovie
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.EventDetailMovie
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.StateDetailMovie
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelMovieDetail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class FragmentBottomSheetMovieDetail : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetMovieDetailBinding
    private val arguments: FragmentBottomSheetMovieDetailArgs by navArgs()
    private val viewModel: ViewModelMovieDetail by viewModels()

    private lateinit var tempButtonColor: Drawable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetMovieDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.submitAction(ActionDetailMovie.load(arguments.movieId))
        setup()
        observe()
    }

    private fun observe() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            cleanUI()
            when (state) {
                StateDetailMovie.Loading -> {
                    stateLoading()
                }
                is StateDetailMovie.Success -> {
                    stateSuccess(state.data)
                }
                is StateDetailMovie.Error -> {
                    stateError()
                }
            }
        }
        viewModel.eventsFlow.onEach { event ->
            when (event) {
                EventDetailMovie.Close -> {
                    // todo this property needs to be set somewhere else
                    (requireDialog() as BottomSheetDialog).dismissWithAnimation = true
                    dismiss()
                }
                is EventDetailMovie.ShowToast -> {
                    Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }

    private fun setup() {
        // Navigation - Close fragment
        binding.imgDetailMovieClose.setOnClickListener {
            viewModel.submitAction(ActionDetailMovie.close())
        }
        // bind poster and movie title
        bindPriorityData()
    }

    private fun bindPriorityData() {
        binding.tvDetailMovieTitle.text = arguments.movieTitle
        Glide.with(this)
            .load(EndpointPoster.Standard.urlWithResource(arguments.posterPath))
            .centerCrop()
            .error(R.drawable.error_poster)
            .transition(DrawableTransitionOptions.withCrossFade(100))
            .into(binding.imgDetailMoviePoster)
    }

    private fun stateSuccess(data: UIModelMovieDetail) {
        binding.tvDetailMovieDescription.apply {
            text = data.overview
            setMaxLinesToEllipsize()
        }
        binding.tvDetailMovieRuntime.text = data.runtime
        binding.tvDetailMovieReleaseYear.text = data.releaseYear
        binding.tvDetailMovieCertification.text = data.certification

        if (data.watchlisted && !data.deleted) {
            binding.btnDetailMovieAdd.setBackgroundColor(Color.RED)
            binding.btnDetailMovieAdd.text = getString(R.string.remove)
            binding.btnDetailMovieAdd.click { viewModel.submitAction(ActionDetailMovie.remove(data.id)) }
        } else {

            binding.btnDetailMovieAdd.setBackgroundColor(fetchPrimaryColor(requireContext()))
            binding.btnDetailMovieAdd.text = getString(R.string.add_button)
            binding.btnDetailMovieAdd.click { viewModel.submitAction(ActionDetailMovie.add(data.id)) }
        }

        // todo some business logic regarding the watched status of movies "deleted" and then
        //  re-added
        if (data.watched && !data.deleted) {
            binding.btnDetailMovieWatchStatus.text = getString(R.string.already_watched)
            binding.btnDetailMovieWatchStatus.click { viewModel.submitAction(
                ActionDetailMovie.setUnwatched(
                    data.id
                )
            ) }
        } else {
            binding.btnDetailMovieWatchStatus.text = getString(R.string.mark_watched)
            binding.btnDetailMovieWatchStatus.click { viewModel.submitAction(
                ActionDetailMovie.setWatched(
                    data.id
                )
            ) }
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
    private fun stateLoading() {
        binding.layoutDetailMovieSkeleton.visible()
    }
    private fun stateError() {
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
}