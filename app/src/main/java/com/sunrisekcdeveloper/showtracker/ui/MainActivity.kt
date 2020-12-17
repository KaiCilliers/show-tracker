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
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.data.local.MovieDao
import com.sunrisekcdeveloper.showtracker.data.local.model.categories.PopularListEntity
import com.sunrisekcdeveloper.showtracker.databinding.ActivityMainBinding
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.Trakt
import com.sunrisekcdeveloper.showtracker.data.network.NetworkDataSource
import com.sunrisekcdeveloper.showtracker.di.MainActivityModule.MainRepo
import com.sunrisekcdeveloper.showtracker.repository.RepositoryContract
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Main activity Primary container for fragments that provide app functionality
 *
 * @constructor Create empty Main activity
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject @Trakt lateinit var traktService: NetworkDataSource
    @Inject @MainRepo lateinit var repo: RepositoryContract
    @Inject lateinit var dao: MovieDao

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

        dao.test()
        // Trending movies
        val trendingData = traktService.trendingMovies()
        val movies = trendingData.map { it.movie!!.asEntity() }
        val trendings = trendingData.map { it.asTrendingMovieEntity() }

        // Popular Movies
        val popularData = traktService.popularMovies()
        val popMov = popularData.map { it.asEntity() }
        val pops = popularData.map { PopularListEntity.from(it) }

        // All types of recommended movies
        val recDataDaily = traktService.recommendedMovies("daily")
        val recDayMov = recDataDaily.map { it.movie!!.asEntity() }
        val recDaily = recDataDaily.map { it.asRecommendedMovie("daily") }

        val recDataWeekly = traktService.recommendedMovies()
        val recWeekMov = recDataWeekly.map { it.movie!!.asEntity() }
        val recWeek = recDataWeekly.map { it.asRecommendedMovie("weekly") }

        val recDataMonthly = traktService.recommendedMovies("monthly")
        val recMonthMov = recDataMonthly.map { it.movie!!.asEntity() }
        val recMonth = recDataMonthly.map { it.asRecommendedMovie("monthly") }

        val recDataYearly = traktService.recommendedMovies("yearly")
        val recYearMov = recDataYearly.map { it.movie!!.asEntity() }
        val recYear = recDataYearly.map { it.asRecommendedMovie("yearly") }

        Timber.d("GOT ALL THE DATA - DELAY BEFORE INSERTING DATA TO DATABASE")
        delay(10000)
        Timber.d("Incoming")
        delay(5000)

        dao.insertMovie(*popMov.toTypedArray())
        dao.insertMovie(*recDayMov.toTypedArray())
        dao.insertMovie(*recWeekMov.toTypedArray())
        dao.insertMovie(*recMonthMov.toTypedArray())
        dao.insertMovie(*recYearMov.toTypedArray())
        dao.insertMovie(*movies.toTypedArray())

        Timber.d("INSERTED MOVIES")
        delay(6000)

        dao.upsertTrendingMedia(*trendings.toTypedArray())
        dao.upsertPopularMedia(*pops.toTypedArray())
        dao.upsertRecommendedMedia(*recWeek.toTypedArray())
        dao.upsertRecommendedMedia(*recDaily.toTypedArray())
        dao.upsertRecommendedMedia(*recMonth.toTypedArray())
        dao.upsertRecommendedMedia(*recYear.toTypedArray())

        Timber.d("DONE")

//        delay(8000)
//        val daily = traktService.recommendedMovies("daily", "full")
//        val weekly = traktService.recommendedMovies("weekly", "full")
//        val monthly = traktService.recommendedMovies("monthly", "full")
//        val yearly = traktService.recommendedMovies("yearly", "full")
//        val all = traktService.recommendedMovies("all", "full")
//        delay(8000)
//
//        Timber.d("daily=========================================================================================")
//        daily.forEach {
//            Timber.d("${it.movie} user_count are ${it.userCount}")
//        }
//        Timber.d("weekly=========================================================================================")
//        weekly.forEach {
//            Timber.d("${it.movie} user_count are ${it.userCount}")
//        }
//        Timber.d("monthy=========================================================================================")
//        monthly.forEach {
//            Timber.d("${it.movie} user_count are ${it.userCount}")
//        }
//        Timber.d("yearlyy=========================================================================================")
//        yearly.forEach {
//            Timber.d("${it.movie} user_count are ${it.userCount}")
//        }
//        Timber.d("all=========================================================================================")
//        all.forEach {
//            Timber.d("${it.movie} user_count are ${it.userCount}")
//        }

