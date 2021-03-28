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
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.ResponseStandardMedia
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ListType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.search.domain.model.UIModelSearch
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