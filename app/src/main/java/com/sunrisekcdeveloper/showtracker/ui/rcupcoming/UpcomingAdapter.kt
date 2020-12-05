package com.sunrisekcdeveloper.showtracker.ui.rcupcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.BaseListAdapter
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMovieSummaryBinding
import com.sunrisekcdeveloper.showtracker.databinding.RcItemProgressHeaderBinding
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction
import java.lang.UnsupportedOperationException

class UpcomingAdapter(
    private val clickListener: PosterClickAction
) : BaseListAdapter<UiModel, RecyclerView.ViewHolder>(UiModelDiff()) {

    override fun submit(list: List<UiModel>) {
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.rc_item_movie_summary) {
            UpcomingItemViewHolder(
                RcItemMovieSummaryBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } else {
            CategoryViewHolder(
                RcItemProgressHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        when (uiModel) {
            is UiModel.MovieItem -> (holder as UpcomingItemViewHolder).bind(
                uiModel.movie
            )
            is UiModel.CategoryHeader -> (holder as CategoryViewHolder).bind(uiModel.name)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.MovieItem -> R.layout.rc_item_movie_summary
            is UiModel.CategoryHeader -> R.layout.rc_item_progress_header
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }
}