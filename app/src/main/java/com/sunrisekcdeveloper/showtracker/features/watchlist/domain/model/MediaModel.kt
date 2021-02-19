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

package com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model

import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.WatchListType

data class MediaModel(
    val id: Long,
    val title: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val rating: Float,
    val releaseDate: String
)

sealed class MediaModelSealed(
    open val id: Long,
    open val title: String,
    open val overview: String,
    open val posterPath: String,
    open val backdropPath: String,
    open val rating: Float,
    open val releaseDate: String,
    open val watchListType: WatchListType
) {
    data class ShowModel(
        override val id: Long,
        override val title: String,
        override val overview: String,
        override val posterPath: String,
        override val backdropPath: String,
        override val rating: Float,
        override val releaseDate: String,
        override val watchListType: WatchListType
    ) : MediaModelSealed(
        id,
        title,
        overview,
        posterPath,
        backdropPath,
        rating,
        releaseDate,
        watchListType
    )

    data class MovieModel(
        override val id: Long,
        override val title: String,
        override val overview: String,
        override val posterPath: String,
        override val backdropPath: String,
        override val rating: Float,
        override val releaseDate: String,
        override val watchListType: WatchListType
    ) : MediaModelSealed(
        id,
        title,
        overview,
        posterPath,
        backdropPath,
        rating,
        releaseDate,
        watchListType
    )
}