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

package com.sunrisekcdeveloper.detail

import android.content.Context
import android.content.res.TypedArray
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.sunrisekcdeveloper.cache.models.EntityMovie
import com.sunrisekcdeveloper.cache.models.EntityShow
import com.sunrisekcdeveloper.detail.data.model.ResponseMovieDetail
import com.sunrisekcdeveloper.detail.data.model.ResponseShowDetail
import com.sunrisekcdeveloper.detail.domain.model.*
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