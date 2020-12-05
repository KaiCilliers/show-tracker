package com.sunrisekcdeveloper.showtracker

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BasePagingAdapter<T: Any, VH: RecyclerView.ViewHolder>(holder: ViewHolderContract<T>) : PagingDataAdapter<T, VH>(holder.comparison()!!),
    AdapterContract<T>