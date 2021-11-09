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

package com.sunrisekcdeveloper.detail.extras

import android.content.Context
import android.content.res.TypedArray
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.sunrisekcdeveloper.cache.dao.relations.WatchlistMovieWithDetails
import com.sunrisekcdeveloper.cache.dao.relations.WatchlistShowWithDetails
import com.sunrisekcdeveloper.cache.models.EntityMovie
import com.sunrisekcdeveloper.cache.models.EntityShow
import com.sunrisekcdeveloper.cache.models.EntityWatchlistMovie
import com.sunrisekcdeveloper.cache.models.EntityWatchlistShow
import com.sunrisekcdeveloper.detail.R
import com.sunrisekcdeveloper.detail.extras.model.*
import com.sunrisekcdeveloper.movie.*
import com.sunrisekcdeveloper.movie.valueobjects.*
import com.sunrisekcdeveloper.show.FullTVShow
import com.sunrisekcdeveloper.show.valueobjects.Meta
import com.sunrisekcdeveloper.show.valueobjects.Status
import com.sunrisekcdeveloper.show.TVShow
import com.sunrisekcdeveloper.show.valueobjects.WatchlistTVShow
import kotlinx.coroutines.flow.Flow

fun ResponseMovieDetail.asEntityMovie(certification: String = "N/A") = EntityMovie(
    id = "$id",
    title = title,
    overview = overview,
    backdropPath = backdropPath ?: "",
    posterPath = posterPath ?: "",
    certification = certification,
    releaseDate = releaseDate,
    runTime = "$runtime",
    rating = rating,
    popularityValue = popularityValue,
)
fun EntityMovie.asUIModelMovieDetail(status: MovieWatchlistStatus, watched: WatchedStatus) =
    UIModelMovieDetail(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview,
        releaseYear = releaseDate.substring(0..3),
        certification = certification,
        runtime = runTime,
        status = status,
        watched = watched
    )

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

fun EntityShow.asUIModelShowDetail(
    watchlist: ShowWatchlistStatus,
    status: ShowStatus
) = UIModelShowDetail(
    id = id,
    name = title,
    posterPath = posterPath,
    overview = overview,
    certification = certification,
    firstAirDate = firstAirDate.substring(0..3),
    seasonsTotal = seasonTotal,
    watchlist = watchlist,
    status = status
)

fun EntityWatchlistMovie.toDomain(): WatchlistMovie {
    return WatchlistMovie(
        id = id,
        status = MovieStatus(watched, deleted),
        meta = WatchlistMeta(dateAdded, dateLastUpdated, dateWatched, dateDeleted)
    )
}

fun WatchlistMovieWithDetails.toDomain(): FullMovie {
    return FullMovie(
        details = Movie(
            identification = Identification(
                id = details.id,
                title = details.title,
                overview = details.overview
            ),
            images = ImageUrl(
                poster = details.posterPath,
                backdrop = details.backdropPath
            ),
            stats = Stats(
                rating = details.rating,
                popularity = details.popularityValue
            ),
            meta = MovieMeta(
                certification = details.certification,
                releaseDate = details.releaseDate,
                runtime = details.runTime
            )
        ),
        status = WatchlistMovie(
            id = details.id,
            status = MovieStatus(
                watched = status.watched,
                deleted = status.deleted
            ),
            meta = WatchlistMeta(
                dateAdded = status.dateAdded,
                dateLastUpdated = status.dateLastUpdated,
                dateWatched = status.dateWatched,
                dateDeleted = status.dateDeleted
            )
        )
    )
}

