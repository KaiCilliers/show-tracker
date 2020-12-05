package com.sunrisekcdeveloper.showtracker.ui.rc

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunrisekcdeveloper.showtracker.BaseListAdapter
import com.sunrisekcdeveloper.showtracker.databinding.RcItemSmallPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie

class FilterAdapter(val clickListener: PosterClickAction) :
    BaseListAdapter<DisplayMovie, PosterViewHolder>(PosterDifferenceCallBack()) {

    override fun submit(list: List<DisplayMovie>) {
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder =
        PosterViewHolder(
            RcItemSmallPosterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) =
        holder.bind(
            getItem(position))
}