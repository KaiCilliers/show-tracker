package com.sunrisekcdeveloper.showtracker.ui.rc

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.ViewHolderContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMediumPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie

class PosterMediumViewHolder(
    val binding: RcItemMediumPosterBinding
) : BaseViewHolder<DisplayMovie>(binding) {

    override fun bind(item: DisplayMovie) {
        binding.movie = item
        binding.executePendingBindings()
    }

    override fun subRecyclerView(): RecyclerView? = null

    override fun comparison(): DiffUtil.ItemCallback<DisplayMovie> = PosterDifferenceCallBack()
}