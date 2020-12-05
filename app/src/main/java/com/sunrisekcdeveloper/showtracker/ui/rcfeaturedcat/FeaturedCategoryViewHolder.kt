package com.sunrisekcdeveloper.showtracker.ui.rcfeaturedcat

import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.ClickActionContract
import com.sunrisekcdeveloper.showtracker.NestedViewHolder
import com.sunrisekcdeveloper.showtracker.databinding.RcItemFeaturedBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.FeaturedList

class FeaturedCategoryViewHolder(
    private val binding: RcItemFeaturedBinding,
    private val clickAction: ClickActionContract
) : NestedViewHolder<FeaturedList>(binding) {
    override fun bind(item: FeaturedList) {
        binding.featuredList = item
        binding.clickListener = clickAction
        binding.executePendingBindings()
    }
    override fun nestedList(): RecyclerView = binding.rcFeaturedList
}