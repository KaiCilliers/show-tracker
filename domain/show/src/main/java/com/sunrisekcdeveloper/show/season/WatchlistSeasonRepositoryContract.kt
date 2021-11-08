package com.sunrisekcdeveloper.show.season

import kotlinx.coroutines.flow.Flow

interface WatchlistSeasonRepositoryContract {
    suspend fun get(showId: String, season: Int): WatchlistSeason?
    suspend fun update(season: WatchlistSeason)
    suspend fun add(season: WatchlistSeason)
}