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

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.idk.ImageLoadingContract
import com.sunrisekcdeveloper.showtracker.common.util.EndpointPosterStandard
import com.sunrisekcdeveloper.showtracker.common.util.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.common.util.fetchPrimaryColor
import com.sunrisekcdeveloper.showtracker.databinding.ItemWatchlistMovieBinding
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.MovieWatchedStatus
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.UIModelWatchlisMovie

class AdapterWatchlistMovie(
    private val image: ImageLoadingContract,
    var onButtonClicked: OnMovieStatusClickListener = OnMovieStatusClickListener{_, _,_ -> },
    var onPosterClickListener: OnPosterClickListener = OnPosterClickListener { _, _, _, _ -> }
) : ListAdapter<UIModelWatchlisMovie, AdapterWatchlistMovie.ViewHolderWatchlistMovie>(WATCHLIST_MOVIE_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderWatchlistMovie =
        ViewHolderWatchlistMovie.from(parent, image, onButtonClicked, onPosterClickListener)

    override fun onBindViewHolder(holder: ViewHolderWatchlistMovie, position: Int) {
        holder.bind(getItem(position))
    }

    // todo take note
    //  access onClick from adapter straight when VH is marked as inner
    //  but then you can't have companion object for #from method
    class ViewHolderWatchlistMovie(
        val binding: ItemWatchlistMovieBinding,
        private val image: ImageLoadingContract,
        private val onButtonClicked: OnMovieStatusClickListener,
        private val onPosterClickListener: OnPosterClickListener
        ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UIModelWatchlisMovie) {
            binding.tvWatchlistMovieTitle.text = item.title
            binding.tvWatchlistMovieOverview.text = item.overview
            if (item.watched) {
                binding.btnWatchlistWatchedStatus.text = binding.root.context.getString(R.string.already_watched)
                binding.btnWatchlistWatchedStatus.setBackgroundColor(Color.GRAY)
                binding.btnWatchlistWatchedStatus.click {
                    onButtonClicked.onClick(item.id, item.title, MovieWatchedStatus.Watched)
                }
            } else {
                binding.btnWatchlistWatchedStatus.text = binding.root.context.getString(R.string.mark_as_watched)
                binding.btnWatchlistWatchedStatus.setBackgroundColor(fetchPrimaryColor(binding.root.context))
                binding.btnWatchlistWatchedStatus.click {
                    onButtonClicked.onClick(item.id, item.title, MovieWatchedStatus.NotWatched)
                }
            }
            image.load(EndpointPosterStandard(item.posterPath).url(), binding.imgvWatchlistMoviePoster)
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
                image: ImageLoadingContract,
                clickAction: OnMovieStatusClickListener,
                posterClick: OnPosterClickListener
            ) : ViewHolderWatchlistMovie = ViewHolderWatchlistMovie(
                ItemWatchlistMovieBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), image, clickAction, posterClick
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
    fun onClick(mediaId: String, title: String, watchStatus: MovieWatchedStatus)
}