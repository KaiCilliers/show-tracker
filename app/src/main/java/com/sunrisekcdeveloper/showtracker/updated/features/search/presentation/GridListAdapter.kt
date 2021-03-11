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

package com.sunrisekcdeveloper.showtracker.updated.features.search.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.sunrisekcdeveloper.showtracker.databinding.ItemSimplePosterBinding
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.presentation.OnPosterClickListener

class GridListAdapter(
    private var data: MutableList<SearchUIModel>,
    var onPosterClickListener: OnPosterClickListener = OnPosterClickListener { mediaId, mediaType ->  }
) : RecyclerView.Adapter<GridListAdapter.HorizontalPosterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalPosterViewHolder =
        HorizontalPosterViewHolder.from(parent)

    override fun onBindViewHolder(holder: HorizontalPosterViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.binding.imgvItemMoviePoster.setOnClickListener {
            onPosterClickListener.onClick(item.id, item.mediaType)
        }
    }

    override fun getItemCount(): Int = data.size

    fun updateList(data: List<SearchUIModel>) {
        this.data.addAll(data)
        notifyItemRangeInserted(
            this.data.size,
            data.size - 1
        )
    }

    class HorizontalPosterViewHolder(val binding: ItemSimplePosterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SearchUIModel) {
            Glide.with(binding.root)
                .load("https://image.tmdb.org/t/p/w342${data.posterPath}")
                .transform(CenterCrop())
                .into(binding.imgvItemMoviePoster)
        }
        companion object {
            fun from(parent: ViewGroup) : HorizontalPosterViewHolder = HorizontalPosterViewHolder(
                ItemSimplePosterBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }
}