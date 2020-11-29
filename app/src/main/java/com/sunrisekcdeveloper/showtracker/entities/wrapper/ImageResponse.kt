package com.sunrisekcdeveloper.showtracker.entities.wrapper

import com.squareup.moshi.Json
import com.sunrisekcdeveloper.showtracker.entities.base.Poster

data class ImageResponse(
    @Json(name = "name") val name: String,
    @Json(name = "movieposter") val posters: List<Poster>
)