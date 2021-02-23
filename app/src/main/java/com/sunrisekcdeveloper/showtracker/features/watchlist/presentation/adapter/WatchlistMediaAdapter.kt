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

package com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.MediaModelSealed

class WatchlistMediaAdapter(
    private var media: MutableList<MediaModelSealed>,
    var onMediaClicked: (movie: MediaModelSealed) -> Unit = {}
) : RecyclerView.Adapter<WatchlistMediaAdapter.MediaViewHolder>() {

    private var all = mutableListOf<MediaModelSealed>()
    private var filtered = mutableListOf<MediaModelSealed>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie_tmdb, parent, false)
        return MediaViewHolder(view)
    }

    override fun getItemCount(): Int = media.size

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(media[position])
    }

    fun filter(filter: FilterListBy) {
        if (all.isEmpty()) {
            all.addAll(media)
        }
        filtered = when (filter) {
            FilterListBy.OnlyMovies -> {
                all.filterIsInstance<MediaModelSealed.MovieModel>().toMutableList()
            }
            FilterListBy.OnlyShows -> {
                all.filterIsInstance<MediaModelSealed.ShowModel>().toMutableList()
            }
            FilterListBy.None -> {
                all
            }
        }
        updateList(filtered)
    }

    // todo impl DiffUtil
    //  also find different way to fix bug (watchlist data needs to be updated when back is pressed from detail fragment)
    //  possible solution is Flow results from database - since we are fetching data from single table
    fun updateList(media: List<MediaModelSealed>) {
        if (this.media.size > 0) this.media.clear() // prevents duplicate list contents
        this.media.addAll(media)
        notifyDataSetChanged()
    }

    inner class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val poster: ImageView = itemView.findViewById(R.id.imgv_item_movie_poster)
        private val fab: FloatingActionButton = itemView.findViewById(R.id.fab_add)

        fun bind(item: MediaModelSealed) {
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${item.posterPath}")
                .transform(CenterCrop())
                .into(poster)
            poster.setOnClickListener { onMediaClicked(item) }
            fab.visibility = View.GONE
            itemView.setOnLongClickListener {
                when (itemView) {
                    is MaterialCardView -> {
                        itemView.isChecked = !itemView.isChecked
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }
}

sealed class FilterListBy {
    object OnlyMovies: FilterListBy()
    object OnlyShows: FilterListBy()
    object None: FilterListBy()
}