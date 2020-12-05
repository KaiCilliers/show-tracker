package com.sunrisekcdeveloper.showtracker

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<T>(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root),
    ViewHolderContract<T>