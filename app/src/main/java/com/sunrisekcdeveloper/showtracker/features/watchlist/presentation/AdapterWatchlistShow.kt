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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.util.*
import com.sunrisekcdeveloper.showtracker.databinding.ItemWatchlistShowBinding
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import timber.log.Timber

class AdapterWatchlistShow(
    var onButtonClicked: OnShowStatusClickListener = OnShowStatusClickListener { _ -> },
    var onPosterClickListener: OnPosterClickListener = OnPosterClickListener { _, _, _, _-> }
) : ListAdapter<UIModelWatchlistShow, AdapterWatchlistShow.ViewHolderWatchlistShow>(WATCHLIST_SHOW_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderWatchlistShow =
        ViewHolderWatchlistShow.from(parent, onButtonClicked, onPosterClickListener)

    override fun onBindViewHolder(holder: ViewHolderWatchlistShow, position: Int) {
        holder.bind(getItem(position))
    }


    fun positionOfItem(showId: String): Int {
        Timber.d("I want the position of the show with ID: $showId")
        val data = currentList
        Timber.d("data: ${data.map { "${it.id}: ${it.title}" }}")
        val item = data.find {
            it.id == showId
        }
        Timber.e("item found: $item")
        var position = -1
        item?.let {
            position = data.indexOf(item)
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
            Glide.with(binding.root)
                .load(EndpointPosterStandard(item.posterPath).url())
                .centerCrop()
                .error(R.drawable.error_poster)
                .transition(DrawableTransitionOptions.withCrossFade(100))
                .into(binding.imgvWatchlistShowPoster)

            binding.imgvWatchlistShowPoster.click {
                onPosterClickListener.onClick(
                    item.id,
                    item.title,
                    item.posterPath,
                    MediaType.Show
                )
            }

            binding.tvWatchlistShowTitle.text = item.title

            binding.btnWatchlistShowStartWatching.click {
                Timber.e("adapter - start watching...")
                onButtonClicked.onClick(ShowAdapterAction.StartWatchingShow(item.id, item.title))
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
                Timber.e("current episode: ${item.currentEpisodeNumber} and last episode: ${item.lastEpisodeInSeason} (total episodes in season ${item.currentSeasonNumber} is ${item.episodesInSeason})")
                if (item.currentEpisodeNumber == item.lastEpisodeInSeason) {
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
                binding.tvWatchlistShowProgressCurrentEpisode.gone()
                binding.tvWatchlistShowSeperator.gone()
                binding.tvWatchlistShowProgressMaxEpisodes.gone()
                binding.progressWatchlistShowSeasonProgress.gone()
                binding.tvWatchlistShowUpToDate.visible()
                binding.btnWatchlistShowStartWatching.gone()
                binding.btnWatchlistShowCurrentEpisode.gone()
                binding.btnWatchlistShowMarkAsWatched.gone()
                binding.tvWatchlistShowEpisodeName.gone()
            } else {
                binding.tvWatchlistShowUpToDate.gone()
                if (!item.started) {
                    binding.tvWatchlistShowSeperator.gone()
                    binding.btnWatchlistShowStartWatching.visible()
                    binding.btnWatchlistShowCurrentEpisode.gone()
                    binding.btnWatchlistShowMarkAsWatched.gone()
                    binding.tvWatchlistShowEpisodeName.gone()
                } else {
                    binding.progressWatchlistShowSeasonProgress.visible()
                    binding.tvWatchlistShowProgressCurrentEpisode.visible()
                    binding.tvWatchlistShowSeperator.visible()
                    binding.tvWatchlistShowProgressMaxEpisodes.visible()
                    binding.btnWatchlistShowStartWatching.gone()
                    binding.btnWatchlistShowCurrentEpisode.visible()
                    binding.btnWatchlistShowMarkAsWatched.visible()
                    binding.tvWatchlistShowEpisodeName.visible()

//                    item.
//                        @643
//                        @692
//                            kort 3/50
//                            645/692/50

                    // Progress indicator
                    val progress = binding.progressWatchlistShowSeasonProgress
                    progress.max = item.episodesInSeason
                    progress.progress = item.episodesInSeason - (item.lastEpisodeInSeason - item.currentEpisodeNumber)
                    binding.tvWatchlistShowProgressCurrentEpisode.text = item.currentEpisodeNumber.toString()
                    binding.tvWatchlistShowProgressMaxEpisodes.text = item.lastEpisodeInSeason.toString()

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

    companion object {
        private val WATCHLIST_SHOW_COMPARATOR = object : DiffUtil.ItemCallback<UIModelWatchlistShow>() {
            override fun areItemsTheSame(
                oldItem: UIModelWatchlistShow,
                newItem: UIModelWatchlistShow
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: UIModelWatchlistShow,
                newItem: UIModelWatchlistShow
            ): Boolean {
                return (oldItem.id == newItem.id &&
                        oldItem.title == newItem.title &&
                        oldItem.posterPath == newItem.posterPath &&
                        oldItem.currentEpisodeName == newItem.currentEpisodeName &&
                        oldItem.currentEpisodeNumber == newItem.currentEpisodeNumber &&
                        oldItem.currentSeasonNumber == newItem.currentSeasonNumber &&
                        oldItem.started == newItem.started &&
                        oldItem.upToDate == newItem.upToDate &&
                        oldItem.dateAdded == newItem.dateAdded)
            }
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
    val lastEpisodeInSeason: Int,
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
        val showId: String,
        val title: String
    ) : ShowAdapterAction()
}