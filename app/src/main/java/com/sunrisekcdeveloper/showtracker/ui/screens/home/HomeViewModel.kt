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

package com.sunrisekcdeveloper.showtracker.ui.screens.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunrisekcdeveloper.showtracker.entities.domain.FeaturedList
import com.sunrisekcdeveloper.showtracker.entities.domain.Movie

/**
 * Home ViewModel
 *
 * @constructor Create empty Home view model
 */
class HomeViewModel @ViewModelInject constructor() : ViewModel() {

    private val _featuredListData = MutableLiveData<List<FeaturedList>>()
    val featuredListData: LiveData<List<FeaturedList>>
        get() = _featuredListData

    init {
        _featuredListData.value = fakeFeaturedData()
    }

    private fun fakeFeaturedData(): List<FeaturedList> {
        return listOf(
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
}