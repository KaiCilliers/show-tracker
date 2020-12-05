package com.sunrisekcdeveloper.showtracker

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BasePagingAdapter<T: Any, VH: RecyclerView.ViewHolder>(diff: DiffUtil.ItemCallback<T>) : PagingDataAdapter<T, VH>(diff),
    AdapterContract<T>