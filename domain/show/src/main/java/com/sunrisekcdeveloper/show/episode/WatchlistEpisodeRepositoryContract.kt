package com.sunrisekcdeveloper.show.episode

interface WatchlistEpisodeRepositoryContract {
    suspend fun get(showId: String, episode: Int, season: Int): WatchlistEpisode?
    suspend fun update(episode: WatchlistEpisode)
    suspend fun add(episode: WatchlistEpisode)
}