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
import com.sunrisekcdeveloper.showtracker.models.roomresults.*
import timber.log.Timber
import java.util.*

class DiscoveryRepository(
    private val local: DiscoveryDao,
    @DiscoveryClient private val remote: DiscoveryDataSourceContract
) : DiscoveryRepositoryContract {

//    var cachedFeatured: MutableMap<String, List<Movie>>? = null
//    private var cacheIsDirty = false
//
//    private fun refreshCache(movies: MutableMap<String, List<Movie>>) {
//        cachedFeatured?.clear()
//        cachedFeatured = movies
//    }
//
//    private suspend fun refreshLocalCache(movies: MutableMap<String, List<MovieEntity>>) {
//        local.clearAllFeatured()
//        movies.forEach {
//            local.insertMovie(*it.value.toTypedArray())
//        }
//    }
//
//    private suspend fun moviesFromRemote() {
//        val result = remote.fetchFeaturedMovies().mapValues { entry ->
//            entry.value.map { it.asDomain() }
//        }.toMutableMap()
//        refreshCache(result)
//    }

    override suspend fun featuredMovies(): Resource<List<FeaturedList>> {
//        var featureMovies = mutableMapOf<String, List<Movie>>()
//        if (cachedFeatured != null && !cacheIsDirty) {
//            featureMovies = cachedFeatured as MutableMap<String, List<Movie>>
//        } else if (cacheIsDirty) {
//            moviesFromRemote()
//        }
//        return featureMovies
//        local.clearAllFeatured()
//        val one = remote.fetchTrend()
//        val two = remote.fetchAnticipated()
//        val three = remote.fetchPop()
//        val four = remote.fetchRecommended()
//        if (one is Success && two is Success && three is Success && four is Success) {
//            one.data.forEach {
//                local.insertFeatured(it.asFeaturedMovieEntity())
//                local.insertMovie(it.movie!!.asEntity())
//            }
//            two.data.forEach {
//                local.insertFeatured(it.asFeaturedMovieEntity())
//                local.insertMovie(it.movie!!.asEntity())
//            }
//            three.data.forEach {
//                local.insertFeatured(it.asFeaturedMovieEntity())
//                local.insertMovie(it.asEntity())
//            }
//            four.data.forEach {
//                local.insertFeatured(it.asFeaturedMovieEntity())
//                local.insertMovie(it.movie!!.asEntity())
//            }
//        }
//        Timber.d("All good :)")
        val result = local.groupedFeatured()
        val tags = hashSetOf<String>()
        result.forEach { tags.add(it.data.tag) }
        val list = mutableListOf<FeaturedList>()
        for(tag in tags) {
            list.add(
                FeaturedList(
                heading = tag,
                results = result.filter { it.data.tag == tag }.map { it.asMovie() }
            ))
        }
        Timber.d("$list")
        return Resource.Success(Collections.unmodifiableList(list))
    }

    //    private val ioScope = CoroutineScope(Job() + Dispatchers.IO)
//    private val cpuScope = CoroutineScope(Job() + Dispatchers.Default)
//
//    private fun validCache(updated: String): Boolean {
//        Timber.e("UPDATED AT VALUE: $updated")
//        return true
//    }
//
//    private suspend fun mapper(logic: () -> List<Movie>) = withContext(cpuScope.coroutineContext) {
//        logic()
//    }
//
//    var cacheIsDirty = false
//    var cachedFeatured: MutableMap<String, List<Movie>>? = null

//    private suspend fun methhead(): List<Movie> {
//        if (cachedFeatured != null && cachedFeatured!!["trending"] != null && !cacheIsDirty) {
//            return cachedFeatured!!["trending"]!!
//        }
//        if (cacheIsDirty) {
//
//        }
//    }

//    private suspend fun getFromRemoteDataSource() {
//        val response = remote.fetchTrend()
//        if (response is Resource.Success) {
//
//        }
//    }
//
//    private fun refreshCache(tag: String, movies: List<Movie>) {
//        if (cachedFeatured == null) {
//            cachedFeatured = LinkedHashMap<String, List<Movie>>()
//        }
//        cachedFeatured!![tag] = movies
//        cacheIsDirty = false
//    }
//
//    private fun refresh(movies: List<Movie>) {
//
//    }
//
//    override suspend fun trendingMovie(): List<Movie> = withContext(ioScope.coroutineContext) {
//        var cache = local.trendingMovies()
//        if (cache.isNullOrEmpty() || !validCache(cache[0].movie?.updatedAt!!)) {
//            cache = updateTrending()
//        }
//        mapper { cache.map { it.movie?.asDomain()!! } }
//    }

    //    override suspend fun popularMovie(): List<Movie> = featuredItem(
//        dbCall = { local.popularMovies() },
//        remoteCall = { updatePopular() }
//    )
//
//    override suspend fun boxofficeMovie(): List<Movie> = withContext(ioScope.coroutineContext) {
//        var cache = local.boxOfficeMovies()
//        if (cache.isNullOrEmpty() || !validCache(cache[0].movie?.updatedAt!!)) {
//            cache = updateBox()
//        }
//        mapper { cache.map { it.movie?.asDomain()!! } }
//    }
//    override suspend fun mostPlayedMovie(): List<Movie> = featuredItem(
//        dbCall = { local.mostPlayedMovies() },
//        remoteCall = { updateMostPlayed() }
//    )
//
//    override suspend fun mostWatchedMovie(): List<Movie> = featuredItem(
//        dbCall = { local.mostWatchedMovies() },
//        remoteCall = { updateMostWatched() }
//    )
//
//    override suspend fun mostAnticipatedMovie(): List<Movie> = featuredItem(
//        dbCall = { local.mostAnticipatedMovies() },
//        remoteCall = { updateAnticipated() }
//    )
//
//    override suspend fun recommendedMovie(): List<Movie> = featuredItem(
//        dbCall = { local.recommended() },
//        remoteCall = { updateRecommended() }
//    )

//    override suspend fun updateTrending(): List<TrendingMovies> {
//        val response = remote.fetchTrend()
//        if (response is Resource.Success) {
//            val exists = local.fetchTrending()
//            local.insertMovie(*response.data.map {
//                val entity = it.movie!!.asEntity()
//                Timber.d("${local.moviePosterUrl(entity.slug)}")
//                if (local.moviePosterUrl(entity.slug).isNullOrEmpty()) {
//                    withContext(Dispatchers.IO) {
//                        val poster = remote.poster("${it.movie.identifiers.tmdb}")
//                        if (poster is Resource.Success) {
//                            entity.posterUrl = poster.data.posters?.get(0)?.url ?: ""
//                        }
//                    }
//                }
//                entity
//            }.toTypedArray())
//            if (exists.size == 10) {
//                local.updateTrending(*response.data.map { it.asTrendingMovieEntity() }
//                    .toTypedArray())
//            } else {
//                local.replaceTrending(*response.data.map { it.asTrendingMovieEntity() }
//                    .toTypedArray())
//            }
//            return local.trendingMovies()
//        }
//        return emptyList()
//    }
//
//    override suspend fun updateBox(): List<BoxOfficeMovies> {
//        val response = remote.fetchBox()
//        if (response is Resource.Success) {
//            val exists = local.fetchBox()
//            local.insertMovie(*response.data.map {
//                val entity = it.movie.asEntity()
//                if (local.moviePosterUrl(entity.slug).isNullOrEmpty()) {
//                    withContext(Dispatchers.IO) {
//                        val poster = remote.poster("${it.movie.identifiers.tmdb}")
//                        if (poster is Resource.Success) {
//                            entity.posterUrl = poster.data.posters?.get(0)?.url ?: ""
//                        }
//                    }
//                }
//                entity
//            }.toTypedArray())
//            if (exists.size == 10) {
//                local.updateBox(*response.data.map { it.asEntity() }.toTypedArray())
//            } else {
//                local.replaceBox(*response.data.map { it.asEntity() }.toTypedArray())
//            }
//            return local.boxOfficeMovies()
//        }
//        return emptyList()
//    }
//
//    override suspend fun updatePopular() {
//        val response = remote.fetchPop()
//        if (response is Resource.Success) {
//            val exists = local.fetchPopular()
//            local.insertMovie(*response.data.map {
//                val entity = it.asEntity()
//                withContext(Dispatchers.IO) {
//                    val poster = remote.poster("${it.identifiers.tmdb}")
//                    if (poster is Resource.Success) {
//                        entity.posterUrl = poster.data.posters?.get(0)?.url ?: ""
//                    }
//                }
//                entity
//            }.toTypedArray())
//            if (exists.size == 10) {
//                val s = local.updatePopular(*response.data.map { PopularListEntity.from(it) }
//                    .toTypedArray())
//            } else {
//                local.replacePopular(*response.data.map { PopularListEntity.from(it) }
//                    .toTypedArray())
//            }
//        }
//    }
//
//    override suspend fun updateMostPlayed() {
//        val response = remote.fetchMostPlayed()
//        if (response is Resource.Success) {
//            val exists = local.fetchMostPlayed()
//            local.insertMovie(*response.data.map {
//                val entity = it.movie!!.asEntity()
//                withContext(Dispatchers.IO) {
//                    val poster = remote.poster("${it.movie.identifiers.tmdb}")
//                    if (poster is Resource.Success) {
//                        entity.posterUrl = poster.data.posters?.get(0)?.url ?: ""
//                    }
//                }
//                entity
//            }.toTypedArray())
//            if (exists.size == 10) {
//                local.updateMostPlayed(*response.data.map { MostPlayedListEntity.from(it) }
//                    .toTypedArray())
//            } else {
//                local.replaceMostPlayed(*response.data.map { MostPlayedListEntity.from(it) }
//                    .toTypedArray())
//            }
//        }
//    }
//
//    override suspend fun updateMostWatched() {
//        val response = remote.fetchMostWatched()
//        if (response is Resource.Success) {
//            val exists = local.fetchMostWatched()
//            local.insertMovie(*response.data.map {
//                val entity = it.movie!!.asEntity()
//                withContext(Dispatchers.IO) {
//                    val poster = remote.poster("${it.movie.identifiers.tmdb}")
//                    if (poster is Resource.Success) {
//                        entity.posterUrl = poster.data.posters?.get(0)?.url ?: ""
//                    }
//                }
//                entity
//            }.toTypedArray())
//            if (exists.size == 10) {
//                local.updateMostWatched(*response.data.map { MostWatchedListEntity.from(it) }
//                    .toTypedArray())
//            } else {
//                local.replaceMostWatched(*response.data.map { MostWatchedListEntity.from(it) }
//                    .toTypedArray())
//            }
//        }
//    }
//
//    override suspend fun updateAnticipated() {
//        val response = remote.fetchAnticipated()
//        // val response = safeApiCall { remote.fetchAnticipated() }
//        if (response is Resource.Success) {
//            val exists = local.fetchAnticipated()
//            local.insertMovie(*response.data.map {
//                val entity = it.movie!!.asEntity()
//                withContext(Dispatchers.IO) {
//                    val poster = remote.poster("${it.movie.identifiers.tmdb}")
//                    if (poster is Resource.Success) {
//                        var url = ""
//                        poster.data.posters?.get(0)?.let {
//                            url += it.url + ";"
//                        }
//                        poster.data.background?.get(0)?.let {
//                            url += it.url + ";"
//                        }
//                        poster.data.thumb?.get(0)?.let {
//                            url += it.url + ";"
//                        }
//                        poster.data.logo?.get(0)?.let {
//                            url += it.url
//                        }
//                        Timber.e("URRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR")
//                        Timber.e(url)
//                        entity.posterUrl = url
//                    }
//                }
//                entity
//            }.toTypedArray())
//            if (exists.size == 10) {
//                local.updateAnticipated(*response.data.map { AnticipatedListEntity.from(it) }
//                    .toTypedArray())
//            } else {
//                local.replaceAnticipated(*response.data.map { AnticipatedListEntity.from(it) }
//                    .toTypedArray())
//            }
//        }
//    }
//
//    override suspend fun updateRecommended() {
//        val response = remote.fetchRecommended()
//        if (response is Resource.Success) {
//            val exists = local.fetchRecommended()
//            local.insertMovie(*response.data.map {
//                val entity = it.movie!!.asEntity()
//                withContext(Dispatchers.IO) {
//                    val poster = remote.poster("${it.movie.identifiers.tmdb}")
//                    if (poster is Resource.Success) {
//                        entity.posterUrl = poster.data.posters?.get(0)?.url ?: ""
//                    }
//                }
//                entity
//            }.toTypedArray())
//            if (exists.size == 10) {
//                local.updateRecommended(*response.data.map { RecommendedListEntity.from(it) }
//                    .toTypedArray())
//            } else {
//                local.replaceRecommended(*response.data.map { RecommendedListEntity.from(it) }
//                    .toTypedArray())
//            }
//        }
//    }
}

