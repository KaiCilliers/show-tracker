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

package com.sunrisekcdeveloper.watchlist.other

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
import com.sunrisekcdeveloper.watchlist.R
import com.sunrisekcdeveloper.watchlist.domain.model.UIModelWatchlistMovie
import com.sunrisekcdeveloper.watchlist.presentation.UIModelWatchlistShow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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