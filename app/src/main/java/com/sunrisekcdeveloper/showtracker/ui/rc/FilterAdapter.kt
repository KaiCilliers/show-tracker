package com.sunrisekcdeveloper.showtracker.ui.rc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.sunrisekcdeveloper.showtracker.AdapterContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemSmallPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilterAdapter(val clickListener: PosterClickAction) :
    ListAdapter<DisplayMovie, PosterViewHolder>(
        PosterDifferenceCallBack()
    ), AdapterContract<DisplayMovie> {
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun addData(list: List<DisplayMovie>) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder =
        PosterViewHolder(
            RcItemSmallPosterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) =
        holder.bind(
            getItem(position),
            clickListener
        )
}