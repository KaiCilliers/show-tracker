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

package com.sunrisekcdeveloper.showtracker.features.watchlist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.sunrisekcdeveloper.showtracker.databinding.ItemWatchlistShowBinding

class AdapterWatchlistShow(
    private val data: MutableList<UIModelWatchlistShow>
) : RecyclerView.Adapter<AdapterWatchlistShow.ViewHolderWatchlistShow>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderWatchlistShow =
        ViewHolderWatchlistShow.from(parent)

    override fun onBindViewHolder(holder: ViewHolderWatchlistShow, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun refreshData(data: List<UIModelWatchlistShow>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolderWatchlistShow(val binding: ItemWatchlistShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UIModelWatchlistShow) {
            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w342${item.posterPath}")
                .transform(CenterCrop())
                .into(binding.imgvWatchlistShowPoster)

            binding.tvWatchlistShowTitle.text = item.title

            if (!item.started) {
                binding.btnWatchlistShowStartWatching.visibility = View.VISIBLE // todo make extension function
                binding.btnWatchlistShowCurrentEpisode.visibility = View.GONE
                binding.btnWatchlistShowMarkAsWatched.visibility = View.GONE
                binding.tvWatchlistShowEpisodeName.visibility = View.GONE
            } else {
                binding.btnWatchlistShowStartWatching.visibility = View.GONE // todo make extension function
                binding.btnWatchlistShowCurrentEpisode.visibility = View.VISIBLE
                binding.btnWatchlistShowMarkAsWatched.visibility = View.VISIBLE
                binding.tvWatchlistShowEpisodeName.visibility = View.VISIBLE

                binding.btnWatchlistShowCurrentEpisode.text = "S${item.currentSeasonNumber}E${item.currentEpisodeNumber}"
                binding.tvWatchlistShowEpisodeName.text = item.currentEpisodeName
            }
        }
        companion object {
            fun from(parent: ViewGroup) = ViewHolderWatchlistShow(
                ItemWatchlistShowBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }
}

data class UIModelWatchlistShow(
    val id: String,
    val title: String,
    val posterPath: String,
    val currentEpisodeName: String,
    val currentEpisodeNumber: Int,
    val currentSeasonNumber: Int,
    val episodesInSeason: Int,
    val started: Boolean,
    val upToDate: Boolean,
    val dateAdded: Long
)