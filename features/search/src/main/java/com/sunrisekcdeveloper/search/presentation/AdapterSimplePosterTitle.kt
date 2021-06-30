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

package com.sunrisekcdeveloper.search.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.search.EndpointBackdropStandard
import com.sunrisekcdeveloper.search.ImageLoadingContract
import com.sunrisekcdeveloper.search.OnPosterClickListener
import com.sunrisekcdeveloper.search.databinding.ItemSimplePosterAndTitleBinding
import com.sunrisekcdeveloper.search.domain.model.UIModelPoster

class AdapterSimplePosterTitle(
    private val image: ImageLoadingContract,
    private var onPosterClickListener: OnPosterClickListener = OnPosterClickListener { _, _, _, _ -> }
) : ListAdapter<UIModelPoster, AdapterSimplePosterTitle.ViewHolderSimplePosterTitle>(
    UIMODEL_POSTER_RESULT_COMPARATOR
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSimplePosterTitle =
        ViewHolderSimplePosterTitle.from(parent, image, onPosterClickListener)

    fun setPosterClickAction(clickListener: OnPosterClickListener) {
        onPosterClickListener = clickListener
    }

    override fun onBindViewHolder(holder: ViewHolderSimplePosterTitle, position: Int) {
        holder.bind((getItem(position)))
    }

    class ViewHolderSimplePosterTitle(
        val binding: ItemSimplePosterAndTitleBinding,
        private val glide: ImageLoadingContract,
        private val onPosterClickListener: OnPosterClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UIModelPoster) {
            binding.root.setOnClickListener {
                onPosterClickListener.onClick(data.id, data.title, data.posterPath, data.type)
            }
            binding.cardPoster.setOnClickListener {
                onPosterClickListener.onClick(data.id, data.title, data.posterPath, data.type)
            }
            glide.load(EndpointBackdropStandard(data.backdropPath).url(), binding.imgvItemMediaPoster)
            binding.tvMediaTitle.text = data.title
        }

        companion object {
            fun from(
                parent: ViewGroup,
                glide: ImageLoadingContract,
                posterClickListener: OnPosterClickListener
            ): ViewHolderSimplePosterTitle = ViewHolderSimplePosterTitle(
                ItemSimplePosterAndTitleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), glide, posterClickListener
            )
        }
    }

    companion object {
        private val UIMODEL_POSTER_RESULT_COMPARATOR = object : DiffUtil.ItemCallback<UIModelPoster>() {
            override fun areItemsTheSame(
                oldItem: UIModelPoster,
                newItem: UIModelPoster
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: UIModelPoster,
                newItem: UIModelPoster
            ): Boolean = oldItem.id == newItem.id &&
                    oldItem.title == newItem.title &&
                    oldItem.posterPath == newItem.posterPath &&
                    oldItem.backdropPath == newItem.backdropPath &&
                    oldItem.listType == newItem.listType &&
                    oldItem.type == newItem.type
        }
    }
}