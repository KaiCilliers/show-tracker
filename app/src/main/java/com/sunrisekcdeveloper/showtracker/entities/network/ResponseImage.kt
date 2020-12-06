package com.sunrisekcdeveloper.showtracker.entities.network

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponsePoster

/**
 * TODO better name
 */
data class ResponseImage(
    @Json(name = "name") val name: String,
    @Json(name = "movieposter") val posters: List<ResponsePoster>
)