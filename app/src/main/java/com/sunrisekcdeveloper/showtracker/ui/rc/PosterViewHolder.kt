package com.sunrisekcdeveloper.showtracker.ui.rc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.databinding.RcItemSmallPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie

/**
 * Horizontal RC view item
 */
class PosterViewHolder(
    val binding: RcItemSmallPosterBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: DisplayMovie,
        clickListener: PosterClickAction
    ) {
        binding.movie = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }
    // TODO consider replacing
    companion object {
        fun from(parent: ViewGroup): PosterViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RcItemSmallPosterBinding.inflate(layoutInflater, parent, false)
            return  PosterViewHolder(binding)
        }
    }
}