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
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sunrisekcdeveloper.showtracker.common.EndpointPoster
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.common.util.observeInLifecycle
import com.sunrisekcdeveloper.showtracker.common.util.setMaxLinesToEllipsize
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
        viewModel.submitAction(ActionDetailMovie.Load(arguments.movieId))
        setup()
        observe()
    }

    private fun observe() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            cleanUI()
            when (state) {
                StateDetailMovie.Loading -> { stateLoading() }
                is StateDetailMovie.Success -> { stateSuccess(state.data) }
                is StateDetailMovie.Error -> { stateError() }
            }
        }
        viewModel.eventsFlow.onEach { event ->
            when (event) {
                EventDetailMovie.Close -> { dismissAllowingStateLoss() }
                is EventDetailMovie.ShowToast -> {
                    Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }

    private fun setup() {
        // Navigation - Close fragment
        binding.imgDetailMovieClose.setOnClickListener {
            viewModel.submitAction(ActionDetailMovie.Close)
        }
        // bind poster and movie title
        bindPriorityData()
    }

    private fun bindPriorityData() {
        viewModel.movieDetails(arguments.movieId)
        binding.tvDetailMovieTitle.text = arguments.movieTitle
        Glide.with(binding.root)
            .load(EndpointPoster.Standard.urlWithResource(arguments.posterPath))
            .transform(CenterCrop())
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
            binding.btnDetailMovieAdd.text = "Remove"
            binding.btnDetailMovieAdd.click { viewModel.submitAction(ActionDetailMovie.Remove(data.id)) }
        } else {
            binding.btnDetailMovieAdd.text = "+ add"
            binding.btnDetailMovieAdd.click { viewModel.submitAction(ActionDetailMovie.Add(data.id)) }
        }

        // todo some business logic regarding the watched status of movies "deleted" and then
        //  re-added
        if (data.watched && !data.deleted) {
            binding.btnDetailMovieWatchStatus.text = "YOU'VE WATCHED THIS"
            binding.btnDetailMovieWatchStatus.click { viewModel.submitAction(ActionDetailMovie.SetUnwatched(data.id)) }
        } else {
            binding.btnDetailMovieWatchStatus.text = "MARK AS WATCHED"
            binding.btnDetailMovieWatchStatus.click { viewModel.submitAction(ActionDetailMovie.SetWatched(data.id)) }
        }
        binding.tvDetailMovieDescription.isVisible = true
        binding.tvDetailMovieRuntime.isVisible = true
        binding.tvDetailMovieReleaseYear.isVisible = true
        binding.tvSeparatorOne.isVisible = true
        binding.tvSeparatorTwo.isVisible = true
        binding.tvDetailMovieCertification.isVisible = true
        binding.btnDetailMovieAdd.isEnabled = true
        binding.btnDetailMovieWatchStatus.isEnabled = true
    }
    private fun stateLoading() {
        binding.layoutDetailMovieSkeleton.isVisible = true
    }
    private fun stateError() {
        Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
    }
    private fun cleanUI() {
        binding.layoutDetailMovieSkeleton.isGone = true
        binding.tvSeparatorOne.isGone = true
        binding.tvSeparatorTwo.isGone = true
        binding.tvDetailMovieDescription.isGone = true
        binding.tvDetailMovieRuntime.isGone = true
        binding.tvDetailMovieReleaseYear.isGone = true
        binding.tvDetailMovieCertification.isGone = true
        binding.btnDetailMovieAdd.isEnabled = false
        binding.btnDetailMovieWatchStatus.isEnabled = false
    }

}