package com.sunrisekcdeveloper.showtracker.ui.components.viewholders.impl

import com.sunrisekcdeveloper.showtracker.ui.components.viewholders.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.ui.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemSmallPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.Movie

/**
 * Horizontal RC view item
 */
class SmallPosterViewHolder(
    private val binding: RcItemSmallPosterBinding,
    private val clickAction: ClickActionContract
) : BaseViewHolder<Movie>(binding) {
    override fun bind(item: Movie) {
        binding.movie = item
        binding.clickListener = clickAction
        binding.executePendingBindings()
    }

}