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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.sunrisekcdeveloper.showtracker.common.EndpointPoster
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.databinding.ItemWatchlistMovieBinding
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.MovieWatchedStatus
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.UIModelWatchlisMovie

class AdapterWatchlistMovie(
    var onButtonClicked: OnMovieStatusClickListener = OnMovieStatusClickListener{_, _ -> },
    var onPosterClickListener: OnPosterClickListener = OnPosterClickListener {_, _, _, _ -> }
) : ListAdapter<UIModelWatchlisMovie, AdapterWatchlistMovie.ViewHolderWatchlistMovie>(WATCHLIST_MOVIE_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderWatchlistMovie =
        ViewHolderWatchlistMovie.from(parent, onButtonClicked, onPosterClickListener)

    override fun onBindViewHolder(holder: ViewHolderWatchlistMovie, position: Int) {
        holder.bind(getItem(position))
    }

    // todo take note
    //  access onClick from adapter straight when VH is marked as inner
    //  but then you can't have companion object for #from method
    class ViewHolderWatchlistMovie(
        val binding: ItemWatchlistMovieBinding,
        private val onButtonClicked: OnMovieStatusClickListener,
        private val onPosterClickListener: OnPosterClickListener
        ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UIModelWatchlisMovie) {
            binding.tvWatchlistMovieTitle.text = item.title
            binding.tvWatchlistMovieOverview.text = item.overview
            if (item.watched) {
                binding.btnWatchlistWatchedStatus.text = "Already Watched"
                binding.btnWatchlistWatchedStatus.click {
                    onButtonClicked.onClick(item.id, MovieWatchedStatus.Watched)
                }
            } else {
                binding.btnWatchlistWatchedStatus.text = "Mark as Watched"
                binding.btnWatchlistWatchedStatus.click {
                    onButtonClicked.onClick(item.id, MovieWatchedStatus.NotWatched)
                }
            }
            Glide.with(binding.root)
                    // todo update all Glide calls with this enum
                .load(EndpointPoster.Standard.urlWithResource(item.posterPath))
                .transform(CenterCrop())
                .into(binding.imgvWatchlistMoviePoster)

            binding.imgvWatchlistMoviePoster.click {
                onPosterClickListener.onClick(
                    item.id,
                    item.title,
                    item.posterPath,
                    MediaType.Movie
                )
            }
        }
        // todo this button click action passing is silly
        companion object {
            fun from(
                parent: ViewGroup,
                clickAction: OnMovieStatusClickListener,
                posterClick: OnPosterClickListener
            ) : ViewHolderWatchlistMovie = ViewHolderWatchlistMovie(
                ItemWatchlistMovieBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), clickAction, posterClick
            )
        }
    }
    companion object {
        private val WATCHLIST_MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<UIModelWatchlisMovie>() {
            override fun areItemsTheSame(
                oldItem: UIModelWatchlisMovie,
                newItem: UIModelWatchlisMovie
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: UIModelWatchlisMovie,
                newItem: UIModelWatchlisMovie
            ): Boolean {
                return (oldItem.title == newItem.title &&
                        oldItem.id == newItem.id &&
                        oldItem.dateAdded == newItem.dateAdded &&
                        oldItem.lastUpdated == newItem.lastUpdated &&
                        oldItem.watched == newItem.watched &&
                        oldItem.overview == newItem.overview &&
                        oldItem.dateWatched == newItem.dateWatched &&
                        oldItem.posterPath == newItem.posterPath)
            }
        }
    }
}

// todo better name
fun interface OnMovieStatusClickListener {
    fun onClick(mediaId: String, watchStatus: MovieWatchedStatus)
}