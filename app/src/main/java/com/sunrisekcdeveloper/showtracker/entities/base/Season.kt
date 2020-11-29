package com.sunrisekcdeveloper.showtracker.entities.base

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.entities.base.Identifiers

data class Season(
    @Json(name = "number") val number: Int,
    @Json(name = "ids") val identifiers: Identifiers
)
