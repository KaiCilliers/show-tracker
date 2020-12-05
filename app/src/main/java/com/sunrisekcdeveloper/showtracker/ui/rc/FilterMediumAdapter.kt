package com.sunrisekcdeveloper.showtracker.ui.rc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sunrisekcdeveloper.showtracker.AdapterContract
import com.sunrisekcdeveloper.showtracker.BaseListAdapter
import com.sunrisekcdeveloper.showtracker.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMediumPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie

class FilterMediumAdapter(
    private val clickAction: ClickActionContract
) : BaseListAdapter<DisplayMovie, PosterMediumViewHolder>(PosterDifferenceCallBack()) {
    override fun submit(list: List<DisplayMovie>) {
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterMediumViewHolder =
        PosterMediumViewHolder(
            RcItemMediumPosterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), clickAction
        )

    override fun onBindViewHolder(holder: PosterMediumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}