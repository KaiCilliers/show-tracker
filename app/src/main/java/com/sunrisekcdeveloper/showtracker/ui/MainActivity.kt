package com.sunrisekcdeveloper.showtracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.remote.service.FanartService
import com.sunrisekcdeveloper.showtracker.remote.service.TraktService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    val traktService by lazy { TraktService.create() }
    val fanartService by lazy { FanartService.create() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation_main)
        bottomNav?.setupWithNavController(findNavController(R.id.nav_host_fragment_main))
    }

    suspend fun go() {
        val res = traktService.trendingMovies()
        val res2 = traktService.popularMovies()
        println(res)
        println(res2)

        val single = res[0]

        println(fanartService.poster("${single.movie.identifiers.tmdb}"))
    }
}