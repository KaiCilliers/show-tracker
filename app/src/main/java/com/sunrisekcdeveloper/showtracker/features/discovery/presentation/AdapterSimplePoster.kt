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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.databinding.ItemSimplePosterBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelDiscovery
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.UIModelPoster

class AdapterSimplePoster(
    private var data: MutableList<UIModelPoster>,
    var onPosterClickListener: OnPosterClickListener = OnPosterClickListener { _, _ ->  }
) : RecyclerView.Adapter<AdapterSimplePoster.ViewHolderSimplePoster>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSimplePoster =
        ViewHolderSimplePoster.from(parent)

    override fun onBindViewHolder(holder: ViewHolderSimplePoster, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.binding.imgvItemMoviePoster.click {
            onPosterClickListener.onClick(item.id, item.mediaType)
        }
    }

    override fun getItemCount(): Int = data.size

    fun updateList(data: List<UIModelPoster>) {
        this.data.addAll(data)
        notifyItemRangeInserted(
            this.data.size,
            data.size - 1
        )
    }

    fun replaceList(data: List<UIModelPoster>) {
        this.data.clear()
        updateList(data)
    }

    class ViewHolderSimplePoster(val binding: ItemSimplePosterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UIModelPoster) {
            Glide.with(binding.root)
                .load("https://image.tmdb.org/t/p/w342${data.posterPath}")
                .transform(CenterCrop())
                .into(binding.imgvItemMoviePoster)
        }
        companion object {
            fun from(parent: ViewGroup) : ViewHolderSimplePoster = ViewHolderSimplePoster(
                ItemSimplePosterBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }
}