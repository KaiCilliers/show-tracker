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