package com.sunrisekcdeveloper.showtracker.ui.rc

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilterAdapter(val clickListener: PosterClickAction):
    ListAdapter<DisplayMovie, PosterViewHolder>(
        PosterDifferenceCallBack()
    ) {
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addData(list: List<DisplayMovie>) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder =
        PosterViewHolder.from(parent)

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) =
        holder.bind(
            getItem(position),
            clickListener
        )
}