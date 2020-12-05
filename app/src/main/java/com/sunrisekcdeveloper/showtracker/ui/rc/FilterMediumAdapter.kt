package com.sunrisekcdeveloper.showtracker.ui.rc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sunrisekcdeveloper.showtracker.AdapterContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMediumPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie

class FilterMediumAdapter(
    val clickListener: PosterClickAction
) : ListAdapter<DisplayMovie, PosterMediumViewHolder>(PosterDifferenceCallBack()), AdapterContract<DisplayMovie> {
    override fun addData(list: List<DisplayMovie>) {
        submitList(list)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterMediumViewHolder =
        PosterMediumViewHolder(
            RcItemMediumPosterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    override fun onBindViewHolder(holder: PosterMediumViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}