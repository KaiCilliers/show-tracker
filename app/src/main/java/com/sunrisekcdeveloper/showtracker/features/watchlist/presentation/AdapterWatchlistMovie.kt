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
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.databinding.FragmentSearchBinding
import com.sunrisekcdeveloper.showtracker.databinding.ItemWatchlistMovieBinding
import timber.log.Timber

class AdapterWatchlistMovie(
    private val data: MutableList<UIModelWatchlisMovie>
) : RecyclerView.Adapter<AdapterWatchlistMovie.ViewHolderWatchlistMovie>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderWatchlistMovie =
        ViewHolderWatchlistMovie.from(parent)

    override fun onBindViewHolder(holder: ViewHolderWatchlistMovie, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun refreshList(data: List<UIModelWatchlisMovie>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolderWatchlistMovie(val binding: ItemWatchlistMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UIModelWatchlisMovie) {
            binding.tvWatchlistMovieTitle.text = item.title
            binding.tvWatchlistMovieOverview.text = item.overview
            if (item.watched) {
                binding.btnWatchlistWatchedStatus.text = "Already Watched"
                binding.btnWatchlistWatchedStatus.click {
                    Timber.e("Text is already watched...")
                }
            } else {
                binding.btnWatchlistWatchedStatus.text = "Mark as Watched"
                binding.btnWatchlistWatchedStatus.click {
                    Timber.e("Text is mark as watched...")
                }
            }
            Glide.with(binding.root)
                .load("https://image.tmdb.org/t/p/w342${item.posterPath}")
                .transform(CenterCrop())
                .into(binding.imgvWatchlistMoviePoster)
        }
        companion object {
            fun from(parent: ViewGroup) : ViewHolderWatchlistMovie = ViewHolderWatchlistMovie(
                ItemWatchlistMovieBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }
}