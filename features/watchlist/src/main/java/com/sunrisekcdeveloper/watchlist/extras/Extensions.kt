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

package com.sunrisekcdeveloper.watchlist.extras

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import com.sunrisekcdeveloper.cache.dao.relations.WatchlistMovieWithDetails
import com.sunrisekcdeveloper.cache.dao.relations.WatchlistShowWithDetails
import com.sunrisekcdeveloper.cache.models.*
import com.sunrisekcdeveloper.movie.FullMovie
import com.sunrisekcdeveloper.movie.Movie
import com.sunrisekcdeveloper.movie.WatchlistMovie
import com.sunrisekcdeveloper.movie.valueobjects.MovieMeta
import com.sunrisekcdeveloper.movie.valueobjects.MovieStatus
import com.sunrisekcdeveloper.show.FullTVShow
import com.sunrisekcdeveloper.show.TVShow
import com.sunrisekcdeveloper.show.episode.*
import com.sunrisekcdeveloper.show.episode.Identification
import com.sunrisekcdeveloper.show.episode.Meta
import com.sunrisekcdeveloper.show.episode.WatchlistMeta
import com.sunrisekcdeveloper.show.season.*
import com.sunrisekcdeveloper.show.valueobjects.WatchlistTVShow
import com.sunrisekcdeveloper.watchlist.R
import com.sunrisekcdeveloper.watchlist.extras.model.UIModelWatchlistMovie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*


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
        meta = com.sunrisekcdeveloper.show.valueobjects.Meta(
            firstAirDate = details.firstAirDate,
            lastUpdated = details.lastUpdated
        )
    )
}

// todo add lsat episode in season to EntityWatchlistSeason and/or EntitySeason
fun WatchlistShowWithDetails.asUIModelWatchlistShow(lastEpisodeInSeason: Int) =
    UIModelWatchlistShow(
        id = details.id,
        title = details.title,
        posterPath = details.posterPath,
        currentEpisodeNumber = status.currentEpisodeNumber,
        currentEpisodeName = status.currentEpisodeName,
        currentSeasonNumber = status.currentSeasonNumber,
        episodesInSeason = status.currentSeasonEpisodeTotal,
        lastEpisodeInSeason = lastEpisodeInSeason,
        started = status.started,
        upToDate = status.upToDate,
        dateAdded = status.dateAdded
    )

fun WatchlistMovieWithDetails.asUIModelWatchlistMovie() = UIModelWatchlistMovie(
    id = details.id,
    title = details.title,
    overview = details.overview,
    posterPath = details.posterPath,
    watched = status.watched,
    dateAdded = status.dateAdded,
    dateWatched = status.dateWatched,
    lastUpdated = status.dateLastUpdated
)

// TODO [E07-002] [Mappers] For each domain object we need a mapper. Basic conversions are network -> domain, domain -> entity, entity -> domain. Some mappers can also be placed in a common module
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

fun WatchlistMovieWithDetails.toDomain(): FullMovie {
    return FullMovie(
        details = Movie(
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
        ),
        status = WatchlistMovie(
            id = details.id,
            status = MovieStatus(
                watched = status.watched,
                deleted = status.deleted
            ),
            meta = com.sunrisekcdeveloper.movie.valueobjects.WatchlistMeta(
                dateAdded = status.dateAdded,
                dateLastUpdated = status.dateLastUpdated,
                dateWatched = status.dateWatched,
                dateDeleted = status.dateDeleted
            )
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

fun EntityWatchlistMovie.toDomain(): WatchlistMovie {
    return WatchlistMovie(
        id = id,
        status = MovieStatus(watched, deleted),
        meta = com.sunrisekcdeveloper.movie.valueobjects.WatchlistMeta(dateAdded, dateLastUpdated, dateWatched, dateDeleted)
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

fun FullMovie.toWatchlistMovieUi(): UIModelWatchlistMovie {
    return UIModelWatchlistMovie(
        id = details.identification.id,
        title = details.identification.title,
        overview = details.identification.overview,
        posterPath = details.images.poster,
        watched = status.status.watched,
        dateAdded = status.meta.dateAdded,
        dateWatched = status.meta.dateWatched,
        lastUpdated = status.meta.dateLastUpdated
    )
}

fun FullTVShow.toWatchlistShowUi(lastEpisodeInSeason: Int): UIModelWatchlistShow {
    return UIModelWatchlistShow(
        id = details.identification.id,
        title = details.identification.title,
        posterPath = details.images.posterPath,
        currentEpisodeName = status.status.currentEpisodeName,
        currentEpisodeNumber = status.status.currentEpisodeNumber,
        currentSeasonNumber = status.status.currentSeasonNumber,
        episodesInSeason = status.status.currentSeasonEpisodeTotal,
        lastEpisodeInSeason = lastEpisodeInSeason,
        started = status.status.started,
        upToDate = status.status.upToDate,
        dateAdded = status.meta.dateAdded
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
        meta = com.sunrisekcdeveloper.show.season.WatchlistMeta(
            dateStarted, dateCompleted, startedTrackingSeason, finishedBeforeTracking, lastUpdated
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

fun EntityEpisode.toDomain(): Episode {
    return Episode(
        identification = Identification(showId, seasonNumber, number, name, overview),
        meta = Meta(airDate.toDate(), stillPath, lastUpdated)
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
fun ResponseEpisode.toEntity(showId: String): EntityEpisode {
    return EntityEpisode(
        showId = showId,
        seasonNumber = seasonNumber,
        number = number,
        name = name,
        airDate = -1L,
        overview = overview,
        stillPath = stillPath ?: "",
        lastUpdated = System.currentTimeMillis()
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

fun Season.toEntity(): EntitySeason {
    return EntitySeason(
        showId = identification.showId,
        id = identification.id.toInt(),
        number = stats.number,
        name = identification.name,
        overview = identification.overview,
        posterPath = images.posterPath,
        airDate = -1L, // I need to store dates as strings not long
        episodeTotal = stats.episodeTotal,
        lastUpdated = meta.lastUpdated
    )
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
        stats = Stats(number, dateAired, episodeCount),
        meta = com.sunrisekcdeveloper.show.season.Meta(System.currentTimeMillis())
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
            meta = com.sunrisekcdeveloper.show.season.Meta(lastUpdated = System.currentTimeMillis())
        ),
        episodes = episodes.map { it.toDomain(showId) }
    )
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

fun View.hideKeyboard(context: Context, rootView: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
    rootView.requestFocus()
}


// see more https://proandroiddev.com/android-singleliveevent-redux-with-kotlin-flow-b755c70bb055
inline fun <reified T> Flow<T>.observeOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    noinline collector: suspend (T) -> Unit
) = FlowObserver(lifecycleOwner, this, collector)

inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner
) = FlowObserver(lifecycleOwner, this, {})

@ExperimentalCoroutinesApi
fun SearchView.getQueryTextChangedStateFlow(): StateFlow<String> {
    val query = MutableStateFlow("")

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = true

        override fun onQueryTextChange(newText: String?): Boolean {
            newText?.let {
                query.value = newText
            }
            return true
        }
    })

    return query
}