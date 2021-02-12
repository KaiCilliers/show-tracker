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

package com.sunrisekcdeveloper.showtracker.features.watchlist.adapters

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.commons.components.viewholders.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.commons.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemSmallPosterBinding
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie
import timber.log.Timber

/**
 * Small Poster ViewHolder represents a small poster icon that represents a movie or show
 *
 * @property binding is the auto generated binding object that represents the associated item layout
 * @property clickAction is the action executed when the [binding] object is clicked
 */
class SmallPosterViewHolder(
    private val binding: RcItemSmallPosterBinding,
    private val clickAction: ClickActionContract
) : BaseViewHolder<Movie>(binding) {

    override fun bind(item: Movie) {
        binding.movie = item
        binding.clickListener = clickAction

        /** IMAGES */
//        @Headers("Fanart-Api: true")
//        @GET("${BuildConfig.FANART_BASE_URL}movies/{id}?api_key=${BuildConfig.FANART_API_KEY}")
//        override suspend fun poster(@Path("id") id: String): ResponseImages

        /**
         * Applications that use the same resource multiple times in multiple sizes and are
         * willing to trade off some speed and disk space in return for lower bandwidth usage
         * may want to consider enabling disk cache.
         */
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

//        val s = """
//            ${BuildConfig.FANART_BASE_URL}movies/${item.tmdb}?api_key=${BuildConfig.FANART_API_KEY}
//        """.trimIndent()

//        Timber.e(s)

        val urls = item.posterUrl.split(";")
//        Timber.e("${item.title}")
//        Timber.e("$urls")

        if (urls.size > 2) {
            Glide.with(binding.root.context)
                .load(urls[0])
                .placeholder(R.drawable.wanted_poster)
                // try a differenct glid image
                .error(
//                   R.drawable.error_poster
                           Glide.with(binding.root.context)
                       .load(urls[1])
                       .error(
                           Glide.with(binding.root.context)
                               .load(urls[2])
                               .error(R.drawable.error_poster)
                       )
                )
                .into(binding.imgvMovieSmallPoster)
        } else if(urls.size > 1) {
            Glide.with(binding.root.context)
                .load(urls[0])
                .placeholder(R.drawable.wanted_poster)
                // try a differenct glid image
                .error(
//                   R.drawable.error_poster
                    Glide.with(binding.root.context)
                        .load(urls[1])
                        .error(R.drawable.error_poster)
                )
                .into(binding.imgvMovieSmallPoster)
        }else {
            Glide.with(binding.root.context)
                .load(urls[0])
                .placeholder(R.drawable.wanted_poster)
                // try a differenct glid image
                .error(R.drawable.error_poster)
                .into(binding.imgvMovieSmallPoster)

        }


//        Glide.with(binding.imgvMovieSmallPoster).load(
//            item.image)
////            .diskCacheStrategy(DiskCacheStrategy.NONE)
////            .skipMemoryCache(true)
//            .into(binding.imgvMovieSmallPoster)
//placeholder
        // https://m.media-amazon.com/images/I/51c5hmrBLmL._AC_.jpg
        binding.executePendingBindings()
    }
}