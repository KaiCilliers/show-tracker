package com.sunrisekcdeveloper.showtracker.ui.rc

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie

class FilterMediumAdapter(
    val clickListener: PosterClickAction
) : ListAdapter<DisplayMovie, PosterMediumViewHolder>(PosterDifferenceCallBack()) {
    fun addData(data: List<DisplayMovie>) {
        submitList(data)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterMediumViewHolder =
        PosterMediumViewHolder.from(parent)
    override fun onBindViewHolder(holder: PosterMediumViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}