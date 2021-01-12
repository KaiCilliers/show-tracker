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

package com.sunrisekcdeveloper.showtracker.features.discover.adapters

import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.commons.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.commons.components.viewholders.NestedViewHolder
import com.sunrisekcdeveloper.showtracker.databinding.RcItemFeaturedBinding
import com.sunrisekcdeveloper.showtracker.models.roomresults.FeaturedList

/**
 * Suggestion List ViewHolder represents a list of movies or shows identified by a category header
 *
 * @property binding is the auto generated binding object that represents the associated item layout
 * @property clickAction is the action executed when the [binding] object is clicked
 */
class SuggestionListViewHolder(
    private val binding: RcItemFeaturedBinding,
    private val clickAction: ClickActionContract
) : NestedViewHolder<FeaturedList>(binding) {
    override fun bind(item: FeaturedList) {
        binding.featuredList = item
        binding.clickListener = clickAction
        binding.executePendingBindings()
    }
    override fun nestedList(): RecyclerView = binding.rcFeaturedList
}