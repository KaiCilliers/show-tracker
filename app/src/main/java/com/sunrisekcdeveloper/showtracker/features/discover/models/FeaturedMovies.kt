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

package com.sunrisekcdeveloper.showtracker.features.discover.models

import androidx.room.Embedded
import androidx.room.Relation
import com.sunrisekcdeveloper.showtracker.models.local.core.MovieEntity
import com.sunrisekcdeveloper.showtracker.models.roomresults.FeaturedList
import com.sunrisekcdeveloper.showtracker.models.roomresults.Movie

data class FeaturedMovies(
    @Embedded
    val data: FeaturedEntity,
    @Relation(
        parentColumn = "fk_featured_media_slug",
        entityColumn = "movie_slug"
    )
    val movie: MovieEntity?
) {
    companion object {
        fun featuredListOf(movies: List<FeaturedMovies>): List<FeaturedList> {
            val tags = hashSetOf<String>()
            movies.forEach { tags.add(it.data.tag) }
            val list = mutableListOf<FeaturedList>()
            tags.forEach { tag ->
                list.add(FeaturedList(
                    heading = tag,
                    results = movies.filter { it.data.tag == tag }.map { it.asMovie() }
                ))
            }
            return list
        }
    }
    // TODO consider the nullable possibility
    fun asMovie() = Movie (
        title = movie?.title ?: "title",
        slug = movie?.slug ?: "no slug",
        posterUrl = movie?.posterUrl ?: "no poster"
    )
}