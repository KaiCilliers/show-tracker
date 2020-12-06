package com.sunrisekcdeveloper.showtracker.entities.network.base

import com.squareup.moshi.Json

data class ResponseEpisode(
    @Json(name = "season") val season: Int,
    @Json(name = "number") val number: Int,
    @Json(name = "title") val title: String,
    @Json(name = "ids") val identifiers: ResponseIdentifiers
)