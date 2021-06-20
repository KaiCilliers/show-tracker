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

package com.sunrisekcdeveloper.discovery

import androidx.lifecycle.LifecycleOwner
import com.sunrisekcdeveloper.cache.ListType
import com.sunrisekcdeveloper.cache.MediaType
import com.sunrisekcdeveloper.cache.UIModelDiscovery
import com.sunrisekcdeveloper.discovery.data.network.model.ResponseStandardMedia
import kotlinx.coroutines.flow.Flow

fun ResponseStandardMedia.ResponseMovie.asUIModelDiscovery(listType: ListType) = UIModelDiscovery(
    id = "$id",
    mediaTitle = title,
    mediaType = MediaType.movie(),
    posterPath = posterPath ?: "",
    listType = listType
)

fun ResponseStandardMedia.ResponseShow.asUIModelDiscovery(listType: ListType) = UIModelDiscovery(
    id = "$id",
    mediaTitle = name,
    mediaType = MediaType.show(),
    posterPath = posterPath ?: "",
    listType = listType
)
fun UIModelDiscovery.asUIModelPosterResult() = UIModelPoster(
    id = id,
    title = mediaTitle,
    posterPath = posterPath,
    // TODO: 06-04-2021 include backdrop path to UIModelDiscovery
    backdropPath = "",
    type = mediaType,
    listType = listType
)
// see more https://proandroiddev.com/android-singleliveevent-redux-with-kotlin-flow-b755c70bb055
inline fun <reified T> Flow<T>.observeOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    noinline collector: suspend (T) -> Unit
) = FlowObserver(lifecycleOwner, this, collector)

inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner
) = FlowObserver(lifecycleOwner, this, {})