/*
 * Copyright © 2020. The Android Open Source Project
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

package com.sunrisekcdeveloper.showtracker.ui.screens.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.databinding.FragmentMovieDetailBinding
import com.sunrisekcdeveloper.showtracker.model.DetailedMovie
import com.sunrisekcdeveloper.showtracker.model.Movie
import com.sunrisekcdeveloper.showtracker.ui.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.ui.components.adapters.impl.MediumPosterAdapter
import com.sunrisekcdeveloper.showtracker.util.subscribe
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

    @Inject lateinit var adapter: MediumPosterAdapter

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
        binding.movie = DetailedMovie(
            Movie("The Incredibles"),
            "1997",
            "16+",
            "1h58m",
            getString(R.string.movie_description_dummy),
            "Jack Black, Peter Griff.. more",
            "James Cameron"
        )
        Timber.d(arguments?.getString("dummy","2ndDef"))
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        // Temporal Coupling
        adapter.addOnClickAction(object : ClickActionContract {
            override fun onClick(item: Any) {
                Timber.d("DETAIL TITLE: $item")
                // TODO refresh view with new movie data and scroll to top instead of relaunching fragment
                findNavController().navigate(DetailFragmentDirections.actionDetailFragmentDestSelf("FROM SELF"))
            }
        })
        binding.rcMoreLikeThis.adapter = adapter
        binding.rcMoreLikeThis.layoutManager = GridLayoutManager(
            requireContext(), 3, GridLayoutManager.VERTICAL, false
        )
        binding.rcMoreLikeThis.isNestedScrollingEnabled = false
    }
    private fun observeViewModel() {
        viewModel.movieListData.subscribe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}