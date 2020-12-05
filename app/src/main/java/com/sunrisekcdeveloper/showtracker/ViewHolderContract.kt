package com.sunrisekcdeveloper.showtracker

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction

interface ViewHolderContract<in T>{
    fun bind(item: T, clickAction: PosterClickAction?)
}
interface RealViewHolderContract<T>{
    fun bind(item: T)
    fun subRecyclerView(): RecyclerView
    fun comparison(): DiffUtil.ItemCallback<T>
}