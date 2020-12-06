package com.sunrisekcdeveloper.showtracker.entities.domain.diff

import androidx.recyclerview.widget.DiffUtil
import com.sunrisekcdeveloper.showtracker.entities.domain.Movie

class MovieDiff : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem
}