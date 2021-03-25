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
import com.sunrisekcdeveloper.showtracker.common.EndPointBackdrop
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.databinding.ItemSimplePosterAndTitleBinding
import com.sunrisekcdeveloper.showtracker.features.search.domain.domain.UIModelUnwatchedSearch
import timber.log.Timber

// todo DiffUtil
class AdapterSimplePosterTitle(
    private var data: MutableList<UIModelUnwatchedSearch>,
    var onPosterClickListener: OnPosterClickListener = OnPosterClickListener { _, _, _, _ ->  }
) : RecyclerView.Adapter<AdapterSimplePosterTitle.ViewHolderSimplePosterTitle>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSimplePosterTitle =
        ViewHolderSimplePosterTitle.from(parent)

    override fun onBindViewHolder(holder: ViewHolderSimplePosterTitle, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.binding.cardPoster.setOnClickListener {
            onPosterClickListener.onClick(item.id, "", "", item.mediaType)
        }
    }

    override fun getItemCount(): Int = data.size

    fun updateList(data: List<UIModelUnwatchedSearch>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    // inner class benefits from being able to reference parent (even private variables)
    class ViewHolderSimplePosterTitle(val binding: ItemSimplePosterAndTitleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UIModelUnwatchedSearch) {
            Glide.with(binding.root)
                .load(EndPointBackdrop.Standard.urlFromResource(data.backdropPath))
                .transform(CenterCrop())
                .into(binding.imgvItemMediaPoster)
            binding.tvMediaTitle.text = data.title
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolderSimplePosterTitle = ViewHolderSimplePosterTitle(
                ItemSimplePosterAndTitleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }
}