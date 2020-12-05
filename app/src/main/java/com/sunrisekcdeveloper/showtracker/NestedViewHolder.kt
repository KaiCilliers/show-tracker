package com.sunrisekcdeveloper.showtracker

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class NestedViewHolder<T>(binding: ViewBinding) : BaseViewHolder<T>(binding) {
    abstract fun nestedList(): RecyclerView
}