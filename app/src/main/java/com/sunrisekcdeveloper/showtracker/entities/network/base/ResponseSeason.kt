package com.sunrisekcdeveloper.showtracker.entities.network.base

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseIdentifiers

data class ResponseSeason(
    @Json(name = "number") val number: Int,
    @Json(name = "ids") val identifiers: ResponseIdentifiers
)
