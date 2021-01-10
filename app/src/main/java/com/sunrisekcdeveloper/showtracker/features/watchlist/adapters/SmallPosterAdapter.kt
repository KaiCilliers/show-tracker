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

package com.sunrisekcdeveloper.showtracker.features.watchlist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunrisekcdeveloper.showtracker.commons.components.adapters.BaseListAdapter
import com.sunrisekcdeveloper.showtracker.commons.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemSmallPosterBinding
import com.sunrisekcdeveloper.showtracker.features.discover.models.Movie

/**
 * Small Poster Adapter binds [Movie] to [SmallPosterViewHolder]
 *
 * @property clickAction is the action executed when the [RcItemSmallPosterBinding] object is
 * clicked
 */
class SmallPosterAdapter : BaseListAdapter<Movie, SmallPosterViewHolder>(Movie.Diff) {

    private lateinit var clickAction: ClickActionContract

    override fun submit(list: List<Movie>) {
        submitList(list)
    }

    // Temporal Coupling introduced having to call this function before setting Recycler's adapter
    override fun addOnClickAction(action: ClickActionContract) { clickAction = action }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallPosterViewHolder =
        SmallPosterViewHolder(
            RcItemSmallPosterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            clickAction
        )

    override fun onBindViewHolder(holder: SmallPosterViewHolder, position: Int) =
        holder.bind(
            getItem(position)
        )
}