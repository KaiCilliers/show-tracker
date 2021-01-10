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

package com.sunrisekcdeveloper.showtracker.features.discover.models

import androidx.recyclerview.widget.DiffUtil

/**
 * Movie is a domain object with basic information of a single movie
 *
 * @property title of the movie
 * @property episode temp value for testing purposes
 * @property season temp value for testing purposes
 * @property episodeTitle temp value for testing purposes
 * @property posterUrl references a URL that points to a movie poster image
 */
// TODO split class into Movie and Show
data class Movie(
    val title: String,
    val slug: String,
    val episode: String = "${(1..55).random()}",
    val season: String = "${(1..14).random()}",
    val episodeTitle: String = "Pilot: The best default title",
    var posterUrl: String = "https://source.unsplash.com/random"
) {
    /**
     * Movie Diff knows how to compared [Movie] objects which prevents ListAdapters and
     * PagingDataAdapters replacing an entire list of data and instead only replace the items that got
     * changed
     */
    companion object Diff : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem == newItem
    }
}