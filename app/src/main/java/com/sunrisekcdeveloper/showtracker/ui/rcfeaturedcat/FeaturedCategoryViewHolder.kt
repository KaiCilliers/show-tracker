package com.sunrisekcdeveloper.showtracker.ui.rcfeaturedcat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.ViewHolderContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemFeaturedBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.FeaturedList
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction

class FeaturedCategoryViewHolder(
    private val binding: RcItemFeaturedBinding
) : RecyclerView.ViewHolder(binding.root), ViewHolderContract<FeaturedList> {
    // TODO a bit hacky way to access sub recyclerview
    fun view(): RcItemFeaturedBinding = binding
    override fun bind(item: FeaturedList, clickAction: PosterClickAction?) {
        binding.featuredList = item
        binding.executePendingBindings()
    }
//    companion object {
//        fun from(parent: ViewGroup): FeaturedCategoryViewHolder {
//            val binding = RcItemFeaturedBinding.inflate(
//                LayoutInflater.from(parent.context)
//            )
//            return FeaturedCategoryViewHolder(binding)
//        }
//    }
}