package com.sunrisekcdeveloper.showtracker.ui.rcupcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.ViewHolderContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMovieSummaryBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction

class UpcomingItemViewHolder(
    val binding: RcItemMovieSummaryBinding
) : RecyclerView.ViewHolder(binding.root), ViewHolderContract<DisplayMovie> {
    override fun bind(
        item: DisplayMovie,
        clickAction: PosterClickAction?
    ) {
        binding.movie = item
        clickAction?.let {binding.clickListener = clickAction}
        binding.executePendingBindings()
    }
//    companion object {
//        fun create(parent: ViewGroup): UpcomingItemViewHolder {
//            return UpcomingItemViewHolder(
//                RcItemMovieSummaryBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
//        }
//    }
}