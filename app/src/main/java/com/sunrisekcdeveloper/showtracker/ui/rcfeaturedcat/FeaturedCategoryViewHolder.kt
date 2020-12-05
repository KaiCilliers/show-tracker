package com.sunrisekcdeveloper.showtracker.ui.rcfeaturedcat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.ViewHolderContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemFeaturedBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.FeaturedList
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterDifferenceCallBack

class FeaturedCategoryViewHolder(
    val binding: RcItemFeaturedBinding
) : BaseViewHolder<FeaturedList>(binding) {
    override fun bind(item: FeaturedList) {
        binding.featuredList = item
        binding.executePendingBindings()
    }

    override fun subRecyclerView(): RecyclerView = binding.rcFeaturedList

    override fun comparison(): DiffUtil.ItemCallback<FeaturedList> = CategoryDifferenceCallBack()
}