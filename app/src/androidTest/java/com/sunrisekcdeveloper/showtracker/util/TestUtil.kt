/*
 * Copyright © 2021. The Android Open Source Project
 *
 * @author Kai Cilliers
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sunrisekcdeveloper.showtracker.util

import com.sunrisekcdeveloper.showtracker.common.dao.relations.WatchlistMovieWithDetails
import com.sunrisekcdeveloper.showtracker.common.dao.relations.WatchlistShowWithDetails
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.*
import kotlin.random.Random

object TestUtil {
    fun createEntityMovie(
        id: Int = Random(Int.MAX_VALUE).nextInt(),
        title: String = "${Random(Int.MAX_VALUE).nextInt()}"
    ): EntityMovie {
        return EntityMovie(
            id = "$id",
            title = title,
            overview = "",
            posterPath = "",
            backdropPath = "",
            rating = 1f,
            popularityValue = 1f,
            certification = "",
            releaseDate = "",
            runTime = "",
            dateLastUpdated = -1L
        )
    }

    fun createEntityShow(
        id: Int = Random(Int.MAX_VALUE).nextInt(),
        title: String = "${Random(Int.MAX_VALUE).nextInt()}"
    ): EntityShow {
        return EntityShow(
            id = "$id",
            title = title,
            overview = "",
            certification = "",
            posterPath = "",
            backdropPath = "",
            popularityValue = 1f,
            firstAirDate = "",
            rating = 1f,
            episodeTotal = 1,
            seasonTotal = 1,
            lastUpdated = -1L
        )
    }

    fun createEntitySeason(
        showId: Int = Random(Int.MAX_VALUE).nextInt(),
        id: Int = Random(Int.MAX_VALUE).nextInt(),
        seasonNumber: Int = Random(Int.MAX_VALUE).nextInt(),
        name: String = ""
    ): EntitySeason {
        return EntitySeason(
            showId = "$showId",
            id = id,
            number = seasonNumber,
            name = name,
            overview = "",
            posterPath = "",
            airDate = 0L,
            episodeTotal = 0,
            lastUpdated = -1L
        )
    }

    fun createEntityEpisode(
        showId: Int = Random(Int.MAX_VALUE).nextInt(),
        seasonNumber: Int = Random(Int.MAX_VALUE).nextInt(),
        number: Int = Random(Int.MAX_VALUE).nextInt(),
        name: String = ""
    ): EntityEpisode {
        return EntityEpisode(
            showId = "$showId",
            seasonNumber = seasonNumber,
            number = number,
            name = name,
            airDate = 0L,
            overview = "",
            stillPath = "",
            lastUpdated = 0L
        )
    }

    fun createEntityWatchlistEpisode(
        showId: Int = Random(Int.MAX_VALUE).nextInt(),
        seasonNumber: Int = Random(Int.MAX_VALUE).nextInt(),
        number: Int = Random(Int.MAX_VALUE).nextInt(),
        lastUpdated: Long = Random(Long.MAX_VALUE).nextLong()
    ): EntityWatchlistEpisode {
        return EntityWatchlistEpisode(
            showId = "$showId",
            episodeNumber = number,
            seasonNumber = seasonNumber,
            watched = false,
            initialSetProgressBatch = false,
            viaUpToDateAction = false,
            dateWatched = 0L,
            onEpisodeSinceDate = 0L,
            lastUpdated = lastUpdated
        )
    }

    fun createEntityWatchlistSeason(
        showId: Int = Random(Int.MAX_VALUE).nextInt(),
        number: Int = Random(Int.MAX_VALUE).nextInt(),
        currentEpisode: Int = 0
    ): EntityWatchlistSeason {
        return EntityWatchlistSeason(
            showId = "$showId",
            number = number,
            dateStarted = 0L,
            dateCompleted = 0L,
            completed = false,
            currentEpisode = currentEpisode,
            startedTrackingSeason = false,
            finishedBeforeTracking = false,
            lastUpdated = 0L
        )
    }

    fun createEntityWatchlistShow(
        id: Int = Random(Int.MAX_VALUE).nextInt(),
        started: Boolean = false,
        deleted: Boolean = false,
        upToDate: Boolean = false,
        dateAdded: Long = 0L,
        lastUpdated: Long = 0L
    ): EntityWatchlistShow {
        return EntityWatchlistShow(
            id = "$id",
            currentEpisodeNumber = 0,
            currentEpisodeName = "",
            currentSeasonNumber = 0,
            currentSeasonEpisodeTotal = 0,
            started = started,
            upToDate = upToDate,
            deleted = deleted,
            dateDeleted = 0L,
            dateAdded = dateAdded,
            lastUpdated = lastUpdated
        )
    }

    fun createEntityWatchlistMovie(
        id: Int = Random(Int.MAX_VALUE).nextInt(),
        lastUpdated: Long = 0L,
        watched: Boolean = false,
        deleted: Boolean = false,
        dateAdded: Long = 0L
    ): EntityWatchlistMovie {
        return EntityWatchlistMovie(
            id = "$id",
            watched = watched,
            dateAdded = dateAdded,
            dateWatched = 0L,
            deleted = deleted,
            dateDeleted = 0L,
            dateLastUpdated = lastUpdated
        )
    }

    fun createWatchlistMovieWithDetails(
        id: Int = Random(Int.MAX_VALUE).nextInt(),
        title: String = "",
        watched: Boolean = false,
        dateAdded: Long = 0L
    ): WatchlistMovieWithDetails {
        return WatchlistMovieWithDetails(
            details = createEntityMovie(id = id, title = title),
            status = createEntityWatchlistMovie(id = id, watched = watched, dateAdded = dateAdded)
        )
    }

    fun createWatchlistShowWithDetails(
        id: Int = Random(Int.MAX_VALUE).nextInt(),
        title: String = "",
        started: Boolean = false,
        lastUpdated: Long = 0L,
        upToDate: Boolean = false,
        dateAdded: Long = 0L
    ): WatchlistShowWithDetails {
        return WatchlistShowWithDetails(
            details = createEntityShow(id = id, title = title),
            status = createEntityWatchlistShow(id = id, started = started, upToDate = upToDate, dateAdded = dateAdded, lastUpdated = lastUpdated)
        )
    }
}