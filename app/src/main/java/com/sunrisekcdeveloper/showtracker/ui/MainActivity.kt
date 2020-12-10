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

package com.sunrisekcdeveloper.showtracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.databinding.ActivityMainBinding
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseShow
import com.sunrisekcdeveloper.showtracker.remote.service.FanartService
import com.sunrisekcdeveloper.showtracker.remote.service.TraktService
import com.sunrisekcdeveloper.showtracker.ui.moreentities.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.Path
import timber.log.Timber

/**
 * Main activity Primary container for fragments that provide app functionality
 *
 * @constructor Create empty Main activity
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val traktService by lazy { TraktService.create() }
    val fanartService by lazy { FanartService.create() }

    private val ioScope by lazy { CoroutineScope(Dispatchers.IO) }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setup()
        ioScope.launch {
            go()
        }
    }

    private fun setup() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation_main)
        bottomNav?.setupWithNavController(findNavController(R.id.nav_host_fragment_main))
        // TODO this bottom nav bar needs to be gone when navigating to movie details fragment
//        bottomNav.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
//            override fun onNavigationItemSelected(item: MenuItem): Boolean {
//                when(item.itemId) {
//                    R.id.home_fragment_dest -> {binding.bottomNavigationMain.visibility = View.VISIBLE}
//                    R.id.progress_fragment_dest -> {binding.bottomNavigationMain.visibility = View.VISIBLE}
//                    R.id.search_fragment_dest -> {binding.bottomNavigationMain.visibility = View.VISIBLE}
//                    else -> {binding.bottomNavigationMain.visibility = View.GONE}
//                }
//                return true
//            }
//        })
    }

    /**
     * Go Temporary method to confirm results from remote services
     */
    suspend fun go() {
        /** MISCELLANEOUS */

//        Timber.d("========================MOVIE GENRES========================")
//        val movieGenres = traktService.genres("movies"); delay(2000)
//        Timber.d("========================SHOW GENRES========================")
//        val showGenres = traktService.genres("shows"); delay(2000)
//        Timber.d("========================MOVIE LANGUAGES========================")
//        val movieLangs = traktService.languages("movies"); delay(2000)
//        Timber.d("========================SHOW LANGUAGES========================")
//        val showLangs = traktService.languages("shows"); delay(2000)

        /** MOVIES */

//        Timber.d("========================TRENDING MOVIES========================")
//        val trending = traktService.trendingMovies(); delay(2000)
//        Timber.d("========================POPULAR MOVIES========================")
//        val popular = traktService.popularMovies(); delay(2000)
//        val recommendedWeekly = traktService.recommendedMovies()
//        val recommendedYearly = traktService.recommendedMovies("yearly")
//        val mostPlayedYearly = traktService.mostPlayedMovies("yearly")
//        val mostWatchedAll = traktService.mostWatchedMovies("all")
//        val anticipated = traktService.mostAnticipated()
//        val boxoffice = traktService.boxOffice()
//        val movie = traktService.movie(popular[4].identifiers.slug)
//        val aliases = traktService.movieAliases(popular[4].identifiers.slug)
//        val releases = traktService.movieReleases(popular[5].identifiers.slug, "au")
//        val translations = traktService.movieTranslations(popular[5].identifiers.slug,"ta")
//        val moviePeople = traktService.moviePersons(popular[4].identifiers.slug); delay(2000)
//        val ratings = traktService.movieRatings(popular[5].identifiers.slug)
//        val related = traktService.moviesRelatedTo(popular[5].identifiers.slug)
//        val stats = traktService.movieStats(popular[5].identifiers.slug)
//        val networks = traktService.networks()
//        val movieCreditsOnlyCast = traktService.movieCredits(moviePeople.cast[5].person.identifiers.slug)
//        val movieCreditsBoth = traktService.movieCredits("bryan-cranston")
//        val person = traktService.person(moviePeople.cast[8].person.identifiers.slug)
//        val trendingShows = traktService.trendingShows(); delay(4000)
        val popularShows = traktService.popularShows(); delay(4000)
        val recommendedShows = traktService.recommendedShows(); delay(4000)
//        val mostPlayedShows = traktService.mostPlayedShows("monthly"); delay(4000)
//        val mostWatcehdShows = traktService.mostWatchedShows("daily"); delay(4000)
//        val anticipatedShows = traktService.mostAnticipatedShows(); delay(4000)
//        val show = traktService.show(anticipatedShows[4].show.identifier.slug); delay(4000)
//        val titleAliases = traktService.showTitleAliases(anticipatedShows[8].show.identifier.slug); delay(4000)
//        val showCertification = traktService.showCertifications(anticipatedShows[4].show.identifier.slug); delay(4000)
//        val showTranslations = traktService.showTranslations(anticipatedShows[2].show.identifier.slug); delay(4000)
//        val showPeople = traktService.showPeople(recommendedShows[5].show.identifier.slug)
//        val showCreditsCast = traktService.showCredits(showPeople.cast[3].person.identifiers.slug)
//        val showCreditsCrew = showPeople.crew.directing?.get(0)?.person?.identifiers?.slug?.let {
//            traktService.showCredits(
//                it
//            )
//        }
        delay(5000)
//        val showRatings = traktService.showRatings(recommendedShows[0].show.identifier.slug); delay(4000)
//        val relatedShows = traktService.relatedShows(recommendedShows[1].show.identifier.slug); delay(4000)
//        val showStats = traktService.showStats(recommendedShows[2].show.identifier.slug); delay(4000)
//        val nextEpisode = traktService.showNextScheduledAirEpisode(recommendedShows[3].show.identifier.slug); delay(4000)
//        nextEpisode?.let {
//            if (it.isSuccessful) {
//                it.body()?.let {
//                    Timber.d("$it")
//                }
//            }
//        }
//        val lastEpisode = traktService.showMostRecentlyAiredEpisode(recommendedShows[4].show.identifier.slug); delay(4000)
//        val seasonEpisodes = traktService.seasonEpisodes(recommendedShows[5].show.identifier.slug, 1); delay(4000)
//        val showSeasons = traktService.seasonsOfShow(recommendedShows[6].show.identifier.slug); delay(4000)
//        val seasonPeople = traktService.seasonPeople(recommendedShows[7].show.identifier.slug, 1); delay(4000)
//        val seasonRatings = traktService.seasonRatings(recommendedShows[8].show.identifier.slug, 1); delay(4000)
//        val seasongStats = traktService.seasonStats(recommendedShows[9].show.identifier.slug, 1); delay(4000)
//        val episode = traktService.episode(popularShows[0].identifier.slug, 1, 1); delay(4000)
        val episodeTranslation = traktService.episodeTranslations(popularShows[1].identifier.slug, 1, 1); delay(4000)
        val episodePeople = traktService.episodePeople(popularShows[2].identifier.slug, 1, 1); delay(4000)
        val episodeRatings = traktService.episodeRatings(popularShows[3].identifier.slug, 1, 1); delay(4000)
        val episodeStats =traktService.episodeStats(popularShows[4].identifier.slug, 1, 1); delay(4000)

//        Timber.d("========================RESULT========================\n$person")
//        Timber.d("========================RESULT========================\n$movieCreditsBoth")

//        val single = trending[3]
//
//        println(fanartService.poster("${single.movie.identifiers.tmdb}"))
    }
}