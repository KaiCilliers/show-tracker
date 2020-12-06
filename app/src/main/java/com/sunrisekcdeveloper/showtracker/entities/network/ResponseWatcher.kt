package com.sunrisekcdeveloper.showtracker.entities.network

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseMovie

/**
 * TODO better name
 */
data class ResponseWatcher(
    @Json(name = "watchers") val watchers: Int,
    @Json(name = "movie") val movie: ResponseMovie
)