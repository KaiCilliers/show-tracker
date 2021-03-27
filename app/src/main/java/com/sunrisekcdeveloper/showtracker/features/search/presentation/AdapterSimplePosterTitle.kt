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

package com.sunrisekcdeveloper.showtracker.features.search.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.EndPointBackdrop
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.databinding.ItemSimplePosterAndTitleBinding
import com.sunrisekcdeveloper.showtracker.features.search.domain.model.UIModelUnwatchedSearch

class AdapterSimplePosterTitle(
    private val glide: RequestManager,
    var onPosterClickListener: OnPosterClickListener = OnPosterClickListener { _, _, _, _ ->  }
) : ListAdapter<UIModelUnwatchedSearch, AdapterSimplePosterTitle.ViewHolderSimplePosterTitle>(
    UNWATCHED_SEARCH_MODEL_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSimplePosterTitle =
        ViewHolderSimplePosterTitle.from(parent, glide, onPosterClickListener)

    override fun onBindViewHolder(holder: ViewHolderSimplePosterTitle, position: Int) {
        holder.bind((getItem(position)))
    }

    class ViewHolderSimplePosterTitle(
        val binding: ItemSimplePosterAndTitleBinding,
        private val glide: RequestManager,
        private val onPosterClickListener: OnPosterClickListener
        ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UIModelUnwatchedSearch) {
            binding.imgvItemMediaPoster.click {
                onPosterClickListener.onClick(data.id, data.title, data.backdropPath, data.mediaType)
            }
            glide.load(EndPointBackdrop.Standard.urlFromResource(data.backdropPath))
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(150))
                .into(binding.imgvItemMediaPoster)
            binding.tvMediaTitle.text = data.title
        }
        companion object {
            fun from(
                parent: ViewGroup,
                glide: RequestManager,
                posterClickListener: OnPosterClickListener
            ): ViewHolderSimplePosterTitle = ViewHolderSimplePosterTitle(
                ItemSimplePosterAndTitleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), glide, posterClickListener
            )
        }
    }
    companion object {
        private val UNWATCHED_SEARCH_MODEL_COMPARATOR =
            object : DiffUtil.ItemCallback<UIModelUnwatchedSearch>() {
                override fun areItemsTheSame(
                    oldItem: UIModelUnwatchedSearch,
                    newItem: UIModelUnwatchedSearch
                ): Boolean = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: UIModelUnwatchedSearch,
                    newItem: UIModelUnwatchedSearch
                ): Boolean {
                    // todo don't include comparing MediaType objects, i am not sure about the behaviour of comparing Objects
                    return (oldItem.id == newItem.id &&
                            oldItem.title == newItem.title &&
                            oldItem.backdropPath == newItem.backdropPath)
                }
            }
    }
}