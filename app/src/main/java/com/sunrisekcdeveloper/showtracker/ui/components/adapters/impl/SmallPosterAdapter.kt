package com.sunrisekcdeveloper.showtracker.ui.components.adapters.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunrisekcdeveloper.showtracker.ui.components.adapters.BaseListAdapter
import com.sunrisekcdeveloper.showtracker.ui.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemSmallPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.Movie
import com.sunrisekcdeveloper.showtracker.entities.domain.diff.MovieDiff
import com.sunrisekcdeveloper.showtracker.ui.components.viewholders.impl.SmallPosterViewHolder

class SmallPosterAdapter(private val clickAction: ClickActionContract) :
    BaseListAdapter<Movie, SmallPosterViewHolder>(MovieDiff()) {

    override fun submit(list: List<Movie>) {
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallPosterViewHolder =
        SmallPosterViewHolder(
            RcItemSmallPosterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            clickAction
        )

    override fun onBindViewHolder(holder: SmallPosterViewHolder, position: Int) =
        holder.bind(
            getItem(position)
        )
}