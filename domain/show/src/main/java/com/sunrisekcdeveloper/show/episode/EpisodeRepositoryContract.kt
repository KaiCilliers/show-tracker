package com.sunrisekcdeveloper.show.episode

import com.sunrisekcdeveloper.show.season.Season
import kotlinx.coroutines.flow.Flow

interface EpisodeRepositoryContract {
    suspend fun get(showId: String, season: Int, episode: Int): Episode?
    suspend fun add(episode: Episode)
    suspend fun sync(showId: String, season: Int, episode: Int)
    suspend fun firstInSeason(showId: String, season: Int): Episode?
    suspend fun allInSeason(showId: String, season: Int): List<Episode?>
    suspend fun lastInSeason(showId: String, season: Int): Episode?
}