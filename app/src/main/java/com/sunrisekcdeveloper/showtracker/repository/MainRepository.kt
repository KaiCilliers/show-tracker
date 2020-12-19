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
import com.sunrisekcdeveloper.showtracker.data.local.model.categories.PopularListEntity
import com.sunrisekcdeveloper.showtracker.data.network.NetworkDataSourceContract
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DataSourceTrakt
import com.sunrisekcdeveloper.showtracker.model.Movie
import com.sunrisekcdeveloper.showtracker.util.datastate.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val local: MovieDao,
    @DataSourceTrakt private val remote: NetworkDataSourceContract
) : RepositoryContract {

    override fun trending() = local.trendingMoviesFlow()
        .distinctUntilChanged { old, new ->
            var same = true
            (new.indices).forEach { i ->
                if (new[i].data.mediaSlug != old[i].data.mediaSlug) same = false
            }
            Timber.d("trending table data the same: $same")
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

    override fun popular() = local.popularMoviesFlow()
        .distinctUntilChanged { old, new ->
            var same = true
            (new.indices).forEach { i ->
                if (new[i].data.mediaSlug != old[i].data.mediaSlug) same = false
            }
            Timber.d("popular table data the same: $same")
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

    override fun box() = local.boxOfficeMoviesFlow()
        .distinctUntilChanged { old, new ->
            var same = true
            (new.indices).forEach { i ->
                if (new[i].data.mediaSlug != old[i].data.mediaSlug) same = false
            }
            Timber.d("box table data the same: $same")
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

    override suspend fun updateTrending() {
        val remote = remote.fetchTrend()
        if (remote is Resource.Success) {
            local.insertMovie(*remote.data.map { it.movie!!.asEntity() }.toTypedArray())
            local.replaceTrending(*remote.data.map { it.asTrendingMovieEntity() }.toTypedArray())
            Timber.d("Updated trending")
        }
    }

    override suspend fun updateBox() {
        val remote = remote.fetchBox()
        if (remote is Resource.Success) {
            local.insertMovie(*remote.data.map { it.movie.asEntity() }.toTypedArray())
            local.replaceBox(*remote.data.map { it.asEntity() }.toTypedArray())
            Timber.d("Updated box")
        }
    }

    override suspend fun updatePopular() {
        val remote = remote.fetchPop()
        if (remote is Resource.Success) {
            val exists = local.fetchPopular()
            local.insertMovie(*remote.data.map { it.asEntity() }.toTypedArray())
            if (exists.size == 10) {
                val s = local.updatePopular(*remote.data.map { PopularListEntity.from(it) }.toTypedArray())
                Timber.d("Updated Popular: $s")
            } else {
                local.replacePopular(*remote.data.map { PopularListEntity.from(it) }.toTypedArray())
                Timber.d("Replaced Popular")
            }
        }
    }
}