//        val genreMovie = traktService.genres("movies")
//        val genreShow = traktService.genres("shows")
//        delay(8000)
//        genreMovie.forEach {movie ->
//            genreShow.forEach { show ->
//                if (movie.slug == show.slug) {
//                    Timber.d("Matching slugs")
//                    if (movie.name == show.name) {
//                        Timber.d("GOOD match")
//                    } else {
//                        Timber.d("BAD match")
//                    }
//                }
//            }
//        }

//        val movies = traktService.countries("movies")
//        val shows = traktService.countries("shows")
//        delay(8000)
//        movies.forEach { movie ->
//            shows.forEach { show ->
//                if (movie.code == show.code) {
//                    Timber.d("We've got a match!")
//                    Timber.d("Movie code: ${movie.code} is equal to Show code: ${show.code}")
//                    if (movie.name == show.name) {
//                        Timber.d("GOOD: Movie value: ${movie.name} is equal to Show value: ${show.name}")
//                    } else {
//                        Timber.d("BAD: Movie value: ${movie.name} is NOT equal to Show value: ${show.name}")
//                    }
//                }
//            }
//        }

//        dao.findMovie()
////        dao.clear()
//        val popular = traktService.popularMovies()
//        val trending = traktService.trendingMovies()
//        val anticipated = traktService.mostAnticipated()
//
//        Timber.d("FETCHED ALL NETWORK SOURCES")
//        delay(8000)
//
//        val pop = popular.map { it.asEntity() }
//        dao.insert(*pop.toTypedArray())
//        Timber.d("INSERTED FIRST SET OF DATA")
//        delay(15000)
//        Timber.d("GOING TO NEXT STEP IN 5 seconds")
//        delay(5000)
//
//        val trend = trending.map { it.movie!!.asEntity() }
//        dao.insert(*trend.toTypedArray())
//        Timber.d("INSERTED SECOND SET OF DATA")
//        delay(15000)
//        Timber.d("GOING TO NEXT STEP IN 5 seconds")
//        delay(5000)
//
//        val ternd2 = trending.map { it.asMovieEntity() }
//        dao.insertTrendingList(*ternd2.toTypedArray())
//        Timber.d("INSERTED TRENDING TABLE SET OF DATA")
//        delay(15000)
//        Timber.d("GOING TO NEXT STEP IN 5 seconds")
//        delay(5000)
//
//        val ant = anticipated.map { it.movie!!.asEntity() }
//        dao.insert(*ant.toTypedArray())
//        Timber.d("INSERTED THIRD SET OF DATA")
//        delay(15000)
//        Timber.d("GOING TO NEXT STEP IN 5 seconds")
//        delay(5000)
//
//        val res = dao.trendingMovies()
//        Timber.d("$res")



//        dao2.clearDog()
//        dao2.clearOwner()
//
//        dao2.insertOwners(
//            Owner("one", "James"),
//            Owner("two", "Peter"),
//            Owner("three", "Alex"),
//            Owner("four", "Jack")
//        )
//        Timber.d("inserted owners")
//        delay(10000)
//        dao2.insertDogs(
//            Dog(10, "one", "doggo1", 1414),
//            Dog(11, "four", "doggo2", 1333),
//            Dog(12, "two", "doggo3", 1661),
//            Dog(13, "three", "doggo4", 1999)
//        )
//        Timber.d("insered doggos")
//        delay(10000)
//
//        val res = dao2.getDogsAndOwners()
//        Timber.d("$res")

//        dao2.clearMovieTest()
//        dao2.clearTrendingTest()

//        val pop = popular.map { it.asTest() }
//        dao2.insertMovies(*pop.toTypedArray())
//
//
//        Timber.d("pop: ${dao2.allMovieTest()}")
//        delay(10000)
//
//        val ant = anticipated.map { it.movie!!.asTest() }
//        dao2.insertMovies(*ant.toTypedArray())
//
//        Timber.d("ant: ${dao2.allMovieTest()}")
//        delay(10000)
////
//        val trend = trending.map { it.asMovieEntityTest() }
//        val trendMov = trending.map { it.movie!!.asTest() }
//
//
//        delay(10000)
//
//        dao2.insertTrending(*trend.toTypedArray())
//        dao2.insertMovies(*trendMov.toTypedArray())
//
//        Timber.d("both")
//        Timber.d("${dao2.allMovieTest()}")
//        delay(5000)
//        Timber.d("${dao2.allTrendingTest()}")
//        delay(5000)
//
//        val resss = dao2.myWay()
//        Timber.d("/n/n/n\n\n\n\n$resss")




