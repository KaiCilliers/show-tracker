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

package com.sunrisekcdeveloper.showtracker.ui.components.adapters.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunrisekcdeveloper.showtracker.ui.components.adapters.BaseListAdapter
import com.sunrisekcdeveloper.showtracker.ui.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemFeaturedBinding
import com.sunrisekcdeveloper.showtracker.model.FeaturedList
import com.sunrisekcdeveloper.showtracker.ui.components.viewholders.impl.SuggestionListViewHolder

/**
 * Suggestion List Adapter binds [FeaturedList] to [SuggestionListViewHolder] and also
 * inflates any child RecyclerView lists with their own adapters
 *
 * @property clickAction is the action executed when the [RcItemFeaturedBinding] object is
 * clicked
 */
class SuggestionListAdapter : BaseListAdapter<FeaturedList, SuggestionListViewHolder>(FeaturedList.Diff) {

    private var data: List<FeaturedList> = listOf()
    private lateinit var clickAction: ClickActionContract

    // TODO be able to submit a single item at a time then just append and resubmit the entire list
    //  on this end. Also perhaps have an option to refresh it with a new list?
    override fun submit(list: List<FeaturedList>) {
        this.data = list
        submitList(list)
    }

    override fun submit(item: FeaturedList) {
        val newList = listOf(*data.toTypedArray(), item)
        data = newList
        submitList(data)
    }

    // Temporal Coupling introduced having to call this function before setting Recycler's adapter
    override fun addOnClickAction(action: ClickActionContract) { clickAction = action }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionListViewHolder =
        SuggestionListViewHolder(
            RcItemFeaturedBinding.inflate(
                LayoutInflater.from(parent.context)
            ), clickAction
        )

    override fun onBindViewHolder(holder: SuggestionListViewHolder, position: Int) {
        holder.bind(getItem(position))

        val subLayout = LinearLayoutManager(
            holder.itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val subAdapter = SmallPosterAdapter()
        subAdapter.addOnClickAction(clickAction) // Temporal coupling
        val subRc = holder.nestedList()

        subRc.layoutManager = subLayout
        subRc.adapter = subAdapter
        subAdapter.submit(getItem(position).results)
    }
}