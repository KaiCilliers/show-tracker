package com.sunrisekcdeveloper.showtracker.ui.rcupcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.ViewHolderContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemProgressHeaderBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterDifferenceCallBack

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

    override fun subRecyclerView(): RecyclerView? = null

    override fun comparison(): DiffUtil.ItemCallback<String>? = null
}