package com.sunrisekcdeveloper.showtracker.ui.rc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMediumPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie

class PosterMediumViewHolder(
    val binding: RcItemMediumPosterBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DisplayMovie, clickListener: PosterClickAction) {
        binding.movie = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }
    companion object {
        fun from(parent: ViewGroup): PosterMediumViewHolder {
            val infalter = LayoutInflater.from(parent.context)
            val binding = RcItemMediumPosterBinding.inflate(infalter, parent, false)
            return PosterMediumViewHolder(binding)
        }
    }
}