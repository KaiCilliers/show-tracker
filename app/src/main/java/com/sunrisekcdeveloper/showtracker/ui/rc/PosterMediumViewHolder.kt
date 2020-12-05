package com.sunrisekcdeveloper.showtracker.ui.rc

import com.sunrisekcdeveloper.showtracker.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMediumPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie

class PosterMediumViewHolder(
    private val binding: RcItemMediumPosterBinding,
    private val clickAction: ClickActionContract
) : BaseViewHolder<DisplayMovie>(binding) {
    override fun bind(item: DisplayMovie) {
        binding.movie = item
        binding.clickListener = clickAction
        binding.executePendingBindings()
    }
}