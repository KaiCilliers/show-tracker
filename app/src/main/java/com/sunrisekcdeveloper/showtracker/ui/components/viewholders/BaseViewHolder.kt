package com.sunrisekcdeveloper.showtracker.ui.components.viewholders

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.sunrisekcdeveloper.showtracker.ui.components.ViewHolderContract

abstract class BaseViewHolder<T>(
    binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root), ViewHolderContract<T>

