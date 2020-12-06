package com.sunrisekcdeveloper.showtracker.entities.network.base

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseIdentifiers

data class ResponseShow(
    @Json(name = "title") val title: String,
    @Json(name = "year") val year: Int,
    @Json(name = "ids") val identifier: ResponseIdentifiers
)
