/*
 * Copyright © 2020. The Android Open Source Project
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

package com.sunrisekcdeveloper.showtracker.repository

import com.sunrisekcdeveloper.showtracker.data.local.MovieDao
import com.sunrisekcdeveloper.showtracker.data.local.model.categories.*
import com.sunrisekcdeveloper.showtracker.data.network.NetworkDataSourceContract
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DataSourceTrakt
import com.sunrisekcdeveloper.showtracker.model.Movie
import com.sunrisekcdeveloper.showtracker.util.datastate.Resource
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val local: MovieDao,
    @DataSourceTrakt private val remote: NetworkDataSourceContract
) : RepositoryContract {

    // TODO all these flows are very similar - refractor
    override fun trendingMoviesFlow() = local.trendingMoviesFlow()
        .distinctUntilChanged { old, new ->
            var same = true
            if (!old.isNullOrEmpty() && !new.isNullOrEmpty()) {
                (new.indices).forEach { i ->
                    if (new[i].data.mediaSlug != old[i].data.mediaSlug) same = false
                }
                Timber.d("trending table data the same: $same")
            } else {
                same = false
            }
            return@distinctUntilChanged same
        }
        .map { item ->
            val list = arrayListOf<Movie>()
            if (!item.isNullOrEmpty()) {
                item.forEach {
                    it.movie?.let { movie ->
                        list.add(movie.asDomain())
                    }
                }
            }
            return@map list
        }
        .onEach { updateTrending() }

    override fun popularMoviesFlow() = local.popularMoviesFlow()
        .distinctUntilChanged { old, new ->
            var same = true
            if (!old.isNullOrEmpty() && !new.isNullOrEmpty()) {
                (new.indices).forEach { i ->
                    if (new[i].data.mediaSlug != old[i].data.mediaSlug) same = false
                }
                Timber.d("popular table data the same: $same")
            } else {
                same = false
            }
            return@distinctUntilChanged same
        }
        .map { item ->
            val list = arrayListOf<Movie>()
            if (!item.isNullOrEmpty()) {
                item.forEach {
                    it.movie?.let { movie ->
                        list.add(movie.asDomain())
                    }
                }
            }
            return@map list
        }
        .onEach { updatePopular() }

    override fun boxofficeMoviesFlow() = local.boxOfficeMoviesFlow()
        .distinctUntilChanged { old, new ->
            var same = true
            if (!old.isNullOrEmpty() && !new.isNullOrEmpty()) {
                (new.indices).forEach { i ->
                    if (new[i].data.mediaSlug != old[i].data.mediaSlug) same = false
                }
                Timber.d("box office table data the same: $same")
            } else {
                same = false
            }
            return@distinctUntilChanged same
        }
        .map { item ->
            val list = arrayListOf<Movie>()
            if (!item.isNullOrEmpty()) {
                item.forEach {
                    it.movie?.let { movie ->
                        list.add(movie.asDomain())
                    }
                }
            }
            return@map list
        }
        .onEach { updateBox() }

    override fun mostPlayedMoviesFlow() = local.mostPlayedMoviesFlow()
        .distinctUntilChanged { old, new ->
            var same = true
            if (!old.isNullOrEmpty() && !new.isNullOrEmpty()) {
                (new.indices).forEach { i ->
                    if (new[i].data.mediaSlug != old[i].data.mediaSlug) same = false
                }
                Timber.d("most played table data the same: $same")
            } else {
                same = false
            }
            return@distinctUntilChanged same
        }
        .map { item ->
            val list = arrayListOf<Movie>()
            if (!item.isNullOrEmpty()) {
                item.forEach {
                    it.movie?.let { movie ->
                        list.add(movie.asDomain())
                    }
                }
            }
            return@map list
        }.onEach { updateMostPlayed() }

    override fun mostWatchedMoviesFlow() = local.mostWatchedMoviesFlow()
        .distinctUntilChanged { old, new ->
            var same = true
            if (!old.isNullOrEmpty() && !new.isNullOrEmpty()) {
                (new.indices).forEach { i ->
                    if (new[i].data.mediaSlug != old[i].data.mediaSlug) same = false
                }
                Timber.d("most watched table data the same: $same")
            } else {
                same = false
            }
            return@distinctUntilChanged same
        }
        .map { item ->
            val list = arrayListOf<Movie>()
            if (!item.isNullOrEmpty()) {
                item.forEach {
                    it.movie?.let { movie ->
                        list.add(movie.asDomain())
                    }
                }
            }
            return@map list
        }.onEach { updateMostWatched() }

    override fun mostAnticipatedMoviesFlow() = local.mostAnticipatedMoviesFlow()
        .distinctUntilChanged { old, new ->
            var same = true
            if (!old.isNullOrEmpty() && !new.isNullOrEmpty()) {
                (new.indices).forEach { i ->
                    if (new[i].data.mediaSlug != old[i].data.mediaSlug) same = false
                }
                Timber.d("anticipated table data the same: $same")
            } else {
                same = false
            }
            return@distinctUntilChanged same
        }
        .map { item ->
            val list = arrayListOf<Movie>()
            if (!item.isNullOrEmpty()) {
                item.forEach {
                    it.movie?.let { movie ->
                        list.add(movie.asDomain())
                    }
                }
            }
            return@map list
        }
        .onEach { updateAnticipated() }

    override fun recommendedMoviesFlow() = local.recommendedFlow()
        .distinctUntilChanged { old, new ->
            var same = true
            if (!old.isNullOrEmpty() && !new.isNullOrEmpty()) {
                (new.indices).forEach { i ->
                    if (new[i].data.mediaSlug != old[i].data.mediaSlug) same = false
                }
                Timber.d("recommended table data the same: $same")
            } else {
                same = false
            }
            return@distinctUntilChanged same
        }
        .map { item ->
            val list = arrayListOf<Movie>()
            if (!item.isNullOrEmpty()) {
                item.forEach {
                    it.movie?.let { movie ->
                        list.add(movie.asDomain())
                    }
                }
            }
            return@map list
        }
        .onEach { updateRecommended() }

    override suspend fun updateTrending() {
        val response = remote.fetchTrend()
        if (response is Resource.Success) {
            val exists = local.fetchTrending()
            local.insertMovie(*response.data.map { it.movie!!.asEntity() }.toTypedArray())
            if (exists.size == 10) {
                local.updateTrending(*response.data.map { it.asTrendingMovieEntity() }
                    .toTypedArray())
            } else {
                local.replaceTrending(*response.data.map { it.asTrendingMovieEntity() }
                    .toTypedArray())
            }
        }
    }

    override suspend fun updateBox() {
        val response = remote.fetchBox()
        if (response is Resource.Success) {
            val exists = local.fetchBox()
            local.insertMovie(*response.data.map { it.movie.asEntity() }.toTypedArray())
            if (exists.size == 10) {
                local.updateBox(*response.data.map { it.asEntity() }.toTypedArray())
            } else {
                local.replaceBox(*response.data.map { it.asEntity() }.toTypedArray())
            }
        }
    }

    override suspend fun updatePopular() {
        val response = remote.fetchPop()
        if (response is Resource.Success) {
            val exists = local.fetchPopular()
            local.insertMovie(*response.data.map { it.asEntity() }.toTypedArray())
            if (exists.size == 10) {
                val s = local.updatePopular(*response.data.map { PopularListEntity.from(it) }
                    .toTypedArray())
            } else {
                local.replacePopular(*response.data.map { PopularListEntity.from(it) }
                    .toTypedArray())
            }
        }
    }

    override suspend fun updateMostPlayed() {
        val response = remote.fetchMostPlayed()
        if (response is Resource.Success) {
            val exists = local.fetchMostPlayed()
            local.insertMovie(*response.data.map { it.movie!!.asEntity() }.toTypedArray())
            if (exists.size == 10) {
                local.updateMostPlayed(*response.data.map { MostPlayedListEntity.from(it) }
                    .toTypedArray())
            } else {
                local.replaceMostPlayed(*response.data.map { MostPlayedListEntity.from(it) }
                    .toTypedArray())
            }
        }
    }

    override suspend fun updateMostWatched() {
        val response = remote.fetchMostWatched()
        if (response is Resource.Success) {
            val exists = local.fetchMostWatched()
            local.insertMovie(*response.data.map { it.movie!!.asEntity() }.toTypedArray())
            if (exists.size == 10) {
                local.updateMostWatched(*response.data.map { MostWatchedListEntity.from(it) }
                    .toTypedArray())
            } else {
                local.replaceMostWatched(*response.data.map { MostWatchedListEntity.from(it) }
                    .toTypedArray())
            }
        }
    }

    override suspend fun updateAnticipated() {
        val response = remote.fetchAnticipated()
        if (response is Resource.Success) {
            val exists = local.fetchAnticipated()
            local.insertMovie(*response.data.map { it.movie!!.asEntity() }.toTypedArray())
            if (exists.size == 10) {
                local.updateAnticipated(*response.data.map { AnticipatedListEntity.from(it) }
                    .toTypedArray())
            } else {
                local.replaceAnticipated(*response.data.map { AnticipatedListEntity.from(it) }
                    .toTypedArray())
            }
        }
    }

    override suspend fun updateRecommended() {
        val response = remote.fetchRecommended()
        if (response is Resource.Success) {
            val exists = local.fetchRecommended()
            local.insertMovie(*response.data.map { it.movie!!.asEntity() }.toTypedArray())
            if (exists.size == 10) {
                local.updateRecommended(*response.data.map { RecommendedListEntity.from(it) }
                    .toTypedArray())
            } else {
                local.replaceRecommended(*response.data.map { RecommendedListEntity.from(it) }
                    .toTypedArray())
            }
        }
    }
}