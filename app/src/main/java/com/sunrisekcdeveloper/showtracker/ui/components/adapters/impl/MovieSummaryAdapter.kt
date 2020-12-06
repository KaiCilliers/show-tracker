package com.sunrisekcdeveloper.showtracker.ui.components.adapters.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.ui.components.adapters.BaseListAdapter
import com.sunrisekcdeveloper.showtracker.ui.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMovieSummaryBinding
import com.sunrisekcdeveloper.showtracker.databinding.RcItemProgressHeaderBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.SuggestionListModel
import com.sunrisekcdeveloper.showtracker.entities.domain.diff.SuggestionListModelDiff
import com.sunrisekcdeveloper.showtracker.ui.components.viewholders.impl.HeaderViewHolder
import com.sunrisekcdeveloper.showtracker.ui.components.viewholders.impl.MovieSummaryViewHolder
import java.lang.UnsupportedOperationException

class MovieSummaryAdapter(
    private val clickAction: ClickActionContract
) : BaseListAdapter<SuggestionListModel, RecyclerView.ViewHolder>(SuggestionListModelDiff()) {

    override fun submit(list: List<SuggestionListModel>) {
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.rc_item_movie_summary) {
            MovieSummaryViewHolder(
                RcItemMovieSummaryBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), clickAction
            )
        } else {
            HeaderViewHolder(
                RcItemProgressHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        when (uiModel) {
            is SuggestionListModel.MovieItem -> (holder as MovieSummaryViewHolder).bind(
                uiModel.movie
            )
            is SuggestionListModel.HeaderItem -> (holder as HeaderViewHolder).bind(uiModel.name)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SuggestionListModel.MovieItem -> R.layout.rc_item_movie_summary
            is SuggestionListModel.HeaderItem -> R.layout.rc_item_progress_header
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }
}