package com.sunrisekcdeveloper.showtracker.ui.rc

import com.sunrisekcdeveloper.showtracker.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemSmallPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie

/**
 * Horizontal RC view item
 */
class PosterViewHolder(
    private val binding: RcItemSmallPosterBinding,
    private val clickAction: ClickActionContract
) : BaseViewHolder<DisplayMovie>(binding) {
    override fun bind(item: DisplayMovie) {
        binding.movie = item
        binding.clickListener = clickAction
        binding.executePendingBindings()
    }

}