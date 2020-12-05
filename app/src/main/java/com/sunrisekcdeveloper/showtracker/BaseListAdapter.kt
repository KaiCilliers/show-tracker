package com.sunrisekcdeveloper.showtracker

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<T, VH: RecyclerView.ViewHolder>(diff: DiffUtil.ItemCallback<T>) : ListAdapter<T, VH>(diff),
    AdapterContract<T>