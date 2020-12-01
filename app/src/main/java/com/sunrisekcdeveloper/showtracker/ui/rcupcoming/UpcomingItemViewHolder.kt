package com.sunrisekcdeveloper.showtracker.ui.rcupcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMovieSummaryBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction

class UpcomingItemViewHolder(
    val binding: RcItemMovieSummaryBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: DisplayMovie,
        clickListener: PosterClickAction
    ) {
        binding.movie = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): UpcomingItemViewHolder {
            return UpcomingItemViewHolder(
                RcItemMovieSummaryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}