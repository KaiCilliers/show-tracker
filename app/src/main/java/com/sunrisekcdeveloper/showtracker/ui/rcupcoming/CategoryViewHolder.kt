package com.sunrisekcdeveloper.showtracker.ui.rcupcoming

import com.sunrisekcdeveloper.showtracker.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.databinding.RcItemProgressHeaderBinding

/**
 * Today, Tomorrow, Next Week etc.
 * headers for upcoming episode releases
 */
class CategoryViewHolder(
    private val binding: RcItemProgressHeaderBinding
    ) : BaseViewHolder<String>(binding) {
    override fun bind(item: String) {
        binding.content = item
    }

}