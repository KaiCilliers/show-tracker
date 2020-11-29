package com.sunrisekcdeveloper.showtracker.entities.wrapper

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.entities.base.Movie

data class WatcherResponse(
    @Json(name = "watchers") val watchers: Int,
    @Json(name = "movie") val movie: Movie
)