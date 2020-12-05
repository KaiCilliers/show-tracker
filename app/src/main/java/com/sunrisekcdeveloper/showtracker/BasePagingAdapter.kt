package com.sunrisekcdeveloper.showtracker

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BasePagingAdapter<T: Any, VH: RecyclerView.ViewHolder>(holder: RealViewHolderContract<T>) : PagingDataAdapter<T, VH>(holder.comparison()),
    RealAdapterContract<T>