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
import com.sunrisekcdeveloper.showtracker.features.discover.data.local.model.RecentlyAddedMediaEntity
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.ResponseMovieTMDB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Models
fun ResponseMovieTMDB.asRecentlyAddedEntity() = RecentlyAddedMediaEntity(
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