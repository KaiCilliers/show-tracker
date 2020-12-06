package com.sunrisekcdeveloper.showtracker.entities.network.base

import com.squareup.moshi.Json

data class ResponsePoster(
    @Json(name = "id") val id: String,
    @Json(name = "url") val url: String,
    @Json(name = "lang") val lang: String,
    @Json(name = "likes") val likes: String
)