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

package com.sunrisekcdeveloper.showtracker.features.discover

import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DiscoveryClient
import com.sunrisekcdeveloper.showtracker.features.discover.client.DiscoveryDataSourceContract
import com.sunrisekcdeveloper.showtracker.models.local.categories.*
import com.sunrisekcdeveloper.showtracker.models.roomresults.*
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class DiscoveryRepository(
    private val local: DiscoveryDao,
    @DiscoveryClient private val remote: DiscoveryDataSourceContract
) : DiscoveryRepositoryContract {

    private val ioScope = CoroutineScope(Job() + Dispatchers.IO)
    private val cpuScope = CoroutineScope(Job() + Dispatchers.Default)

    // TODO
    //  move to a base class - in a commons package or utils
    private fun update(block: suspend () -> Unit) {
        ioScope.launch {
            block.invoke()
        }
    }

    private suspend fun <T> featuredItem(
        dbCall: suspend () -> List<T>,
        remoteCall: suspend () -> Unit
    ): List<Movie> {
        val cache = withContext(ioScope.coroutineContext) { dbCall() }
        update { remoteCall() }
        return withContext(cpuScope.coroutineContext) {
            cache.map { item ->
                asDomain(item!!)
            }
        }
    }

    private fun asDomain(item: Any): Movie {
        return when(item) {
            is TrendingMovies -> item.movie?.asDomain()!!
            is PopularMovies -> item.movie?.asDomain()!!
            is BoxOfficeMovies -> item.movie?.asDomain()!!
            is MostPlayedMovies -> item.movie?.asDomain()!!
            is MostWatchedMovies -> item.movie?.asDomain()!!
            is AnticipatedMovies -> item.movie?.asDomain()!!
            is RecommendedMovies -> item.movie?.asDomain()!!
            else -> Movie("NOTHING", "NOTHING")
        }
    }

    override suspend fun trendingMovie(): List<Movie> = featuredItem(
            dbCall = { local.trendingMovies() },
            remoteCall = { updateTrending() }
        )

    override suspend fun popularMovie(): List<Movie> = featuredItem(
        dbCall = { local.popularMovies() },
        remoteCall = { updatePopular() }
    )

    override suspend fun boxofficeMovie(): List<Movie> = featuredItem(
        dbCall = { local.boxOfficeMovies() },
        remoteCall = { updateBox() }
    )

    override suspend fun mostPlayedMovie(): List<Movie> = featuredItem(
        dbCall = { local.mostPlayedMovies() },
        remoteCall = { updateMostPlayed() }
    )

    override suspend fun mostWatchedMovie(): List<Movie> = featuredItem(
        dbCall = { local.mostWatchedMovies() },
        remoteCall = { updateMostWatched() }
    )

    override suspend fun mostAnticipatedMovie(): List<Movie> = featuredItem(
        dbCall = { local.mostAnticipatedMovies() },
        remoteCall = { updateAnticipated() }
    )

    override suspend fun recommendedMovie(): List<Movie> = featuredItem(
        dbCall = { local.recommended() },
        remoteCall = { updateRecommended() }
    )

    override suspend fun updateTrending() {
        val response = remote.fetchTrend()
        if (response is Resource.Success) {
            val exists = local.fetchTrending()
            local.insertMovie(*response.data.map {
                val entity = it.movie!!.asEntity()
                withContext(Dispatchers.IO) {
                    val poster = remote.poster("${it.movie.identifiers.tmdb}")
                    if (poster is Resource.Success) {
                        entity.posterUrl = poster.data.posters?.get(0)?.url ?: ""
                    }
                }
                entity
            }.toTypedArray())
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
            local.insertMovie(*response.data.map {
                val entity = it.movie.asEntity()
                withContext(Dispatchers.IO) {
                    val poster = remote.poster("${it.movie.identifiers.tmdb}")
                    if (poster is Resource.Success) {
                        entity.posterUrl = poster.data.posters?.get(0)?.url ?: ""
                    }
                }
                entity
            }.toTypedArray())
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
            local.insertMovie(*response.data.map {
                val entity = it.asEntity()
                withContext(Dispatchers.IO) {
                    val poster = remote.poster("${it.identifiers.tmdb}")
                    if (poster is Resource.Success) {
                        entity.posterUrl = poster.data.posters?.get(0)?.url ?: ""
                    }
                }
                entity
            }.toTypedArray())
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
            local.insertMovie(*response.data.map {
                val entity = it.movie!!.asEntity()
                withContext(Dispatchers.IO) {
                    val poster = remote.poster("${it.movie.identifiers.tmdb}")
                    if (poster is Resource.Success) {
                        entity.posterUrl = poster.data.posters?.get(0)?.url ?: ""
                    }
                }
                entity
            }.toTypedArray())
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
            local.insertMovie(*response.data.map {
                val entity = it.movie!!.asEntity()
                withContext(Dispatchers.IO) {
                    val poster = remote.poster("${it.movie.identifiers.tmdb}")
                    if (poster is Resource.Success) {
                        entity.posterUrl = poster.data.posters?.get(0)?.url ?: ""
                    }
                }
                entity
            }.toTypedArray())
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
        // val response = safeApiCall { remote.fetchAnticipated() }
        if (response is Resource.Success) {
            val exists = local.fetchAnticipated()
            local.insertMovie(*response.data.map {
                val entity = it.movie!!.asEntity()
                withContext(Dispatchers.IO) {
                    val poster = remote.poster("${it.movie.identifiers.tmdb}")
                    if (poster is Resource.Success) {
                        var url = ""
                        poster.data.posters?.get(0)?.let {
                            url += it.url + ";"
                        }
                        poster.data.background?.get(0)?.let {
                            url += it.url + ";"
                        }
                        poster.data.thumb?.get(0)?.let {
                            url += it.url + ";"
                        }
                        poster.data.logo?.get(0)?.let {
                            url += it.url
                        }
                        Timber.e("URRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR")
                        Timber.e(url)
                        entity.posterUrl = url
                    }
                }
                entity
            }.toTypedArray())
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
            local.insertMovie(*response.data.map {
                val entity = it.movie!!.asEntity()
                withContext(Dispatchers.IO) {
                    val poster = remote.poster("${it.movie.identifiers.tmdb}")
                    if (poster is Resource.Success) {
                        entity.posterUrl = poster.data.posters?.get(0)?.url ?: ""
                    }
                }
                entity
            }.toTypedArray())
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

