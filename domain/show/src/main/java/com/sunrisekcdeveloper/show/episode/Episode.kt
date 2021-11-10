package com.sunrisekcdeveloper.show.episode

data class Episode (
    val identification: Identification,
    val meta: Meta
)

data class Identification(
    val showId: String,
    val seasonNumber: Int,
    val number: Int,
    val name: String,
    val overview: String
    )
data class Meta(
    val airDate: String,
    val stillPath: String,
    val lastUpdated: Long
)