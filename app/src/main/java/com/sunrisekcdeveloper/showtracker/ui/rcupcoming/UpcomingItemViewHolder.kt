package com.sunrisekcdeveloper.showtracker.ui.rcupcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.ViewHolderContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMovieSummaryBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterDifferenceCallBack

class UpcomingItemViewHolder(
    private val binding: RcItemMovieSummaryBinding
): BaseViewHolder<DisplayMovie>(binding) {
    override fun bind(item: DisplayMovie) {
        binding.movie = item
        binding.executePendingBindings()
    }

    override fun subRecyclerView(): RecyclerView? = null

    override fun comparison(): DiffUtil.ItemCallback<DisplayMovie> = PosterDifferenceCallBack()
}