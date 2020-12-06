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

package com.sunrisekcdeveloper.showtracker.entities.domain.diff

import androidx.recyclerview.widget.DiffUtil
import com.sunrisekcdeveloper.showtracker.entities.domain.SuggestionListModel

class SuggestionListModelDiff : DiffUtil.ItemCallback<SuggestionListModel>() {
    override fun areItemsTheSame(oldItem: SuggestionListModel, newItem: SuggestionListModel): Boolean {
        return (oldItem is SuggestionListModel.MovieItem && newItem is SuggestionListModel.MovieItem
                && oldItem.movie.title == newItem.movie.title) ||
                (oldItem is SuggestionListModel.HeaderItem && newItem is SuggestionListModel.HeaderItem
                        && oldItem.name == newItem.name)
    }

    override fun areContentsTheSame(oldItem: SuggestionListModel, newItem: SuggestionListModel): Boolean =
        oldItem == newItem
}