package com.sunrisekcdeveloper.showtracker.ui.components.adapters

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.ui.components.AdapterContract

abstract class BasePagingAdapter<T: Any, VH: RecyclerView.ViewHolder>(diff: DiffUtil.ItemCallback<T>) : PagingDataAdapter<T, VH>(diff),
    AdapterContract<T>