package com.sunrisekcdeveloper.showtracker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.databinding.RcItemFeaturedBinding
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMediumPosterBinding
import com.sunrisekcdeveloper.showtracker.databinding.RcItemProgressHeaderBinding
import com.sunrisekcdeveloper.showtracker.databinding.RcItemSmallPosterBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import com.sunrisekcdeveloper.showtracker.entities.domain.FeaturedList
import com.sunrisekcdeveloper.showtracker.ui.rc.*
import com.sunrisekcdeveloper.showtracker.ui.rcfeaturedcat.CategoryDifferenceCallBack
import com.sunrisekcdeveloper.showtracker.ui.rcfeaturedcat.FeaturedCategoryViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface AdapterContract<in T> {
    fun addData(list: List<T>)
}

//abstract class AdapterBase<T, VH: RecyclerView.ViewHolder>(
//    private val diff: DiffUtil.ItemCallback<T>
//): ListAdapter<T, VH>(diff), AdapterContract<T>{}
//
//class CoolAdapter(val diff: PosterDifferenceCallBack)
//    : AdapterBase<DisplayMovie, PosterViewHolder>(diff()) {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
//        TODO("Not yet implemented")
//    }
//
//    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
//        TODO("Not yet implemented")
//    }
//
//    override fun addData(list: List<DisplayMovie>) {
//        TODO("Not yet implemented")
//    }
//}
//
//class AdapterOne(
//    val clickListener: PosterClickAction
//) : ListAdapter<DisplayMovie, PosterMediumViewHolder>(PosterDifferenceCallBack()),
//AdapterContract<DisplayMovie>{
//    override fun addData(list: List<DisplayMovie>) {
//        submitList(list)
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterMediumViewHolder =
//        PosterMediumViewHolder(
//            RcItemMediumPosterBinding.inflate(
//                LayoutInflater.from(parent.context), parent, false
//            )
//        )
//    override fun onBindViewHolder(holder: PosterMediumViewHolder, position: Int) {
//        holder.bind(getItem(position), clickListener)
//    }
//}
//
//class AdapterTwo(
//    val clickListener: PosterClickAction
//) : ListAdapter<FeaturedList, FeaturedCategoryViewHolder>(CategoryDifferenceCallBack()),
//AdapterContract<FeaturedList>{
//    // TODO make this a requirement from an interface for all adapters
//    override fun addData(list: List<FeaturedList>) {
//        submitList(list)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedCategoryViewHolder =
//        FeaturedCategoryViewHolder(
//            RcItemFeaturedBinding.inflate(
//                LayoutInflater.from(parent.context)
//            )
//        )
//
//    override fun onBindViewHolder(holder: FeaturedCategoryViewHolder, position: Int) {
//        holder.bind(getItem(position), clickListener)
//    }
//}








































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