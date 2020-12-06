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
import com.sunrisekcdeveloper.showtracker.databinding.FragmentHomeBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.Movie
import com.sunrisekcdeveloper.showtracker.entities.domain.FeaturedList
import com.sunrisekcdeveloper.showtracker.ui.components.adapters.impl.SuggestionListAdapter
import timber.log.Timber

/**
 * Home Fragment displays suggested movies and shows for the user to add to their watchlist,
 * categorised with appropriate headings
 *
 * @constructor Create empty Home fragment
 */
class HomeFragment : Fragment() {

    private val adapter by lazy {
        SuggestionListAdapter(object : ClickActionContract {
            override fun onClick(item: Any) {
                Timber.d("Featured: $item")
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentDestToDetailFragment("FROM HOME FRAGMENT")
                )
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.rcFeaturedCategories.adapter = adapter
        binding.rcFeaturedCategories.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.submit(populateFeaturedDummy())
    }

    private fun populateFeaturedDummy(): List<FeaturedList> =
        listOf(
            FeaturedList(
                "Featured",
                listOf<Movie>(
                    Movie("ONE ONE"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "New Releases",
                listOf<Movie>(
                    Movie("TWO TWO"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "Comedy",
                listOf<Movie>(
                    Movie("THREE THREE"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "Watchlist",
                listOf<Movie>(
                    Movie("FOUR FOUR"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "For you",
                listOf<Movie>(
                    Movie("FIVE FIVE"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "Horror",
                listOf<Movie>(
                    Movie("SIX SIX"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "Beacuse you watched Breaking Bad",
                listOf<Movie>(
                    Movie("SEVEN SEVEN"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "Featured",
                listOf<Movie>(
                    Movie("EIGHT EIGHT"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "New Releases",
                listOf<Movie>(
                    Movie("NINE NINE"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "Comedy",
                listOf<Movie>(
                    Movie("TEN TEN"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "Watchlist",
                listOf<Movie>(
                    Movie("ELEVEN ELEVEN"),
                    Movie("Harry Potter"),
                    Movie("Deadpool"),
                    Movie("Jurassic Park"),
                    Movie("Forest Gump"),
                    Movie("Mall Cop"),
                    Movie("Miss Congeniality"),
                    Movie("Gladiator"),
                    Movie("Finding Dory")
                )
            ),
            FeaturedList(
                "For you",
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
            ),
            FeaturedList(
                "Horror",
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
            ),
            FeaturedList(
                "Beacuse you watched Breaking Bad",
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
            )
        )

}