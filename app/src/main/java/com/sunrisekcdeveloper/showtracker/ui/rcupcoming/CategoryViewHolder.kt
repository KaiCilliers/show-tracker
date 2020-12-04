package com.sunrisekcdeveloper.showtracker.ui.rcupcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.ViewHolderContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemProgressHeaderBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction

/**
 * Today, Tomorrow, Next Week etc.
 * headers for upcoming episode releases
 */
class CategoryViewHolder(
    private val binding: RcItemProgressHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root), ViewHolderContract<String> {
    override fun bind(item: String, clickAction: PosterClickAction?) {
        binding.content = item
    }
//    companion object {
//        fun create(parent: ViewGroup) : CategoryViewHolder {
//            val binding = RcItemProgressHeaderBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//            return CategoryViewHolder(binding)
//        }
//    }
}