/*
 * Copyright © 2021. The Android Open Source Project
 *
 * @author Kai Cilliers
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sunrisekcdeveloper.showtracker.features.discovery.presentation

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.ActivityMain
import com.sunrisekcdeveloper.showtracker.common.EndpointPoster
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.databinding.ItemSimplePosterBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.PagingAdapterSimplePoster.ViewHolderPagingSimplePoster
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

sealed class MultiSelectMode {
    object Activated : MultiSelectMode()
    object Inactivated : MultiSelectMode()
}
// todo big issue is passing activity reference
@ExperimentalCoroutinesApi
class PagingAdapterSimplePoster(
    var activity: ActivityMain?,
    private var onPosterClick: OnPosterClickListener = OnPosterClickListener { _, _, _, _ ->  }
) : PagingDataAdapter<UIModelDiscovery, ViewHolderPagingSimplePoster>(
    UIMODEL_DISCOVERY_COMPARATOR), ActionMode.Callback{

    private var multiSelectMode: MultiSelectMode = MultiSelectMode.Inactivated
    private val selectedItems = arrayListOf<Int>()

    fun setPosterClickAction(clickListener: OnPosterClickListener) {
        onPosterClick = clickListener
    }

    override fun onBindViewHolder(holder: ViewHolderPagingSimplePoster, position: Int) {
        val data = getItem(position)
        data?.let {
            holder.bind(it)

            if (selectedItems.contains(position)) {
                Timber.d("${getItem(position)?.id} is selected")
                holder.binding.imgvItemMoviePoster.alpha = 0.3f
            } else {
                holder.binding.imgvItemMoviePoster.alpha = 1.0f
            }

            holder.binding.imgvItemMoviePoster.click {
                if (multiSelectMode is MultiSelectMode.Activated) {
                    selectItem(holder, position)
                } else {
                    onPosterClick.onClick(
                        it.id,
                        it.mediaTitle,
                        it.posterPath,
                        it.mediaType
                    )
                }
            }

            holder.binding.imgvItemMoviePoster.setOnLongClickListener {
                if (multiSelectMode is MultiSelectMode.Inactivated) {
                    multiSelectMode = MultiSelectMode.Activated
                    activity?.startSupportActionMode(this)
                    selectItem(holder, position)
                    true
                }else
                false
            }
        }
    }

    private fun selectItem(holder: ViewHolderPagingSimplePoster, position: Int) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position)
            Timber.d("Removed item with position: $position")
            holder.binding.imgvItemMoviePoster.alpha = 1.0f
        } else {
            selectedItems.add(position)
            Timber.d("Added item with position: $position")
            holder.binding.imgvItemMoviePoster.alpha = 0.3f
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderPagingSimplePoster = ViewHolderPagingSimplePoster.from(parent, onPosterClick)

    class ViewHolderPagingSimplePoster(
        val binding: ItemSimplePosterBinding,
        val onClick: OnPosterClickListener
        ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UIModelDiscovery) {
            Glide.with(binding.root)
                .load(EndpointPoster.Standard.urlWithResource(data.posterPath))
                .centerCrop()
                .error(R.drawable.error_poster)
                .transition(DrawableTransitionOptions.withCrossFade(100))
                .into(binding.imgvItemMoviePoster)
        }

        companion object {
            fun from(parent: ViewGroup, onClick: OnPosterClickListener) : ViewHolderPagingSimplePoster = ViewHolderPagingSimplePoster(
                ItemSimplePosterBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), onClick
            )
        }
    }
    companion object {
        private val UIMODEL_DISCOVERY_COMPARATOR = object : DiffUtil.ItemCallback<UIModelDiscovery>() {
            override fun areItemsTheSame(
                oldItem: UIModelDiscovery,
                newItem: UIModelDiscovery
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: UIModelDiscovery,
                newItem: UIModelDiscovery
            ): Boolean {
                return (oldItem.id == newItem.id &&
                        oldItem.mediaTitle == newItem.mediaTitle &&
                        oldItem.mediaType == newItem.mediaType &&
                        oldItem.posterPath == newItem.posterPath &&
                        oldItem.listType == newItem.listType)
            }
        }
    }

    // Called when the menu was created i.e. when the user starts multi-select mode
    // Inflate your menu xml here
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        val inflater = mode?.menuInflater
        inflater?.inflate(R.menu.menu_discovery_contextual_toolbar, menu)
        return true
    }

    // Called to refresh an action mode's action menu
    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        // here you will update the toolbar text
        return false
    }

    // Called when a menu item was clicked
    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete) {
            Timber.d("$selectedItems")
            mode?.finish()
        }
        return true
    }

    // Called when the Context ActionBar disappears i.e. when the user leaves the multi-select mode
    override fun onDestroyActionMode(mode: ActionMode?) {
        // finished multi selection
        multiSelectMode = MultiSelectMode.Inactivated
        selectedItems.clear()
        notifyDataSetChanged()
    }
}