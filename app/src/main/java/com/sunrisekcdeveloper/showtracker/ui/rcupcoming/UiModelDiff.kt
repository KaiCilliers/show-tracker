package com.sunrisekcdeveloper.showtracker.ui.rcupcoming

import androidx.recyclerview.widget.DiffUtil

class UiModelDiff : DiffUtil.ItemCallback<UiModel>() {
    override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
        return (oldItem is UiModel.MovieItem && newItem is UiModel.MovieItem
                        && oldItem.movie.title == newItem.movie.title) ||
                (oldItem is UiModel.CategoryHeader && newItem is UiModel.CategoryHeader
                        && oldItem.name == newItem.name)
    }

    override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
        oldItem == newItem
}