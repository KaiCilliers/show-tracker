package com.sunrisekcdeveloper.showtracker.remote.source

import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.entities.network.ResponseWatcher
import retrofit2.http.GET
import retrofit2.http.Headers

interface TraktDataSource : NetworkDataSource {
    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/trending")
    override suspend fun trendingMovies(): List<ResponseWatcher>
    @Headers(
        "Content-Type: application/json",
        "trakt-api-key: 62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1",
        "trakt-api-version: 2"
    )
    @GET("movies/popular")
    override suspend fun popularMovies(): List<ResponseMovie>
}