fun WatchlistMovie.toEntity(): EntityWatchlistMovie {
    return EntityWatchlistMovie(
        id = id,
        watched = status.watched,
        dateAdded = meta.dateAdded,
        dateWatched = meta.dateWatched,
        deleted = status.deleted,
        dateDeleted = meta.dateDeleted,
        dateLastUpdated = meta.dateLastUpdated
    )
}
fun EntityMovie.toDomain(): Movie {
    return Movie(
        identification = Identification(id, title, overview),
        images = ImageUrl(posterPath, backdropPath),
        stats = Stats(rating, popularityValue),
        meta = MovieMeta(certification, releaseDate, runTime)
    )
}
fun Movie.toEntity(): EntityMovie {
    return EntityMovie(
        id = identification.id,
        title = identification.title,
        overview = identification.overview,
        posterPath = images.poster,
        backdropPath = images.backdrop,
        rating = stats.rating,
        popularityValue = stats.popularity,
        certification = meta.certification,
        releaseDate = meta.releaseDate,
        runTime = meta.runtime
    )
}

fun Movie.asUIModel(status: MovieWatchlistStatus, watched: WatchedStatus): UIModelMovieDetail {
    return UIModelMovieDetail(
        id = identification.id,
        title = identification.title,
        overview = identification.overview,
        posterPath = images.poster,
        releaseYear = meta.releaseDate,
        certification = meta.certification,
        runtime = meta.runtime,
        status = status,
        watched = watched
    )
}

fun EntityWatchlistShow.toDomain(): WatchlistTVShow {
    return WatchlistTVShow(
        id = id,
        status = Status(currentEpisodeNumber, currentEpisodeName, currentSeasonNumber, currentSeasonEpisodeTotal, started, upToDate, deleted),
        meta = com.sunrisekcdeveloper.show.valueobjects.WatchlistMeta(dateDeleted, dateAdded, lastUpdated)
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
            meta = Meta(
                firstAirDate = details.firstAirDate,
                lastUpdated = details.lastUpdated
            )
        ),
        status = WatchlistTVShow(
            id = details.id,
            status = Status(
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
        meta = Meta(firstAirDate, lastUpdated)
    )
}

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

fun TVShow.toUiModel(watchlist: ShowWatchlistStatus,
                     status: ShowStatus): UIModelShowDetail {
    return UIModelShowDetail(
        id = identification.id,
        name = identification.title,
        posterPath = images.posterPath,
        overview = identification.overview,
        certification = stats.certification,
        firstAirDate = meta.firstAirDate,
        seasonsTotal = stats.seasonTotal,
        watchlist = watchlist,
        status = status
    )
}

fun WatchlistMovieWithDetails.toMovieDomain(): Movie {
    return Movie(
        identification = com.sunrisekcdeveloper.movie.valueobjects.Identification(
            id = details.id,
            title = details.title,
            overview = details.overview
        ),
        images = com.sunrisekcdeveloper.movie.valueobjects.ImageUrl(
            poster = details.posterPath,
            backdrop = details.backdropPath
        ),
        stats = com.sunrisekcdeveloper.movie.Stats(
            rating = details.rating,
            popularity = details.popularityValue
        ),
        meta = MovieMeta(
            certification = details.certification,
            releaseDate = details.releaseDate,
            runtime = details.runTime
        )
    )
}

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
        meta = Meta(
            firstAirDate = details.firstAirDate,
            lastUpdated = details.lastUpdated
        )
    )
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.enabled() {
    this.isEnabled = true
}

fun View.disabled() {
    this.isEnabled = false
}

fun TextView.setMaxLinesToEllipsize() {
    val visibleLines = (measuredHeight - paddingTop - paddingBottom) / lineHeight
    maxLines = visibleLines
}

// hacky way of changing button colors depending on the action it represents
fun fetchPrimaryColor(context: Context): Int {
    val typedValue = TypedValue()
    val a: TypedArray =
        context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorPrimary))
    val color = a.getColor(0, 0)
    a.recycle()
    return color
}

fun fetchErrorColor(context: Context): Int {
    val typedValue = TypedValue()
    val a: TypedArray =
        context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorError))
    val color = a.getColor(0, 0)
    a.recycle()
    return color
}

// see more https://proandroiddev.com/android-singleliveevent-redux-with-kotlin-flow-b755c70bb055
inline fun <reified T> Flow<T>.observeOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    noinline collector: suspend (T) -> Unit
) = FlowObserver(lifecycleOwner, this, collector)

inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner
) = FlowObserver(lifecycleOwner, this, {})