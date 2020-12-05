package com.sunrisekcdeveloper.showtracker.ui.rcupcoming

import com.sunrisekcdeveloper.showtracker.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMovieSummaryBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie

class UpcomingItemViewHolder(
    private val binding: RcItemMovieSummaryBinding
): BaseViewHolder<DisplayMovie>(binding) {
    override fun bind(item: DisplayMovie) {
        binding.movie = item
        binding.executePendingBindings()
    }

}