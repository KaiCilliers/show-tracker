package com.sunrisekcdeveloper.showtracker.entities.domain

data class Movie(
    val title: String,
    val episode: String = "${(1..55).random()}",
    val season: String = "${(1..14).random()}",
    val episodeTitle: String = "Pilot: The best default title",
    val image: String = "https://source.unsplash.com/random"
)