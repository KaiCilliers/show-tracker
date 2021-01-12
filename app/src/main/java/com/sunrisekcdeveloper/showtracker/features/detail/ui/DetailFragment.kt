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

package com.sunrisekcdeveloper.showtracker.features.detail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.databinding.FragmentMovieDetailBinding
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie
import com.sunrisekcdeveloper.showtracker.commons.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.features.detail.adapters.MediumPosterAdapterDetail
import com.sunrisekcdeveloper.showtracker.commons.util.subscribe
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Detail Fragment that provides detailed information about a show or movie. Also provides a list
 * of suggested shows or movies that are deemed similar to the movie or show displayed in this
 * screen
 */
// TODO make new activity: reason: no bottom nav bar and to get back button in titlebargit
@AndroidEntryPoint
class DetailFragment : Fragment() {

    @Inject lateinit var adapter: MediumPosterAdapterDetail

    private lateinit var binding: FragmentMovieDetailBinding

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        setupBinding()
        observeViewModel()
        return binding.root
    }
    private fun setupBinding() {
        viewModel.getMovieDetails((arguments?.getString("mediaSlug","defua") ?: ""))
        Timber.d(arguments?.getString("mediaSlug","defua"))
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        // Temporal Coupling
        adapter.addOnClickAction(object : ClickActionContract {
            override fun onClick(item: Movie) {
                Timber.d("DETAIL TITLE: $item")
                // TODO refresh view with new movie data and scroll to top instead of relaunching fragment
                findNavController().navigate(
                    DetailFragmentDirections.actionDetailFragmentDestSelf(
                        item.slug
                    )
                )
            }
        })
        binding.rcMoreLikeThis.adapter = adapter
        binding.rcMoreLikeThis.layoutManager = GridLayoutManager(
            requireContext(), 3, GridLayoutManager.VERTICAL, false
        )
        binding.rcMoreLikeThis.isNestedScrollingEnabled = false
    }
    private fun observeViewModel() {
        viewModel.detailedMovie.subscribe(viewLifecycleOwner) {
            Timber.d("new movie: $it")
            binding.movie = it
            Glide.with(this)
                .load(it.basics.posterUrl)
                .placeholder(R.drawable.wanted_poster)
                .error(R.drawable.error_poster)
                .into(binding.imgvMoviePoster)
        }
        viewModel.relatedMovies.subscribe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}