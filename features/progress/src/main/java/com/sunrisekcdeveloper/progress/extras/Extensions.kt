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

package com.sunrisekcdeveloper.progress.extras

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.sunrisekcdeveloper.cache.dao.relations.WatchlistShowWithDetails
import com.sunrisekcdeveloper.cache.models.*
import com.sunrisekcdeveloper.progress.extras.model.*
import com.sunrisekcdeveloper.show.FullTVShow
import com.sunrisekcdeveloper.show.TVShow
import com.sunrisekcdeveloper.show.TVShowWithSeasons
import com.sunrisekcdeveloper.show.episode.Episode
import com.sunrisekcdeveloper.show.episode.Identification
import com.sunrisekcdeveloper.show.episode.Meta
import com.sunrisekcdeveloper.show.episode.WatchlistEpisode
import com.sunrisekcdeveloper.show.episode.WatchlistMeta
import com.sunrisekcdeveloper.show.episode.WatchlistStatus
import com.sunrisekcdeveloper.show.season.*
import com.sunrisekcdeveloper.show.valueobjects.WatchlistTVShow
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

fun TVShow.toEntity(): EntityShow {
    return EntityShow(
        id = identification.id,
        title = identification.title,
        overview = identification.overview,
        certification = stats.certification,
        posterPath = images.posterPath,
        backdropPath = images.backdropPath,
        popularityValue = stats.popularityValue,
        firstAirDate = meta.firstAirDate,
        rating = stats.rating,
        episodeTotal = stats.episodeTotal,
        seasonTotal = stats.seasonTotal,
        lastUpdated = meta.lastUpdated
    )
}

fun ResponseShowDetail.asEntityShow(certification: String = "N/A") = EntityShow(
    id = "$id",
    title = name,
    overview = overview,
    certification = certification,
    posterPath = posterPath ?: "",
    backdropPath = backdropPath ?: "",
    popularityValue = popularityValue,
    firstAirDate = firstAirYear,
    rating = rating,
    episodeTotal = episodeCount,
    seasonTotal = seasonCount,
    lastUpdated = java.lang.System.currentTimeMillis()
)

fun WatchlistShowWithDetails.toTVShowDomain(): TVShow {
    return TVShow(
        identification = com.sunrisekcdeveloper.show.valueobjects.Identification(
            id = details.id,
            title = details.title,
            overview = details.overview
        ),
        images = com.sunrisekcdeveloper.show.valueobjects.ImageUrl(
            posterPath = details.posterPath,
            backdropPath = details.backdropPath
        ),
        stats = com.sunrisekcdeveloper.show.valueobjects.Stats(
            rating = details.rating,
            certification = details.certification,
            popularityValue = details.popularityValue,
            episodeTotal = details.episodeTotal,
            seasonTotal = details.seasonTotal
        ),
        meta = com.sunrisekcdeveloper.show.valueobjects.Meta(
            firstAirDate = details.firstAirDate,
            lastUpdated = details.lastUpdated
        )
    )
}

fun WatchlistShowWithDetails.toDomain(): FullTVShow {
    return FullTVShow(
        details = TVShow(
            identification = com.sunrisekcdeveloper.show.valueobjects.Identification(
                id = details.id,
                title = details.title,
                overview = details.overview
            ),
            images = com.sunrisekcdeveloper.show.valueobjects.ImageUrl(
                posterPath = details.posterPath,
                backdropPath = details.backdropPath
            ),
            stats = com.sunrisekcdeveloper.show.valueobjects.Stats(
                rating = details.rating,
                certification = details.certification,
                popularityValue = details.popularityValue,
                episodeTotal = details.episodeTotal,
                seasonTotal = details.seasonTotal
            ),
            meta = com.sunrisekcdeveloper.show.valueobjects.Meta(
                firstAirDate = details.firstAirDate,
                lastUpdated = details.lastUpdated
            )
        ),
        status = WatchlistTVShow(
            id = details.id,
            status = com.sunrisekcdeveloper.show.valueobjects.Status(
                currentEpisodeNumber = status.currentEpisodeNumber,
                currentEpisodeName = status.currentEpisodeName,
                currentSeasonNumber = status.currentSeasonNumber,
                currentSeasonEpisodeTotal = status.currentSeasonEpisodeTotal,
                started = status.started,
                upToDate = status.upToDate,
                deleted = status.deleted
            ),
            meta = com.sunrisekcdeveloper.show.valueobjects.WatchlistMeta(
                dateDeleted = status.dateDeleted,
                dateAdded = status.dateAdded,
                lastUpdated = status.lastUpdated
            )
        )
    )
}

