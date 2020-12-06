package com.sunrisekcdeveloper.showtracker.entities.domain.diff

import androidx.recyclerview.widget.DiffUtil
import com.sunrisekcdeveloper.showtracker.entities.domain.FeaturedList

class FeaturedListDiff : DiffUtil.ItemCallback<FeaturedList>() {
    override fun areItemsTheSame(oldItem: FeaturedList, newItem: FeaturedList): Boolean =
        oldItem.heading == newItem.heading

    override fun areContentsTheSame(oldItem: FeaturedList, newItem: FeaturedList): Boolean =
        oldItem == newItem
}