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

package com.sunrisekcdeveloper.showtracker.commons.components.adapters

import com.bumptech.glide.Glide
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.commons.components.viewholders.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.commons.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMediumPosterBinding
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie

/**
 * Medium Poster ViewHolder represents a medium poster icon that represents a movie or show
 *
 * @property binding is the auto generated binding object that represents the associated item layout
 * @property clickAction is the action executed when the [binding] object is clicked
 */
class MediumPosterViewHolder(
    private val binding: RcItemMediumPosterBinding,
    private val clickAction: ClickActionContract
) : BaseViewHolder<Movie>(binding) {
    override fun bind(item: Movie) {
        binding.movie = item
        binding.clickListener = clickAction

        Glide.with(binding.root.context)
            .load(item.posterUrl)
            .placeholder(R.drawable.wanted_poster)
            .error(R.drawable.error_poster)
            .into(binding.imgvMovieMediumPoster)

        binding.executePendingBindings()
    }
}