fun WatchlistTVShow.toEntity(): EntityWatchlistShow {
    return EntityWatchlistShow(
        id = id,
        currentEpisodeNumber = status.currentEpisodeNumber,
        currentEpisodeName = status.currentEpisodeName,
        currentSeasonNumber = status.currentSeasonNumber,
        currentSeasonEpisodeTotal = status.currentSeasonEpisodeTotal,
        started = status.started,
        upToDate = status.upToDate,
        deleted = status.deleted,
        dateDeleted = meta.dateDeleted,
        dateAdded = meta.dateAdded,
        lastUpdated = meta.lastUpdated
    )
}

fun WatchlistSeason.toEntity(): EntityWatchlistSeason {
    return EntityWatchlistSeason(
        showId = showId,
        number = status.number,
        dateStarted = meta.dateStarted,
        completed = status.completed,
        dateCompleted = meta.dateCompleted,
        currentEpisode = status.currentEpisode,
        startedTrackingSeason = meta.startedTrackingSeason,
        finishedBeforeTracking = meta.finishedBeforeTracking,
        lastUpdated = meta.lastUpdated
    )
}

fun WatchlistEpisode.toEntity(): EntityWatchlistEpisode {
    return EntityWatchlistEpisode(
        showId = showId,
        episodeNumber = status.episodeNumber,
        seasonNumber = status.seasonNumber,
        watched = status.watched,
        initialSetProgressBatch = meta.initialSetProgressBatch,
        viaUpToDateAction = meta.viaUpToDateAction,
        dateWatched = meta.dateWatched,
        onEpisodeSinceDate = meta.onEpisodeSinceDate,
        lastUpdated = meta.lastUpdated
    )
}

fun ResponseShowDetailWithSeasons.toDomain(showId: String, certification: String): TVShowWithSeasons {
    return TVShowWithSeasons(
        show = TVShow(
            identification = com.sunrisekcdeveloper.show.valueobjects.Identification(
                id = id.toString(),
                title = name,
                overview = overview
            ),
            images = com.sunrisekcdeveloper.show.valueobjects.ImageUrl(
                posterPath = posterPath.orEmpty(),
                backdropPath = backdropPath.orEmpty()
            ),
            stats = com.sunrisekcdeveloper.show.valueobjects.Stats(
                rating = rating,
                certification = certification,
                popularityValue = popularityValue,
                episodeTotal = episodeCount,
                seasonTotal = seasonCount
            ),
            meta = com.sunrisekcdeveloper.show.valueobjects.Meta(
                firstAirDate = firstAirYear,
                lastUpdated = java.lang.System.currentTimeMillis()
            )
        ),
        seasons = seasons.map { it.toDomain(showId) }
    )
}

fun EntityShow.toDomain(): TVShow {
    return TVShow(
        identification = com.sunrisekcdeveloper.show.valueobjects.Identification(id, title, overview),
        stats = com.sunrisekcdeveloper.show.valueobjects.Stats(
            rating,
            certification,
            popularityValue,
            episodeTotal,
            seasonTotal
        ),
        images = com.sunrisekcdeveloper.show.valueobjects.ImageUrl(posterPath, backdropPath),
        meta = com.sunrisekcdeveloper.show.valueobjects.Meta(firstAirDate, lastUpdated)
    )
}

fun EntitySeason.toDomain(): Season {
    return Season(
        identification = com.sunrisekcdeveloper.show.season.Identification(showId, id.toString(), name, overview),
        images = ImageUrl(posterPath),
        stats = Stats(number, airDate.toDate(), episodeTotal),
        meta = com.sunrisekcdeveloper.show.season.Meta(lastUpdated)
    )
}

fun Long.toDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("yyyy.MM.dd")
    return format.format(date)
}

fun ResponseSeason.toDomain(showId: String): Season {
    return Season(
        identification = com.sunrisekcdeveloper.show.season.Identification(
            showId,
            id.toString(),
            name,
            overview
        ),
        images = ImageUrl(posterPath.orEmpty()),
        stats = Stats(number, dateAired.orEmpty(), episodeCount),
        meta = com.sunrisekcdeveloper.show.season.Meta(System.currentTimeMillis())
    )
}

