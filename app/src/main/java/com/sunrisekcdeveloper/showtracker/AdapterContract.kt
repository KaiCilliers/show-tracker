package com.sunrisekcdeveloper.showtracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMediumPosterBinding
import com.sunrisekcdeveloper.showtracker.databinding.RcItemProgressHeaderBinding
import com.sunrisekcdeveloper.showtracker.databinding.RcItemSmallPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction

interface AdapterContract<in T> {
    fun addData(list: List<T>)
}
//class ViewHolderOne(
//    val binding: RcItemMediumPosterBinding
//) : RecyclerView.ViewHolder(binding.root) {
//    fun bind(item: DisplayMovie, clickListener: PosterClickAction) {
//        binding.movie = item
//        binding.clickListener = clickListener
//        binding.executePendingBindings()
//    }
//    companion object {
//        fun from(parent: ViewGroup): ViewHolderOne {
//            val infalter = LayoutInflater.from(parent.context)
//            val binding = RcItemMediumPosterBinding.inflate(infalter, parent, false)
//            return ViewHolderOne(binding)
//        }
//    }
//}
//class ViewHolderTwo(
//    private val binding: RcItemProgressHeaderBinding
//) : RecyclerView.ViewHolder(binding.root) {
//    fun bind(content: String) {
//        binding.content = content
//    }
//    companion object {
//        fun create(parent: ViewGroup) : ViewHolderTwo {
//            val binding = RcItemProgressHeaderBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//            return ViewHolderTwo(binding)
//        }
//    }
//}