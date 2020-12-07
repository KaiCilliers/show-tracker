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
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.ui.components.adapters.BaseListAdapter
import com.sunrisekcdeveloper.showtracker.ui.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMovieSummaryBinding
import com.sunrisekcdeveloper.showtracker.databinding.RcItemProgressHeaderBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.SuggestionListModel
import com.sunrisekcdeveloper.showtracker.entities.domain.diff.SuggestionListModelDiff
import com.sunrisekcdeveloper.showtracker.ui.components.viewholders.impl.HeaderViewHolder
import com.sunrisekcdeveloper.showtracker.ui.components.viewholders.impl.MovieSummaryViewHolder
import java.lang.UnsupportedOperationException

/**
 * Movie Summary Adapter binds [SuggestionListModel] to both [MovieSummaryViewHolder] and
 * [HeaderViewHolder] and determines which item to provide based on the view type required
 *
 * @property clickAction is the action executed when the [RcItemMovieSummaryBinding] object is
 * clicked
 */
class MovieSummaryAdapter : BaseListAdapter<SuggestionListModel, RecyclerView.ViewHolder>(SuggestionListModelDiff()) {

    private lateinit var clickAction: ClickActionContract

    override fun submit(list: List<SuggestionListModel>) {
        submitList(list)
    }

    // Temporal Coupling introduced having to call this function before setting Recycler's adapter
    override fun addOnClickAction(action: ClickActionContract) { clickAction = action }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.rc_item_movie_summary) {
            MovieSummaryViewHolder(
                RcItemMovieSummaryBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), clickAction
            )
        } else {
            HeaderViewHolder(
                RcItemProgressHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        when (uiModel) {
            is SuggestionListModel.MovieItem -> (holder as MovieSummaryViewHolder).bind(
                uiModel.movie
            )
            is SuggestionListModel.HeaderItem -> (holder as HeaderViewHolder).bind(uiModel.name)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SuggestionListModel.MovieItem -> R.layout.rc_item_movie_summary
            is SuggestionListModel.HeaderItem -> R.layout.rc_item_progress_header
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }
}