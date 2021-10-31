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

package com.sunrisekcdeveloper.progress.extras

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.sunrisekcdeveloper.cache.models.EntityEpisode
import com.sunrisekcdeveloper.cache.models.EntitySeason
import com.sunrisekcdeveloper.progress.extras.model.ResponseEpisode
import com.sunrisekcdeveloper.progress.extras.model.ResponseSeason
import kotlinx.coroutines.flow.Flow

fun ResponseEpisode.asEntityEpisode(showId: String) = EntityEpisode(
    showId = showId,
    seasonNumber = seasonNumber,
    number = number,
    name = name,
    overview = overview,
    airDate = -1L, // todo date string to date Long
    stillPath = stillPath ?: "",
    lastUpdated = java.lang.System.currentTimeMillis()
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
    lastUpdated = java.lang.System.currentTimeMillis()
)

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

// see more https://proandroiddev.com/android-singleliveevent-redux-with-kotlin-flow-b755c70bb055
inline fun <reified T> Flow<T>.observeOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    noinline collector: suspend (T) -> Unit
) = FlowObserver(lifecycleOwner, this, collector)

inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner
) = FlowObserver(lifecycleOwner, this, {})