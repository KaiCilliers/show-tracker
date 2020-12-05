package com.sunrisekcdeveloper.showtracker.ui.rcfeaturedcat

import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.databinding.RcItemFeaturedBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.FeaturedList

class FeaturedCategoryViewHolder(
    val binding: RcItemFeaturedBinding
) : BaseViewHolder<FeaturedList>(binding) {
    override fun bind(item: FeaturedList) {
        binding.featuredList = item
        binding.executePendingBindings()
    }

    fun subRecyclerView(): RecyclerView = binding.rcFeaturedList

}