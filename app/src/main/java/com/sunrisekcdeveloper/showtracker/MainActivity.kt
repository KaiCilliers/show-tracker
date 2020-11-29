package com.sunrisekcdeveloper.showtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sunrisekcdeveloper.showtracker.remote.service.FanartService
import com.sunrisekcdeveloper.showtracker.remote.service.TraktService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val traktService  by lazy { TraktService.create() }
    val fanartService by lazy { FanartService.create() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CoroutineScope(Dispatchers.IO).launch {
            go()
        }
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