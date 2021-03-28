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
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDoNothing
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.common.EndpointPoster
import com.sunrisekcdeveloper.showtracker.common.OnPosterClickListener
import com.sunrisekcdeveloper.showtracker.common.util.click
import com.sunrisekcdeveloper.showtracker.common.util.gone
import com.sunrisekcdeveloper.showtracker.common.util.visible
import com.sunrisekcdeveloper.showtracker.databinding.ItemWatchlistShowBinding
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.MovieWatchedStatus
import com.sunrisekcdeveloper.showtracker.features.discovery.domain.model.MediaType
import timber.log.Timber

class AdapterWatchlistShow(
    var onButtonClicked: OnShowStatusClickListener = OnShowStatusClickListener { _ -> },
    var onPosterClickListener: OnPosterClickListener = OnPosterClickListener { _, _, _, _ -> }
) : ListAdapter<UIModelWatchlistShow, AdapterWatchlistShow.ViewHolderWatchlistShow>(
    WATCHLIST_SHOW_COMPARATOR
),
    SwipeableItemAdapter<AdapterWatchlistShow.ViewHolderWatchlistShow> {

    init {
        // todo
        //  shit - this is not going to work
        //  this library requires I set stable IDs to true
        //  but this prevents me from updating list items :(
        //  This swiping idea has to be shelved for now
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        val random = (0..Int.MAX_VALUE).random()
        val uniqueId = getItem(position).dateAdded + random
        return uniqueId
    }

    fun itemAtId(position: Int): UIModelWatchlistShow {
        return currentList[position]
    }
    fun removeItem(position: Int) {
        currentList.removeAt(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderWatchlistShow =
        ViewHolderWatchlistShow.from(parent, onButtonClicked, onPosterClickListener)

    override fun onBindViewHolder(holder: ViewHolderWatchlistShow, position: Int) {
        holder.bind(getItem(position))

        val item = getItem(position)

        val swipeState = holder.swipeState

        if (swipeState.isUpdated) {
            val backgroundResId = when {
                swipeState.isActive -> {
                    R.drawable.bg_item_swiping_active_state
                }
                swipeState.isSwiping -> {
                    R.drawable.bg_item_swiping_state
                }
                else -> {
                    R.drawable.bg_item_normal_state
                }
            }
            holder.binding.container.setBackgroundResource(backgroundResId)
        }

        holder.swipeItemHorizontalSlideAmount =
            SwipeableItemConstants.OUTSIDE_OF_THE_WINDOW_LEFT ?: 0f
    }


    fun positionOfItem(showId: String): Int {
        val data = currentList
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

    override fun onGetSwipeReactionType(
        holder: ViewHolderWatchlistShow,
        position: Int,
        x: Int,
        y: Int
    ): Int {
        Timber.e("swipe reaction type with holder: ${holder.itemId}")
        // Make swipeable to LEFT direction
        return SwipeableItemConstants.REACTION_CAN_SWIPE_BOTH_H
    }

    override fun onSwipeItemStarted(holder: ViewHolderWatchlistShow, position: Int) {
        Timber.e("swipe item started")
        notifyDataSetChanged()
    }

    override fun onSetSwipeBackground(holder: ViewHolderWatchlistShow, position: Int, type: Int) {
        // You can set background color/resource to holder.itemView.

        // The argument "type" can be one of the followings;
        // - SwipeableItemConstants.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND
        // - SwipeableItemConstants.DRAWABLE_SWIPE_LEFT_BACKGROUND
        // (- SwipeableItemConstants.DRAWABLE_SWIPE_UP_BACKGROUND)
        // (- SwipeableItemConstants.DRAWABLE_SWIPE_RIGHT_BACKGROUND)
        // (- SwipeableItemConstants.DRAWABLE_SWIPE_DOWN_BACKGROUND)

        var bgInt = 0

        when (type) {
            SwipeableItemConstants.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND -> {
                Timber.e("set swipe background: neutral")
                bgInt = R.drawable.bg_swipe_item_neutral
            }
            SwipeableItemConstants.DRAWABLE_SWIPE_LEFT_BACKGROUND -> {
                Timber.e("set swipe background: left")
                bgInt = R.drawable.bg_swipe_item_left
            }
            SwipeableItemConstants.DRAWABLE_SWIPE_RIGHT_BACKGROUND -> {
                Timber.e("set swipe background: right")
                bgInt = R.drawable.bg_swipe_item_right
            }
        }

//        holder.binding.root.setBackgroundResource(bgInt)
        holder.binding.root.setBackgroundResource(bgInt)
    }

    override fun onSwipeItem(
        holder: ViewHolderWatchlistShow,
        position: Int,
        result: Int
    ): SwipeResultAction? {
        Timber.e("onSwipeItem: position=$position, result=$result")
        when (result) {
            SwipeableItemConstants.RESULT_SWIPED_RIGHT -> {
                return SwipeRightResultAction(this, position)
            }
            SwipeableItemConstants.RESULT_SWIPED_LEFT -> {
                return SwipeLeftResultAction(this, position)
            }
            else -> {
                return null
            }
        }
    }

    class ViewHolderWatchlistShow(
        val binding: ItemWatchlistShowBinding,
        private val onButtonClicked: OnShowStatusClickListener,
        private val onPosterClickListener: OnPosterClickListener
    ) : AbstractSwipeableItemViewHolder(binding.root) {

        override fun getSwipeableContainerView(): View {
            return binding.container
        }

        fun bind(item: UIModelWatchlistShow) {
            Glide.with(binding.root)
                .load(EndpointPoster.Standard.urlWithResource(item.posterPath))
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

                    // Progress indicator
                    val progress = binding.progressWatchlistShowSeasonProgress
                    progress.max = item.episodesInSeason
                    progress.progress = item.currentEpisodeNumber
                    binding.tvWatchlistShowProgressCurrentEpisode.text =
                        item.currentEpisodeNumber.toString()
                    binding.tvWatchlistShowProgressMaxEpisodes.text =
                        item.episodesInSeason.toString()

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
        private val WATCHLIST_SHOW_COMPARATOR =
            object : DiffUtil.ItemCallback<UIModelWatchlistShow>() {
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