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
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.databinding.ItemSimplePosterBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelPoster

@Deprecated("Replaced with PagingAdapterSimplePoster class")
class AdapterSimplePoster(
    var onPosterClickListener: OnPosterClickListener = OnPosterClickListener { _, _, _, _ ->  }
) : ListAdapter<UIModelPoster, AdapterSimplePoster.ViewHolderSimplePoster>(POSTER_MODEL_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSimplePoster =
        ViewHolderSimplePoster.from(parent, onPosterClickListener)

    override fun onBindViewHolder(holder: ViewHolderSimplePoster, position: Int) {
        holder.bind(getItem(position))
    }

//    @Deprecated("updated to ListAdapter, call it's submit function")
//    fun updateList(data: List<UIModelPoster>) {
//        this.data.addAll(data)
//        notifyItemRangeInserted(
//            this.data.size,
//            data.size - 1
//        )
//    }

//    fun replaceList(data: List<UIModelPoster>) {
//        this.data.clear()
//        updateList(data)
//        notifyDataSetChanged()
//    }

    class ViewHolderSimplePoster(
        val binding: ItemSimplePosterBinding,
        val click: OnPosterClickListener
        ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UIModelPoster) {
            // todo navigating from search activity to detail screen or watchlist is going to be hacky most likely
            binding.imgvItemMoviePoster.click {
                click.onClick(data.id, "todo Title", data.posterPath, data.mediaType)
            }
            Glide.with(binding.root)
                .load("https://image.tmdb.org/t/p/w342${data.posterPath}")
                .transform(CenterCrop())
                .into(binding.imgvItemMoviePoster)
        }
        companion object {
            fun from(
                parent: ViewGroup,
                posterClick: OnPosterClickListener
            ) : ViewHolderSimplePoster = ViewHolderSimplePoster(
                ItemSimplePosterBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), posterClick
            )
        }
    }
    companion object {
        private val POSTER_MODEL_COMPARATOR = object : DiffUtil.ItemCallback<UIModelPoster>() {
            override fun areItemsTheSame(oldItem: UIModelPoster, newItem: UIModelPoster): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: UIModelPoster,
                newItem: UIModelPoster
            ): Boolean {
                return (oldItem.id == newItem.id &&
                        oldItem.mediaType == newItem.mediaType &&
                        oldItem.posterPath == newItem.posterPath)
            }
        }
    }
}