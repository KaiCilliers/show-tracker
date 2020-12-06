package com.sunrisekcdeveloper.showtracker.entities.domain

data class DetailedMovie(
    val basics: Movie,
    val releaseDate: String,
    val ageRating: String,
    val runningTime: String,
    val description: String,
    val notableActors: String,
    val director: String
)