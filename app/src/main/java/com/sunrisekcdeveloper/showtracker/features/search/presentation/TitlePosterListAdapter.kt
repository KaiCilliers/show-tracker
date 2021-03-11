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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.sunrisekcdeveloper.showtracker.databinding.ItemSimplePosterAndTitleBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.presentation.OnPosterClickListener

class TitlePosterListAdapter(
    private var data: MutableList<SearchUIModel>,
    var onPosterClickListener: OnPosterClickListener = OnPosterClickListener { mediaId, mediaType ->  }
) : RecyclerView.Adapter<TitlePosterListAdapter.TitlePosterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitlePosterViewHolder =
        TitlePosterViewHolder.from(parent)

    override fun onBindViewHolder(holder: TitlePosterViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.binding.cardPoster.setOnClickListener {
            onPosterClickListener.onClick(item.id, item.mediaType)
        }
    }

    override fun getItemCount(): Int = data.size

    fun updateList(data: List<SearchUIModel>) {
        this.data.addAll(data)
        notifyItemRangeInserted(
            this.data.size,
            data.size -1
        )
    }

    // inner class benefits from being able to reference parent (even private variables)
    class TitlePosterViewHolder(val binding: ItemSimplePosterAndTitleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SearchUIModel) {
            Glide.with(binding.root)
                .load("https://image.tmdb.org/t/p/w342${data.posterPath}")
                .transform(CenterCrop())
                .into(binding.imgvItemMediaPoster)
            binding.tvMediaTitle.text = data.title
        }
        companion object {
            fun from(parent: ViewGroup): TitlePosterViewHolder = TitlePosterViewHolder(
                ItemSimplePosterAndTitleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }
}