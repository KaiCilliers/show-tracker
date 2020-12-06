package com.sunrisekcdeveloper.showtracker.remote.source

import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.entities.network.ResponseWatcher

interface NetworkDataSource {
    suspend fun trendingMovies(): List<ResponseWatcher>
    suspend fun popularMovies(): List<ResponseMovie>
}