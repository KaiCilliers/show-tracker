package com.sunrisekcdeveloper.showtracker.ui.components.adapters

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.ui.components.AdapterContract

abstract class BaseListAdapter<T, VH: RecyclerView.ViewHolder>(diff: DiffUtil.ItemCallback<T>) : ListAdapter<T, VH>(diff),
    AdapterContract<T>