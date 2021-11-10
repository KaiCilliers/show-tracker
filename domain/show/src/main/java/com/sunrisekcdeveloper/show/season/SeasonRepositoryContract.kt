package com.sunrisekcdeveloper.show.season

interface SeasonRepositoryContract {
    suspend fun get(showId: String, season: Int): Season?
    suspend fun add(season: Season)
    suspend fun sync(showId: String, season: Int)
    suspend fun lastInShow(showId: String): Season?
    suspend fun seasonWithEpisodes(showId: String, season: Int): SeasonWithEpisodes?
    suspend fun allFromShow(showId: String): List<Season>
}