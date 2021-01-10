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

package com.sunrisekcdeveloper.showtracker.features.watchlist.adapters

import com.sunrisekcdeveloper.showtracker.ui.components.viewholders.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.ui.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMovieSummaryBinding
import com.sunrisekcdeveloper.showtracker.features.discover.models.Movie

/**
 * Movie Summary ViewHolder represents a movie or show item with limited information and a poster
 * preview
 *
 * @property binding is the auto generated binding object that represents the associated item layout
 * @property clickAction is the action executed when the [binding] object is clicked
 */
class MovieSummaryViewHolder(
    private val binding: RcItemMovieSummaryBinding,
    private val clickAction: ClickActionContract
): BaseViewHolder<Movie>(binding) {
    override fun bind(item: Movie) {
        binding.movie = item
        binding.clickListener = clickAction
        binding.executePendingBindings()
    }
}