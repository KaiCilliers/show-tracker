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

package com.sunrisekcdeveloper.search.extras

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import com.sunrisekcdeveloper.cache.ListType
import com.sunrisekcdeveloper.cache.MediaType
import com.sunrisekcdeveloper.cache.UIModelDiscovery
import com.sunrisekcdeveloper.cache.dao.relations.WatchlistMovieWithDetails
import com.sunrisekcdeveloper.cache.dao.relations.WatchlistShowWithDetails
import com.sunrisekcdeveloper.movie.Movie
import com.sunrisekcdeveloper.movie.valueobjects.MovieMeta
import com.sunrisekcdeveloper.network.models.ResponseStandardMedia
import com.sunrisekcdeveloper.search.extras.model.UIModelPoster
import com.sunrisekcdeveloper.search.extras.model.UIModelSearch
import com.sunrisekcdeveloper.show.TVShow
import com.sunrisekcdeveloper.show.valueobjects.Identification
import com.sunrisekcdeveloper.show.valueobjects.ImageUrl
import com.sunrisekcdeveloper.show.valueobjects.Meta
import com.sunrisekcdeveloper.show.valueobjects.Stats
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
fun Context.hasConnection(): Boolean {
    val cm = this.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var connected = false
    cm.getNetworkCapabilities(cm.activeNetwork)?.let {
        connected = it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
    return connected
}
fun UIModelDiscovery.asUIModelPosterResult() = UIModelPoster(
    id = id,
    title = mediaTitle,
    posterPath = posterPath,
    // TODO: 06-04-2021 include backdrop path to UIModelDiscovery
    backdropPath = "",
    type = mediaType,
    listType = listType
)
fun UIModelSearch.asUiModelPosterResult() = UIModelPoster(
    id = id,
    title = title,
    type = mediaType,
    listType = ListType.MoviePopular,
    posterPath = posterPath,
    // TODO: 06-04-2021 include backdropPath in model
    backdropPath = ""
)

fun WatchlistMovieWithDetails.asUiModelPosterResult() = UIModelPoster(
    id = status.id,
    title = details.title,
    posterPath = details.posterPath,
    backdropPath = details.backdropPath,
    type = MediaType.Movie,
    listType = ListType.noList()
)

fun WatchlistShowWithDetails.asUiModelPosterResult() = UIModelPoster(
    id = status.id,
    title = details.title,
    posterPath = details.posterPath,
    backdropPath = details.backdropPath,
    type = MediaType.Show,
    listType = ListType.noList()
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
        identification = Identification(
            id = details.id,
            title = details.title,
            overview = details.overview
        ),
        images = ImageUrl(
            posterPath = details.posterPath,
            backdropPath = details.backdropPath
        ),
        stats = Stats(
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

fun Movie.toUiPosterModel(): UIModelPoster {
    return UIModelPoster(
        id = identification.id,
        title = identification.title,
        posterPath = images.poster,
        backdropPath = images.backdrop,
        type = MediaType.movie(),
        listType = ListType.noList()
    )
}

fun TVShow.toUiPosterModel(): UIModelPoster {
    return UIModelPoster(
        id = identification.id,
        title = identification.title,
        posterPath = images.posterPath,
        backdropPath = images.backdropPath,
        type = MediaType.show(),
        listType = ListType.noList()
    )
}

// see more https://proandroiddev.com/android-singleliveevent-redux-with-kotlin-flow-b755c70bb055
inline fun <reified T> Flow<T>.observeOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    noinline collector: suspend (T) -> Unit
) = FlowObserver(lifecycleOwner, this, collector)

inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner
) = FlowObserver(lifecycleOwner, this, {})