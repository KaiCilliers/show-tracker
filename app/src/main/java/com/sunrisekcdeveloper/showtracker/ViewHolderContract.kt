package com.sunrisekcdeveloper.showtracker

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.databinding.RcItemMediumPosterBinding
import com.sunrisekcdeveloper.showtracker.databinding.RcItemProgressHeaderBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import com.sunrisekcdeveloper.showtracker.generated.callback.OnClickListener
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterDifferenceCallBack
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterMediumViewHolder
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Differences:
 *
 * Layout which needs inflation
 * Data to bind to the layout
 */
interface ViewHolderContract<in T>{
    fun bind(item: T, clickAction: PosterClickAction?)
}
/**
 * Each VH has to take a custom BINDING (cant abstract it)
 * Inflate binding for now - you can use Hilt to provide it later :)
 */
//class OneVH(private val binding: RcItemProgressHeaderBinding
//): RecyclerView.ViewHolder(binding.root), ViewHolderContract<String>{
//    override fun bind(data: String, clickAction: PosterClickAction?) {
//        binding.content = data
//    }
//}
//class TwoVH(private val binding: RcItemMediumPosterBinding
//): RecyclerView.ViewHolder(binding.root), ViewHolderContract<DisplayMovie>{
//    override fun bind(data: DisplayMovie, clickAction: PosterClickAction?) {
//        binding.movie = data
//        clickAction?.let { binding.clickListener = clickAction }
//    }
//}
//
//
//
//
//class GenericViewHolder<T : ListItemViewModel>(private val binding: ViewDataBinding) :
//    RecyclerView.ViewHolder(binding.root) {
//
//    fun bind(itemViewModel: T) {
//        binding.setVariable(BR.listItemViewModel, itemViewModel)
//        binding.executePendingBindings()
//    }
//
//}























//class AdapterOne(val clickListener: PosterClickAction
//) : ListAdapter<DisplayMovie, PosterMediumViewHolder>(PosterDifferenceCallBack()) {
//    fun addData(data: List<DisplayMovie>) {
//        submitList(data)
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterMediumViewHolder =
//        PosterMediumViewHolder.from(parent)
//    override fun onBindViewHolder(holder: PosterMediumViewHolder, position: Int) {
//        holder.bind(getItem(position), clickListener)
//    }
//}
//
//class AdapterTwo(val clickListener: PosterClickAction
//): ListAdapter<DisplayMovie, PosterViewHolder>(PosterDifferenceCallBack()) {
//
//    fun addData(list: List<DisplayMovie>) {
//        submitList(list)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder =
//        PosterViewHolder.from(parent)
//
//    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) =
//        holder.bind(
//            getItem(position),
//            clickListener
//        )
//}
//
///**
// * DataType to bind to VH
// * VH to return
// */
//fun s() {}