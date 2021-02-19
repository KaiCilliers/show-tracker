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

package com.sunrisekcdeveloper.showtracker.commons.util

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.sunrisekcdeveloper.showtracker.commons.models.local.*
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.DiscoveryPopularEntity
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.DiscoveryTopRatedEntity
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.DiscoveryUpcomingEntity
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.ResponseMovieTMDB
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.WatchListEntity
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.WatchListType
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.MediaModel
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.MediaModelSealed
import com.sunrisekcdeveloper.showtracker.models.local.core.MediaEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Models
fun DiscoveryPopularEntity.asMediaModel(type: MediaType): MediaModelSealed {
    return when (mediaType) {
        MediaType.MOVIE -> MediaModelSealed.MovieModel(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath?: "",
            backdropPath = backdropPath?: "",
            rating = rating,
            releaseDate = releaseDate?: "",
            watchListType = WatchListType.NONE
        )
        MediaType.SHOW -> MediaModelSealed.ShowModel(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath?: "",
            backdropPath = backdropPath?: "",
            rating = rating,
            releaseDate = releaseDate?: "",
            watchListType = WatchListType.NONE
        )
    }
}
fun DiscoveryTopRatedEntity.asMediaModel(type: MediaType): MediaModelSealed {
    return when (mediaType) {
        MediaType.MOVIE -> MediaModelSealed.MovieModel(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath?: "",
            backdropPath = backdropPath?: "",
            rating = rating,
            releaseDate = releaseDate?: "",
            watchListType = WatchListType.NONE
        )
        MediaType.SHOW -> MediaModelSealed.ShowModel(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath?: "",
            backdropPath = backdropPath?: "",
            rating = rating,
            releaseDate = releaseDate?: "",
            watchListType = WatchListType.NONE
        )
    }
}
fun DiscoveryUpcomingEntity.asMediaModel(type: MediaType): MediaModelSealed {
    return when (mediaType) {
        MediaType.MOVIE -> MediaModelSealed.MovieModel(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath?: "",
            backdropPath = backdropPath?: "",
            rating = rating,
            releaseDate = releaseDate?: "",
            watchListType = WatchListType.NONE
        )
        MediaType.SHOW -> MediaModelSealed.ShowModel(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath?: "",
            backdropPath = backdropPath?: "",
            rating = rating,
            releaseDate = releaseDate?: "",
            watchListType = WatchListType.NONE
        )
    }
}
fun MediaModelSealed.asDiscoveryPopularEntity() = DiscoveryPopularEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath?: "",
    backdropPath = backdropPath?: "",
    rating = rating,
    releaseDate = releaseDate?: "",
    mediaType = when (this) {
        is MediaModelSealed.ShowModel -> MediaType.SHOW
        is MediaModelSealed.MovieModel -> MediaType.MOVIE
    }
)
fun MediaModelSealed.asDiscoveryTopRatedEntity() = DiscoveryTopRatedEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath?: "",
    backdropPath = backdropPath?: "",
    rating = rating,
    releaseDate = releaseDate?: "",
    mediaType = when (this) {
        is MediaModelSealed.ShowModel -> MediaType.SHOW
        is MediaModelSealed.MovieModel -> MediaType.MOVIE
    }
)
fun MediaModelSealed.asDiscoveryUpcomingEntity() = DiscoveryUpcomingEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath?: "",
    backdropPath = backdropPath?: "",
    rating = rating,
    releaseDate = releaseDate?: "",
    mediaType = when (this) {
        is MediaModelSealed.ShowModel -> MediaType.SHOW
        is MediaModelSealed.MovieModel -> MediaType.MOVIE
    }
)
fun ResponseMovieTMDB.asMediaModel(mediaType: MediaType): MediaModelSealed {
    return when (mediaType) {
        MediaType.MOVIE -> MediaModelSealed.MovieModel(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath?: "",
            backdropPath = backdropPath?: "",
            rating = rating,
            releaseDate = releaseDate?: "",
            watchListType = WatchListType.NONE
        )
        MediaType.SHOW -> MediaModelSealed.ShowModel(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath?: "",
            backdropPath = backdropPath?: "",
            rating = rating,
            releaseDate = releaseDate?: "",
            watchListType = WatchListType.NONE
        )
    }
}
fun MediaModelSealed.MovieModel.asWatchListEntity() = WatchListEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate,
    mediaType = MediaType.MOVIE,
    watchListType = watchListType
)
fun ResponseMovieTMDB.asDomainMovieSealed(watchListType: WatchListType) = MediaModelSealed.MovieModel(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate,
    watchListType = watchListType
)
fun WatchListEntity.asDomainMovieSealed() = MediaModelSealed.MovieModel(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate,
    watchListType = watchListType
)

fun WatchListEntity.asDomainShowSealed() = MediaModelSealed.ShowModel(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate,
    watchListType = watchListType
)

fun RecentlyAddedMediaEntity.asDomainMovie() = MediaModel(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate
)

fun InProgressMediaEntity.asDomainMovie() = MediaModel(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate
)

fun UpcomingMediaEntity.asDomainMovie() = MediaModel(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate
)

fun CompletedMediaEntity.asDomainMovie() = MediaModel(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate
)

fun AnticipatedMediaEntity.asDomainMovie() = MediaModel(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate
)

fun ResponseMovieTMDB.asRecentlyAddedEntity() = RecentlyAddedMediaEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate
)

fun ResponseMovieTMDB.asMediaEntity(type: String) = MediaEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath ?: "",
    backdropPath = backdropPath ?: "",
    rating = rating,
    releaseDate = releaseDate?: "",
    type = type
)

fun MediaEntity.asRecentlyAddedMedia() = RecentlyAddedMediaEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate
)

fun MediaEntity.asCompletedMediaEntity() = CompletedMediaEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate
)

fun MediaEntity.asUpcomingMediaEntity() = UpcomingMediaEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate
)

fun MediaEntity.asAnticipatedMediaEntity() = AnticipatedMediaEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate
)

fun MediaEntity.asInProgressMediaEntity() = InProgressMediaEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate
)

fun RecentlyAddedMediaEntity.asResponseMovieTMDB() = ResponseMovieTMDB(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    rating = rating,
    releaseDate = releaseDate
)


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

/**
 * Subscribe simplifies subscribing to LiveData
 *
 * @param T data type that LiveData wraps
 * @param owner specifies when the observer should be terminated
 * @param action code to execute when LiveData's data changes
 * @receiver [LiveData]
 */
inline fun <T> LiveData<T>.subscribe(owner: LifecycleOwner, crossinline action: (T) -> Unit) =
    observe(owner, Observer { action(it) })