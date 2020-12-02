package com.sunrisekcdeveloper.showtracker.entities.domain

data class DetailedMovie(
    val basics: DisplayMovie,
    val releaseDate: String,
    val ageRating: String,
    val runningTime: String,
    val description: String,
    val notableActors: String,
    val director: String
)