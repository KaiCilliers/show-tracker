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

package com.sunrisekcdeveloper.showtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sunrisekcdeveloper.showtracker.databinding.ActivityMain2Binding
import com.sunrisekcdeveloper.ui_components.Model
import com.sunrisekcdeveloper.ui_components.StMyListTvShow
import com.sunrisekcdeveloper.ui_components.StRatingIndicator
import com.sunrisekcdeveloper.ui_components.StSubList
import com.sunrisekcdeveloper.ui_components.databinding.StRatingIndicatorBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private var progresss = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mine.setMax(100)
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                while (progresss < 100) {
                    go()
                }
            }
        }
        binding.ratingGuy.setRating(8.4f)
        binding.genreList.setHeading("Genres")
        binding.genreList.setData(listOf(
            "Action",
            "Adventure",
            "Horror",
            "Comedy",
            "Crime",
            "Thriller",
            "Suspense",
        )) { view, string ->
            Toast.makeText(view.context, string, Toast.LENGTH_SHORT).show()
        }
        (binding.myListTvShowMain as StMyListTvShow).submitData(listOf(
            Model(
                posterUrl = "",
                showTitle = "The Blacklist",
                episodeTitle = "The Deer Hunter (No.42)",
                amountWatched = 80,
                totalEpisodes = 400
            ),
            Model(
                posterUrl = "",
                showTitle = "Game of Thrones",
                episodeTitle = "Wall of Death",
                amountWatched = 22,
                totalEpisodes = 64
            ),
            Model(
                posterUrl = "",
                showTitle = "The Office",
                episodeTitle = "Pam's artwork",
                amountWatched = 69,
                totalEpisodes = 74
            ),
            Model(
                posterUrl = "",
                showTitle = "Avatar The Last Airbender",
                episodeTitle = "Book 3: Where is Appa?",
                amountWatched = 57,
                totalEpisodes = 89

            )
        ))
        binding.collapsableTextMain.setTheText("this is a long text, very long. I want to get it to three lines, but I think I wont make it. Maybe i will, You never know hey! Hahaha my goofness can we reach three lines already. I am not egetting any younger here. Good we reached 4 lines! I would like to get to five and end it off there. Nice")
        binding.collapsableTextMain.post(Runnable {
            binding.collapsableTextMain.getIngo()
        })
    }

    suspend fun go() {
        progresss = progresss + 1
        binding.mine.setProgress(progresss)
        delay(150)
        if(progresss == 100) {progresss = 0}
    }

}