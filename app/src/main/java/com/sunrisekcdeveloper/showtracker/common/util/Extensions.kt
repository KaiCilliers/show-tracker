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

import android.view.View
import android.widget.SearchView
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseMovieDetail
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseShowDetail
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelMovieDetail
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelShowDetail
import com.sunrisekcdeveloper.showtracker.features.discovery.data.network.model.ResponseStandardMedia
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.ListType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelPoster
import com.sunrisekcdeveloper.showtracker.features.search.domain.domain.UIModelSearch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// todo save call to db then get entity to convert to UI
fun ResponseMovieDetail.asUIModelMovieDetail() = UIModelMovieDetail(
    id = "$id",
    title = title,
    posterPath = posterPath ?: "",
    overview = overview,
    releaseYear = releaseDate,
    certification = "N/A",
    runtime = "$runtime",
    watchlisted = false,
    watched = false,
    deleted = false
)
fun ResponseShowDetail.asUIModelShowDetail() = UIModelShowDetail(
    id = "$id",
    name = name,
    posterPath = posterPath?: "",
    overview = overview,
    certification = "N/A",
    firstAirDate = firstAirYear,
    seasonsTotal = seasonCount,
    watchlisted = false,
    startedWatching = false,
    upToDate = false
)
fun ResponseStandardMedia.ResponseMovie.asUIModelDiscovery(listType: ListType) = UIModelDiscovery(
    id = "$id",
    mediaType = MediaType.Movie,
    posterPath = posterPath ?: "",
    listType = listType
)
fun ResponseStandardMedia.ResponseShow.asUIModelDiscovery(listType: ListType) = UIModelDiscovery(
    id = "$id",
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
fun UIModelDiscovery.asUIModelPoster() = UIModelPoster(
    id = id,
    posterPath = posterPath,
    mediaType = mediaType
)
fun List<UIModelDiscovery>.asUIModelPosterList() = this.map { it.asUIModelPoster() }
fun UIModelSearch.asUIModelPoster() = UIModelPoster(
    id = id,
    posterPath = posterPath,
    mediaType = mediaType
)
fun List<UIModelSearch>.asUIModelPosterListt() = this.map { it.asUIModelPoster() }

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