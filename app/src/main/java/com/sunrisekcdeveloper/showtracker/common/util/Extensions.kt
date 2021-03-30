/*
 * Copyright © 2020. The Android Open Source Project
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

package com.sunrisekcdeveloper.showtracker.common.util

import android.content.Context
import android.content.res.TypedArray
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.dao.relations.WatchlistMovieWithDetails
import com.sunrisekcdeveloper.showtracker.common.dao.relations.WatchlistShowWithDetails
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseMovieDetail
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseShowDetail
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelMovieDetail
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelShowDetail
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.ResponseStandardMedia
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ListType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.progress.data.model.ResponseEpisode
import com.sunrisekcdeveloper.showtracker.features.progress.data.model.ResponseSeason
import com.sunrisekcdeveloper.showtracker.features.search.domain.model.UIModelSearch
import com.sunrisekcdeveloper.showtracker.features.search.domain.model.UIModelUnwatchedSearch
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityEpisode
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntitySeason
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityShow
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.UIModelWatchlisMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.UIModelWatchlistShow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

fun isDateSame(c1: Calendar, c2: Calendar): Boolean {
    return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(
        Calendar.MONTH
    ) && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
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

fun ResponseStandardMedia.ResponseMovie.asUIModelDiscovery(listType: ListType) = UIModelDiscovery(
    id = "$id",
    mediaTitle = title,
    mediaType = MediaType.Movie,
    posterPath = posterPath ?: "",
    listType = listType
)

fun ResponseStandardMedia.ResponseShow.asUIModelDiscovery(listType: ListType) = UIModelDiscovery(
    id = "$id",
    mediaTitle = name,
    mediaType = MediaType.Show,
    posterPath = posterPath ?: "",
    listType = listType
)

fun EntityShow.asUIModelShowDetail(
    watchlisted: Boolean = false,
    started: Boolean = false,
    upToDate: Boolean = false,
    deleted: Boolean
) = UIModelShowDetail(
    id = id,
    name = title,
    posterPath = posterPath,
    overview = overview,
    certification = certification,
    firstAirDate = firstAirDate,
    seasonsTotal = seasonTotal,
    deleted = deleted,
    watchlisted = watchlisted,
    startedWatching = started,
    upToDate = upToDate
)

fun ResponseShowDetail.asEntityShow() = EntityShow(
    id = "$id",
    title = name,
    overview = overview,
    certification = "N/A",
    posterPath = posterPath ?: "",
    backdropPath = backdropPath ?: "",
    popularityValue = popularityValue,
    firstAirDate = firstAirYear,
    rating = rating,
    episodeTotal = episodeCount,
    seasonTotal = seasonCount,
    lastUpdated = System.currentTimeMillis()
)

fun ResponseMovieDetail.asEntityMovie() = EntityMovie(
    id = "$id",
    title = title,
    overview = overview,
    backdropPath = backdropPath ?: "",
    posterPath = posterPath ?: "",
    certification = "",
    releaseDate = releaseDate,
    runTime = "$runtime",
    rating = rating,
    popularityValue = popularityValue,
)

fun EntityMovie.asUIModelMovieDetail(watchlisted: Boolean, watched: Boolean, deleted: Boolean) =
    UIModelMovieDetail(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview,
        releaseYear = releaseDate,
        certification = certification,
        runtime = runTime,
        deleted = deleted,
        watchlisted = watchlisted,
        watched = watched
    )

fun ResponseEpisode.asEntityEpisode(showId: String) = EntityEpisode(
    showId = showId,
    seasonNumber = seasonNumber,
    number = number,
    name = name,
    overview = overview,
    airDate = -1L, // todo date string to date Long
    stillPath = stillPath ?: "",
    lastUpdated = System.currentTimeMillis()
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
    lastUpdated = System.currentTimeMillis()
)

fun ResponseStandardMedia.ResponseMovie.asUIModelSearch() = UIModelSearch(
    id = "$id",
    title = title,
    mediaType = MediaType.Movie,
    posterPath = posterPath ?: "",
    rating = rating,
    popularity = popularity,
    ratingVotes = voteCount
)

fun ResponseStandardMedia.ResponseShow.asUIModelSearch() = UIModelSearch(
    id = "$id",
    title = name,
    mediaType = MediaType.Show,
    posterPath = posterPath ?: "",
    rating = rating,
    popularity = popularity,
    ratingVotes = voteCount
)

fun UIModelSearch.asUIModelDiscovery() = UIModelDiscovery(
    id = id,
    mediaTitle = title,
    mediaType = mediaType,
    listType = ListType.MoviePopular,
    posterPath = posterPath
)

fun WatchlistMovieWithDetails.asUiModelUnwatchedSearch() = UIModelUnwatchedSearch(
    id = status.id,
    title = details.title,
    posterPath = details.posterPath,
    backdropPath = details.backdropPath,
    mediaType = MediaType.Movie
)

fun WatchlistShowWithDetails.asUiModelUnwatchedSearch() = UIModelUnwatchedSearch(
    id = status.id,
    title = details.title,
    posterPath = details.posterPath,
    backdropPath = details.backdropPath,
    mediaType = MediaType.Show
)

fun WatchlistShowWithDetails.asUIModelWatchlistShow() = UIModelWatchlistShow(
    id = details.id,
    title = details.title,
    posterPath = details.posterPath,
    currentEpisodeNumber = status.currentEpisodeNumber,
    currentEpisodeName = status.currentEpisodeName,
    currentSeasonNumber = status.currentSeasonNumber,
    episodesInSeason = status.currentSeasonEpisodeTotal,
    started = status.started,
    upToDate = status.upToDate,
    dateAdded = status.dateAdded
)

fun WatchlistMovieWithDetails.asUIModelWatchlistMovie() = UIModelWatchlisMovie(
    id = details.id,
    title = details.title,
    overview = details.overview,
    posterPath = details.posterPath,
    watched = status.watched,
    dateAdded = status.dateAdded,
    dateWatched = status.dateWatched,
    lastUpdated = status.dateLastUpdated
)

fun TextView.setMaxLinesToEllipsize() {
    val visibleLines = (measuredHeight - paddingTop - paddingBottom) / lineHeight
    maxLines = visibleLines
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

/**
 * Click simplifies adding an onClickListener to a [View]
 *
 * @param action lambda expression to be called when [View] is clicked
 * @receiver [View]
 */
inline fun View.click(crossinline action: () -> Unit) = setOnClickListener { action() }