//        val s = repo.trendingMovies()
//        repo.popularMovies()
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
//        val popularShows = traktService.popularShows(); delay(4000)
//        val recommendedShows = traktService.recommendedShows(); delay(4000)
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
//        delay(5000)
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
//        val episodeTranslation = traktService.episodeTranslations(popularShows[1].identifier.slug, 1, 1); delay(4000)
//        val episodePeople = traktService.episodePeople(popularShows[2].identifier.slug, 1, 1); delay(4000)
//        val episodeRatings = traktService.episodeRatings(popularShows[3].identifier.slug, 1, 1); delay(4000)
//        val episodeStats =traktService.episodeStats(popularShows[4].identifier.slug, 1, 1); delay(4000)

//        val s = traktService.mostAnticipatedShows(); delay(1000)
//        fanartService.poster("10195")


//        traktService.genres("movies"); delay(3000)
//        traktService.languages("shows"); delay(3000)
//        traktService.networks(); delay(3000)

//        traktService.trendingMovies(); delay(3000)
//        val s = traktService.popularMovies(); delay(3000)
//        traktService.recommendedMovies(); delay(3000)
//        traktService.mostPlayedMovies(); delay(3000)
//        traktService.mostWatchedMovies(); delay(3000)
//        traktService.mostAnticipated(); delay(3000)
//        traktService.boxOffice(); delay(3000)
//        traktService.movie(s[0].identifiers.slug); delay(3000)
//        traktService.movieAliases(s[1].identifiers.slug); delay(3000)
//        traktService.movieReleases(s[2].identifiers.slug); delay(3000)
//        traktService.movieTranslations(s[3].identifiers.slug); delay(3000)
//        val p = traktService.moviePersons(s[4].identifiers.slug); delay(3000)
//        traktService.movieRatings(s[5].identifiers.slug); delay(3000)
//        traktService.moviesRelatedTo(s[6].identifiers.slug); delay(3000)
//        traktService.movieStats(s[7].identifiers.slug); delay(3000)

//        traktService.trendingShows(); delay(3000)
//        val ss = traktService.popularShows(); delay(3000)
//        val dd = traktService.recommendedShows(); delay(3000)
//        traktService.mostPlayedShows(); delay(3000)
//        traktService.mostWatchedShows(); delay(3000)
//        traktService.mostAnticipatedShows(); delay(3000)
//        traktService.show(ss[0].identifiers.slug); delay(3000)
//        traktService.showTitleAliases(ss[1].identifiers.slug); delay(3000)
//        traktService.showCertifications(ss[2].identifiers.slug); delay(3000)
//        traktService.showTranslations(ss[3].identifiers.slug); delay(3000)
//        traktService.showPeople(ss[4].identifiers.slug); delay(3000)
//        traktService.showRatings(ss[5].identifiers.slug); delay(3000)
//        traktService.relatedShows(ss[6].identifiers.slug); delay(3000)
//        traktService.showStats(ss[7].identifiers.slug); delay(3000)
//        traktService.showNextScheduledAirEpisode(ss[8].identifiers.slug); delay(3000)
//        traktService.showMostRecentlyAiredEpisode(ss[9].identifiers.slug); delay(3000)

//        val d = dd[2].show?.identifiers?.slug!!
//        traktService.seasonEpisodes(d, 1); delay(3000)
//        traktService.seasonsOfShow(d); delay(3000)
//        traktService.seasonPeople(d, 1); delay(3000)
//        traktService.seasonRatings(d, 1); delay(3000)
//        traktService.seasonStats(d, 1); delay(3000)
//
//        traktService.episode(d, 1, 1); delay(3000)
//        traktService.episodeTranslations(d, 1, 1); delay(3000)
//        traktService.episodePeople(d, 1, 1); delay(3000)
//        traktService.episodeRatings(d, 1, 1); delay(3000)
//        traktService.episodeStats(d, 1, 1); delay(3000)
//
//        val pp = p.cast[0].person?.identifiers?.slug!!
//        val ppp = p.crew.directing?.get(0)?.person?.identifiers?.slug!!
//        traktService.person(pp); delay(3000)
//        traktService.movieCredits(pp); delay(3000)
//        traktService.showCredits(ppp)
//
//        Timber.d("=======================================++DONE")

//        Timber.d("========================RESULT========================\n$person")
//        Timber.d("========================RESULT========================\n$movieCreditsBoth")

//        val single = trending[3]
//
//        println(fanartService.poster("${single.movie.identifiers.tmdb}"))
    }
}