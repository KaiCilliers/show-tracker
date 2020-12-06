package com.sunrisekcdeveloper.showtracker.ui.components.viewholders

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class NestedViewHolder<T>(binding: ViewBinding) : BaseViewHolder<T>(binding) {
    abstract fun nestedList(): RecyclerView
}