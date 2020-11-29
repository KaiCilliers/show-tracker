package com.sunrisekcdeveloper.showtracker.remote.source

import com.sunrisekcdeveloper.showtracker.entities.base.Movie
import com.sunrisekcdeveloper.showtracker.entities.wrapper.WatcherResponse

interface NetworkDataSource {
    suspend fun trendingMovies(): List<WatcherResponse>
    suspend fun popularMovies(): List<Movie>
}