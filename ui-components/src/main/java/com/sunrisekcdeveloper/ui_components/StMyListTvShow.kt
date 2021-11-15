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

package com.sunrisekcdeveloper.ui_components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.common.timber
import com.sunrisekcdeveloper.ui_components.databinding.MyListTvshowItemBinding
import com.sunrisekcdeveloper.ui_components.databinding.StMyListTvshowBinding

class StMyListTvShow @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
): LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = StMyListTvshowBinding.inflate(LayoutInflater.from(context), this, false)

    init {
        addView(binding.root)
    }

    fun submitData(data: List<Model>) {
        TVShowAdapter().apply {
            submitList(data)
            binding.stMyListTvshowList.adapter = this
        }
    }
}

class TVShowAdapter : ListAdapter<Model, TVShowViewHolder>(COMPARATOR) {

    private val log by timber()
    private var count = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowViewHolder {
        log.e("create viewholder ${count++}")
        return TVShowViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TVShowViewHolder, position: Int) {
        log.e("${getItem(position)}")
        holder.bind(getItem(position))
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Model>() {
            override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem.showTitle == newItem.showTitle &&
                        oldItem.episodeTitle == newItem.episodeTitle &&
                        oldItem.amountWatched == newItem.amountWatched &&
                        oldItem.totalEpisodes == newItem.totalEpisodes &&
                        oldItem.posterUrl == newItem.posterUrl
            }

            override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem.showTitle == newItem.showTitle &&
                        oldItem.episodeTitle == newItem.episodeTitle &&
                        oldItem.amountWatched == newItem.amountWatched &&
                        oldItem.totalEpisodes == newItem.totalEpisodes &&
                        oldItem.posterUrl == newItem.posterUrl
            }
        }
    }
}

class TVShowViewHolder(val binding: MyListTvshowItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Model) {
        binding.myListTvshowTitle.text = data.showTitle
        binding.myListTvshowEpisodeTitle.text = data.episodeTitle
        binding.myListTvshowProgressbar.setMax(data.totalEpisodes)
        binding.myListTvshowProgressbar.setProgress(data.amountWatched)
        binding.myListTvshowEpisodeInfo.setOnClickListener { Toast.makeText(binding.root.context, data.episodeTitle, Toast.LENGTH_SHORT).show() }
        binding.root.setOnClickListener { Toast.makeText(binding.root.context, data.showTitle, Toast.LENGTH_SHORT).show() }
    }
    companion object {
        fun from(parent: ViewGroup): TVShowViewHolder {
           return  TVShowViewHolder(MyListTvshowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }
}

data class Model(
    val posterUrl: String,
    val showTitle: String,
    val episodeTitle: String,
    val amountWatched: Int,
    val totalEpisodes: Int
)