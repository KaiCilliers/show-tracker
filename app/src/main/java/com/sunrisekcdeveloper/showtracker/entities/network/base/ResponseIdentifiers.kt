package com.sunrisekcdeveloper.showtracker.entities.network.base

import com.squareup.moshi.Json

/**
 * All the possible IDs assocaited with movies and shows
 * Not all entities have all the ID types, thus some
 * fields are nullable
 */
class ResponseIdentifiers(
    @Json(name = "trakt") val trakt: Int,
    @Json(name = "slug") val slug: String,
    @Json(name = "imdb") val imdb: String?,
    @Json(name = "tmdb") val tmdb: Int,
    @Json(name = "tvdb") val tvdb: Int?,
)