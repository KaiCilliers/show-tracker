package com.sunrisekcdeveloper.showtracker.entities.domain

/**
 * This is just for dummy data purposes
 * TODO rename to just movie
 */
data class DisplayMovie(
    val title: String,
    val episode: String = "${(1..55).random()}",
    val season: String = "${(1..14).random()}",
    val episodeTitle: String = "Pilot: The best default title",
    val image: String = "https://source.unsplash.com/random"
)