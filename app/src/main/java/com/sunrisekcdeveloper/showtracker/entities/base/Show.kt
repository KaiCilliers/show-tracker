package com.sunrisekcdeveloper.showtracker.entities.base

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.entities.base.Identifiers

data class Show(
    @Json(name = "title") val title: String,
    @Json(name = "year") val year: Int,
    @Json(name = "ids") val identifier: Identifiers
)
