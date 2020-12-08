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

package com.sunrisekcdeveloper.showtracker.entities.domain

import androidx.recyclerview.widget.DiffUtil

/**
 * Featured List is a domain object that represents a collection of [Movie]s associated to a common
 * category represented by [heading]
 *
 * @property heading of the collection of [Movie]s
 * @property results is the list of [Movie] objects
 */
data class FeaturedList(
    val heading: String,
    val results: List<Movie>
) {
    /**
     * FeaturedList Diff knows how to compared [FeaturedList] objects which prevents ListAdapters and
     * PagingDataAdapters replacing an entire list of data and instead only replace the items that got
     * changed
     */
    companion object Diff : DiffUtil.ItemCallback<FeaturedList>() {
        override fun areItemsTheSame(oldItem: FeaturedList, newItem: FeaturedList): Boolean =
            oldItem.heading == newItem.heading

        override fun areContentsTheSame(oldItem: FeaturedList, newItem: FeaturedList): Boolean =
            oldItem == newItem
    }
}