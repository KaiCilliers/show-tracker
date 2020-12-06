package com.sunrisekcdeveloper.showtracker.ui.components.adapters.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunrisekcdeveloper.showtracker.ui.components.adapters.BaseListAdapter
import com.sunrisekcdeveloper.showtracker.ui.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMediumPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.Movie
import com.sunrisekcdeveloper.showtracker.entities.domain.diff.MovieDiff
import com.sunrisekcdeveloper.showtracker.ui.components.viewholders.impl.MediumPosterViewHolder

class MediumPosterAdapter(
    private val clickAction: ClickActionContract
) : BaseListAdapter<Movie, MediumPosterViewHolder>(MovieDiff()) {
    override fun submit(list: List<Movie>) {
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediumPosterViewHolder =
        MediumPosterViewHolder(
            RcItemMediumPosterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), clickAction
        )

    override fun onBindViewHolder(holder: MediumPosterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}