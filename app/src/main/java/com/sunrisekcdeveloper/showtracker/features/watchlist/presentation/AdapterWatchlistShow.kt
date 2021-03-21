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
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.databinding.ItemWatchlistShowBinding
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.MovieWatchedStatus
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import timber.log.Timber

class AdapterWatchlistShow(
    private val data: MutableList<UIModelWatchlistShow>,
    var onButtonClicked: OnShowStatusClickListener = OnShowStatusClickListener { _ -> },
    var onPosterClickListener: OnPosterClickListener = OnPosterClickListener { _, _ -> }
) : RecyclerView.Adapter<AdapterWatchlistShow.ViewHolderWatchlistShow>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderWatchlistShow =
        ViewHolderWatchlistShow.from(parent, onButtonClicked, onPosterClickListener)

    override fun onBindViewHolder(holder: ViewHolderWatchlistShow, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun refreshData(data: List<UIModelWatchlistShow>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun positionOfItem(showId: String): Int {
        Timber.e("adapter show id: $showId")
        Timber.e("data: ${this.data}")
        val item = this.data.find {
            it.id == showId
        }
        Timber.e("item found: $item")
        var position = -1
        item?.let {
            position = this.data.indexOf(item)
            Timber.e("position found $position")
        }
        return position
    }

    class ViewHolderWatchlistShow(
        val binding: ItemWatchlistShowBinding,
        private val onButtonClicked: OnShowStatusClickListener,
        private val onPosterClickListener: OnPosterClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UIModelWatchlistShow) {
            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w342${item.posterPath}")
                .transform(CenterCrop())
                .into(binding.imgvWatchlistShowPoster)

            binding.imgvWatchlistShowPoster.click {
                onPosterClickListener.onClick(item.id, MediaType.Show)
            }

            binding.tvWatchlistShowTitle.text = item.title

            binding.btnWatchlistShowStartWatching.click {
                Timber.e("adapter - start watching...")
                onButtonClicked.onClick(ShowAdapterAction.StartWatchingShow(item.id))
            }

            binding.btnWatchlistShowCurrentEpisode.click {
                Timber.e("adapter - mark season...")
                onButtonClicked.onClick(
                    ShowAdapterAction.MarkSeason(
                        item.id,
                        item.currentSeasonNumber
                    )
                )
            }

            binding.btnWatchlistShowMarkAsWatched.click {
                Timber.e("current episode: ${item.currentEpisodeNumber} and total episodes in season ${item.currentSeasonNumber} is ${item.episodesInSeason}")
                if (item.currentEpisodeNumber == item.episodesInSeason) {
                    Timber.e("adapter - episode mark season...")
                    onButtonClicked.onClick(
                        ShowAdapterAction.MarkSeason(
                            item.id,
                            item.currentSeasonNumber
                        )
                    )
                } else {
                    Timber.e("adapter - mark episode...")
                    onButtonClicked.onClick(
                        ShowAdapterAction.MarkEpisode(
                            item.id,
                            item.currentSeasonNumber,
                            item.currentEpisodeNumber
                        )
                    )
                }
            }

            if (item.upToDate) {
                binding.tvWatchlistShowUpToDate.visibility = View.VISIBLE
                binding.btnWatchlistShowStartWatching.visibility = View.GONE
                binding.btnWatchlistShowCurrentEpisode.visibility = View.GONE
                binding.btnWatchlistShowMarkAsWatched.visibility = View.GONE
                binding.tvWatchlistShowEpisodeName.visibility = View.GONE
            } else {
                binding.tvWatchlistShowUpToDate.visibility = View.GONE
                if (!item.started) {
                    binding.btnWatchlistShowStartWatching.visibility =
                        View.VISIBLE // todo make extension function
                    binding.btnWatchlistShowCurrentEpisode.visibility = View.GONE
                    binding.btnWatchlistShowMarkAsWatched.visibility = View.GONE
                    binding.tvWatchlistShowEpisodeName.visibility = View.GONE
                } else {
                    binding.btnWatchlistShowStartWatching.visibility =
                        View.GONE // todo make extension function
                    binding.btnWatchlistShowCurrentEpisode.visibility = View.VISIBLE
                    binding.btnWatchlistShowMarkAsWatched.visibility = View.VISIBLE
                    binding.tvWatchlistShowEpisodeName.visibility = View.VISIBLE

                    binding.btnWatchlistShowCurrentEpisode.text =
                        "S${item.currentSeasonNumber}E${item.currentEpisodeNumber}"
                    binding.tvWatchlistShowEpisodeName.text = item.currentEpisodeName
                }
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onButtonClicked: OnShowStatusClickListener,
                posterClickListener: OnPosterClickListener
            ) = ViewHolderWatchlistShow(
                ItemWatchlistShowBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), onButtonClicked, posterClickListener
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

// todo better name
fun interface OnShowStatusClickListener {
    fun onClick(action: ShowAdapterAction)
}

sealed class ShowAdapterAction {
    data class MarkEpisode(
        val showId: String,
        val seasonNumber: Int,
        val episodeNumber: Int
    ) : ShowAdapterAction()

    data class MarkSeason(
        val showId: String,
        val seasonNumber: Int
    ) : ShowAdapterAction()

    data class StartWatchingShow(
        val showId: String
    ) : ShowAdapterAction()
}