package com.sunrisekcdeveloper.showtracker.entities.network.base

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseIdentifiers

data class ResponseMovie(
        @Json(name = "title") val title: String,
        @Json(name = "year") val year: Int,
        @Json(name = "ids") val identifiers: ResponseIdentifiers
)