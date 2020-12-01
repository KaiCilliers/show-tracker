package com.sunrisekcdeveloper.showtracker.ui.rcupcoming

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.UnsupportedOperationException

class UpcomingAdapter(
    private val clickListener: PosterClickAction
) : ListAdapter<UiModel, RecyclerView.ViewHolder>(UiModelDiff()) {

    fun addData(list: List<UiModel>) {
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.rc_item_movie_summary) {
            UpcomingItemViewHolder.create(parent)
        } else {
            CategoryViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        when (uiModel) {
            is UiModel.MovieItem -> (holder as UpcomingItemViewHolder).bind(
                uiModel.movie, clickListener
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