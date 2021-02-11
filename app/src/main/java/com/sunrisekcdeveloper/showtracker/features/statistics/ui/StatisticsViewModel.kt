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

package com.sunrisekcdeveloper.showtracker.features.statistics.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.FeaturedList
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie

class StatisticsViewModel @ViewModelInject constructor() : ViewModel() {

    val featured = liveData {
        emit(fakeFeaturedData())
    }

    private fun fakeFeaturedData(): List<FeaturedList> {
        return listOf(
            FeaturedList(heading = "My Horror", results = fakeMovieData()),
            FeaturedList(heading = "My Comedy", results = fakeMovieData()),
            FeaturedList(heading = "My Adventure", results = fakeMovieData())
        )
    }

    private fun fakeMovieData(): List<Movie> {
        return listOf<Movie>(
            Movie("Finding Nemo", "myslug"),
            Movie("Harry Potter", "myslug"),
            Movie("Deadpool", "myslug"),
            Movie("Jurassic Park", "myslug"),
            Movie("Forest Gump", "myslug"),
            Movie("Mall Cop", "myslug"),
            Movie("Miss Congeniality", "myslug"),
            Movie("Gladiator", "myslug"),
            Movie("Finding Dory", "myslug")
        )
    }
}