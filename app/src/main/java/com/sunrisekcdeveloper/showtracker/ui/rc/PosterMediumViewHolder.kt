package com.sunrisekcdeveloper.showtracker.ui.rc

import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.ViewHolderContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMediumPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie

class PosterMediumViewHolder(
    val binding: RcItemMediumPosterBinding
) : RecyclerView.ViewHolder(binding.root), ViewHolderContract<DisplayMovie> {

    override fun bind(item: DisplayMovie, clickAction: PosterClickAction?) {
        binding.movie = item
        clickAction?.let { binding.clickListener = clickAction }
        binding.executePendingBindings()
    }
//    companion object {
//        fun from(parent: ViewGroup): PosterMediumViewHolder {
//            val infalter = LayoutInflater.from(parent.context)
//            val binding = RcItemMediumPosterBinding.inflate(infalter, parent, false)
//            return PosterMediumViewHolder(binding)
//        }
//    }
}