package com.sunrisekcdeveloper.showtracker.ui.rc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.ViewHolderContract
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

    override fun subRecyclerView(): RecyclerView? = null

    override fun comparison(): DiffUtil.ItemCallback<DisplayMovie> = PosterDifferenceCallBack()
}