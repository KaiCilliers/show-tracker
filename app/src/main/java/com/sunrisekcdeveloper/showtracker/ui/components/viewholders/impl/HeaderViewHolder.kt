package com.sunrisekcdeveloper.showtracker.ui.components.viewholders.impl

import com.sunrisekcdeveloper.showtracker.ui.components.viewholders.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.databinding.RcItemProgressHeaderBinding

/**
 * Today, Tomorrow, Next Week etc.
 * headers for upcoming episode releases
 */
class HeaderViewHolder(
    private val binding: RcItemProgressHeaderBinding
    ) : BaseViewHolder<String>(binding) {
    override fun bind(item: String) {
        binding.content = item
    }
}