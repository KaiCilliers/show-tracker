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
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.common.idk.ImageLoadingContract
import com.sunrisekcdeveloper.showtracker.common.idk.ImageLoadingStandardGlide
import com.sunrisekcdeveloper.showtracker.common.util.EndpointPosterStandard
import com.sunrisekcdeveloper.showtracker.common.util.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.databinding.ItemSimplePosterBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.PagingAdapterSimplePoster.ViewHolderPagingSimplePoster

class PagingAdapterSimplePoster(
    private val image: ImageLoadingContract,
    private var onPosterClick: OnPosterClickListener = OnPosterClickListener { _, _, _, _ -> }
) : PagingDataAdapter<UIModelDiscovery, ViewHolderPagingSimplePoster>(
    UIMODEL_DISCOVERY_COMPARATOR
) {

    fun setPosterClickAction(clickListener: OnPosterClickListener) {
        onPosterClick = clickListener
    }

    override fun onBindViewHolder(holder: ViewHolderPagingSimplePoster, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderPagingSimplePoster = ViewHolderPagingSimplePoster.from(parent, image, onPosterClick)

    class ViewHolderPagingSimplePoster(
        private val binding: ItemSimplePosterBinding,
        private val image: ImageLoadingContract,
        private val onClick: OnPosterClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UIModelDiscovery) {
            image.load(EndpointPosterStandard(data.posterPath).url(), binding.imgvItemMoviePoster)

            binding.root.click {
                onClick.onClick(
                    data.id,
                    data.mediaTitle,
                    data.posterPath,
                    data.mediaType
                )
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
                image: ImageLoadingContract,
                onClick: OnPosterClickListener
            ): ViewHolderPagingSimplePoster = ViewHolderPagingSimplePoster(
                ItemSimplePosterBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), image, onClick
            )
        }
    }

    companion object {
        private val UIMODEL_DISCOVERY_COMPARATOR =
            object : DiffUtil.ItemCallback<UIModelDiscovery>() {
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
}