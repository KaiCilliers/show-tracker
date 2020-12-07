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

package com.sunrisekcdeveloper.showtracker.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunrisekcdeveloper.showtracker.ui.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.FragmentProgressBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.Movie
import com.sunrisekcdeveloper.showtracker.entities.domain.SuggestionListModel
import com.sunrisekcdeveloper.showtracker.ui.components.adapters.impl.MovieSummaryAdapter
import com.sunrisekcdeveloper.showtracker.ui.components.adapters.impl.SmallPosterAdapter
import timber.log.Timber

/**
 * Progress Fragment that displays upcoming movies and shows with the capability to filter
 * based on movie or show
 *
 * @constructor Create empty Progress fragment
 */
class ProgressFragment : Fragment() {

    private val adapter by lazy {
        SmallPosterAdapter()
    }

    private val upComingAdapter by lazy {
        MovieSummaryAdapter()
    }

    private val dummyMovies: List<Movie> by lazy {
        populatedummydata()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentProgressBinding.inflate(inflater)
        // Temporal Coupling
        adapter.addOnClickAction(object : ClickActionContract {
                override fun onClick(item: Any) {
                    Timber.d("TITLE: $item")
                    findNavController().navigate(
                        ProgressFragmentDirections.actionProgressFragmentDestToDetailFragment(
                            "FROM PROGRESS FRAGMENT"
                        )
                    )
                }
            })
        binding.rcFilterOptions.adapter = adapter
        binding.rcFilterOptions.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        // Temporal Coupling
        upComingAdapter.addOnClickAction(object : ClickActionContract {
            override fun onClick(item: Any) {
                Timber.d("SPECIAL click: $item")
            }
        })
        binding.rcUpcoming.adapter = upComingAdapter
        binding.rcUpcoming.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.submit(dummyMovies)
        upComingAdapter.submit(mixeddummydata())
    }

    private fun mixeddummydata(): List<SuggestionListModel> =
        listOf(
            SuggestionListModel.HeaderItem("Today"),
            SuggestionListModel.MovieItem(Movie("Finding Nemo")),
            SuggestionListModel.MovieItem(Movie("Harry Potter")),
            SuggestionListModel.MovieItem(Movie("Deadpool")),
            SuggestionListModel.HeaderItem("Tomorrow"),
            SuggestionListModel.MovieItem(Movie("Jurassic Park")),
            SuggestionListModel.MovieItem(Movie("Forest Gump")),
            SuggestionListModel.HeaderItem("Next Week"),
            SuggestionListModel.MovieItem(Movie("Mall Cop")),
            SuggestionListModel.MovieItem(Movie("Miss Congeniality")),
            SuggestionListModel.MovieItem(Movie("Gladiator")),
            SuggestionListModel.MovieItem(Movie("Finding Dory")),
            SuggestionListModel.MovieItem(Movie("Shrek")),
            SuggestionListModel.MovieItem(Movie("Snow White"))
        )

    private fun populatedummydata(): List<Movie> =
        listOf<Movie>(
            Movie("Finding Nemo"),
            Movie("Harry Potter"),
            Movie("Deadpool"),
            Movie("Jurassic Park"),
            Movie("Forest Gump"),
            Movie("Mall Cop"),
            Movie("Miss Congeniality"),
            Movie("Gladiator"),
            Movie("Finding Dory")
        )
}