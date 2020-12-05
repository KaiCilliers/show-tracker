package com.sunrisekcdeveloper.showtracker.ui.rc

import com.sunrisekcdeveloper.showtracker.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.databinding.RcItemSmallPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie

/**
 * Horizontal RC view item
 */
class PosterViewHolder(
    val binding: RcItemSmallPosterBinding
) : BaseViewHolder<DisplayMovie>(binding) {
    override fun bind(item: DisplayMovie) {
        binding.movie = item
        binding.executePendingBindings()
    }

}