fun ResponseSeasonDetailWithEpisodes.toDomain(showId: String): SeasonWithEpisodes {
    return SeasonWithEpisodes(
        season = Season(
            identification = com.sunrisekcdeveloper.show.season.Identification(
                showId = showId,
                id = id.toString(),
                name = name,
                overview = overview
            ),
            images = ImageUrl(
                posterPath = posterPath.orEmpty(),
            ),
            stats = Stats(
                number = number,
                airDate = dateAired.orEmpty(),
                episodeTotal = episodes.size
            ),
            meta = com.sunrisekcdeveloper.show.season.Meta(lastUpdated = java.lang.System.currentTimeMillis())
        ),
        episodes = episodes.map { it.toDomain(showId) }
    )
}

fun EntityWatchlistShow.toDomain(): WatchlistTVShow {
    return WatchlistTVShow(
        id = id,
        status = com.sunrisekcdeveloper.show.valueobjects.Status(
            currentEpisodeNumber,
            currentEpisodeName,
            currentSeasonNumber,
            currentSeasonEpisodeTotal,
            started,
            upToDate,
            deleted
        ),
        meta = com.sunrisekcdeveloper.show.valueobjects.WatchlistMeta(dateDeleted, dateAdded, lastUpdated)
    )
}

fun EntityWatchlistEpisode.toDomain(): WatchlistEpisode {
    return WatchlistEpisode(
        showId = showId,
        status = WatchlistStatus(episodeNumber, seasonNumber, watched),
        meta = WatchlistMeta(initialSetProgressBatch, viaUpToDateAction, dateWatched, onEpisodeSinceDate, lastUpdated)
    )
}
fun EntityWatchlistSeason.toDomain(): WatchlistSeason {
    return WatchlistSeason(
        showId = showId,
        status = Status(number, completed, currentEpisode),
        meta = WatchlistMeta(
            dateStarted, dateCompleted, startedTrackingSeason, finishedBeforeTracking, lastUpdated
        )
    )
}

fun Episode.toEntity(): EntityEpisode {
    return EntityEpisode(
        showId = identification.showId,
        seasonNumber = identification.seasonNumber,
        number = identification.number,
        name = identification.name,
        airDate = -1L,
        overview = identification.overview,
        stillPath = meta.stillPath,
        lastUpdated = meta.lastUpdated
    )
}

fun Season.toEntity(): EntitySeason {
    return EntitySeason(
        showId = identification.showId,
        id = identification.id.toInt(),
        number = stats.number,
        name = identification.name,
        overview = identification.overview,
        posterPath = images.posterPath.orEmpty(),
        airDate = -1L,
        episodeTotal = stats.episodeTotal,
        lastUpdated = meta.lastUpdated
    )
}

fun EntityEpisode.toDomain(): Episode {
    return Episode(
        identification = Identification(showId, seasonNumber, number, name, overview),
        meta = Meta(airDate.toDate(), stillPath, lastUpdated)
    )
}

fun ResponseEpisode.toDomain(showId: String): Episode {
    return Episode(
        identification = Identification(showId, seasonNumber, number, name, overview),
        meta = Meta(
            airDate = dateAired.orEmpty(),
            stillPath = stillPath.orEmpty(),
            lastUpdated = System.currentTimeMillis()
        )
    )
}

fun ResponseEpisode.asEntityEpisode(showId: String) = EntityEpisode(
    showId = showId,
    seasonNumber = seasonNumber,
    number = number,
    name = name,
    overview = overview,
    airDate = -1L, // todo date string to date Long
    stillPath = stillPath ?: "",
    lastUpdated = java.lang.System.currentTimeMillis()
)

fun ResponseSeason.asEntitySeason(showId: String) = EntitySeason(
    showId = showId,
    id = id,
    number = number,
    name = name,
    overview = overview,
    posterPath = posterPath ?: "",
    airDate = -1L, // todo conversion function to take string date and return Long version
    episodeTotal = episodeCount,
    lastUpdated = java.lang.System.currentTimeMillis()
)

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

// see more https://proandroiddev.com/android-singleliveevent-redux-with-kotlin-flow-b755c70bb055
inline fun <reified T> Flow<T>.observeOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    noinline collector: suspend (T) -> Unit
) = FlowObserver(lifecycleOwner, this, collector)

inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner
) = FlowObserver(lifecycleOwner, this, {})