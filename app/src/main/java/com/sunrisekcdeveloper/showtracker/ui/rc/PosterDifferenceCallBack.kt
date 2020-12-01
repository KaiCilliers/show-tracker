package com.sunrisekcdeveloper.showtracker.ui.rc

import androidx.recyclerview.widget.DiffUtil
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie

class PosterDifferenceCallBack : DiffUtil.ItemCallback<DisplayMovie>(){
    override fun areItemsTheSame(oldItem: DisplayMovie, newItem: DisplayMovie): Boolean =
        oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: DisplayMovie, newItem: DisplayMovie): Boolean =
        oldItem == newItem
}