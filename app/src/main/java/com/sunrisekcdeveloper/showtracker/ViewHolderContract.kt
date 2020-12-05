package com.sunrisekcdeveloper.showtracker

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

interface ViewHolderContract<T>{
    fun bind(item: T)
    fun subRecyclerView(): RecyclerView?
    fun comparison(): DiffUtil.ItemCallback<T>?
}