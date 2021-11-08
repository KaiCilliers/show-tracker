package com.sunrisekcdeveloper.show.season

interface SeasonRepositoryContract {
    suspend fun get(showId: String, season: Int): Season?
    suspend fun add(season: Season)
    suspend fun sync(showId: String, season: Int)
}