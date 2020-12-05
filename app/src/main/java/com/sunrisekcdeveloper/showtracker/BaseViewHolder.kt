package com.sunrisekcdeveloper.showtracker

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<T>(
    binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root), ViewHolderContract<T>

//class ClickableViewHolder{} // requires a click action
//class NestedViewHolder{} // contains a RC in its layout
//
//abstract class NVH<T>(binding: ViewBinding) : BaseViewHolder<T>(binding), NestedViewHolderContract<T>
//abstract class CVH<T>(binding: ViewBinding) : BaseViewHolder<T>(binding)
//
//interface VHContract<T>{
//    fun bind(item: T)
//    fun subRecyclerView(): RecyclerView?
//}
//
//interface ClickableVieHolderContract<T> : ViewHolderContract<T> {
//    fun nestedList(): RecyclerView
//}
//interface NestedViewHolderContract<T> : ViewHolderContract<T> {
//    fun bindClickAction(clickAction: ClickActionContract<T>)
//}

