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

package com.sunrisekcdeveloper.showtracker.features.discover.client

import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource.Success
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DiscoveryApi
import com.sunrisekcdeveloper.showtracker.models.local.core.MovieEntity
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseImages
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponsePoster
import com.sunrisekcdeveloper.showtracker.models.network.envelopes.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

class DiscoveryClient(
    @DiscoveryApi private val api: DiscoveryServiceContract,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : DiscoveryRemoteDataSourceContract {

    override suspend fun fetchTrend(): Resource<List<EnvelopeWatchers>> = result {
        api.trendingMovies()
    }

    override suspend fun fetchPop(): Resource<List<ResponseMovie>> = result {
        api.popularMovies()
    }

    override suspend fun fetchMostWatched(): Resource<List<EnvelopeViewStats>> = result {
        api.mostWatchedMovies()
    }

    override suspend fun poster(id: String): Resource<ResponseImages> = result {
        api.poster(id)
    }

    override suspend fun fetchFeaturedMovies(): MutableMap<String, List<MovieEntity>> {
        val trending = result { api.trendingMovies() }
        val popular = result { api.popularMovies() }
        val mostWatched = result { api.mostWatchedMovies() }

        val result = mutableMapOf<String, List<MovieEntity>>()

        if (trending is Success) {
            result["Trending"] = trending.data.mapNotNull {
                it.movie?.let { responseMovie ->
                    movieEntityOf(responseMovie)
                }
            }
        }
        if (popular is Success) {
            result["Popular"] = popular.data.map {
                it.let { responseMovie ->
                    movieEntityOf(responseMovie)
                }
            }
        }
        if (mostWatched is Success) {
            result["Most Watched"] = mostWatched.data.mapNotNull {
                it.movie?.let { responseMovie ->
                    movieEntityOf(responseMovie)
                }
            }
        }
        return result
    }

    // TODO this logic should be at the repo level to include updating cache logic
    //  val wrappedResult = safeApiCall(Dispatchers.IO) { api.getRandomDogBreed() }
    //        when (wrappedResult) {
    //            is ResultWrapper.Success<*> -> {
    //                val dogResponse = wrappedResult.value as ApiResponse<String>
    //                val breedImageUrl = dogResponse.message
    //                val dog = extractBreedName(breedImageUrl)?.let { Dog(it, breedImageUrl) }
    //                dog?.run {
    //                    dogDao.save(this)
    //                }
    //            }
    //        }
    //        return wrappedResult
    // TODO move to commons or utils
    private suspend fun <T> result(request: suspend () -> Response<T>): Resource<T>
            = withContext(dispatcher) {
        return@withContext try {
            val response = request()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return@withContext Success(it)
                }
            }
            error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            error(e.message ?: e.toString(), e)
        }
    }

    private fun <T> error(message: String, e: Exception): Resource<T> {
        Timber.e(message)
        return Resource.Error("Network call has failed for the following reason: $message")
    }


    // TODO: 18-01-2021 address double bang
    private suspend fun validPosterUrl(id: String): String {
        var validIds: String = ""
        val poster = poster(id)
        if (poster is Success) {
            validIds = organisedPosters(poster.data)
        }
        return validIds
    }

    // TODO save all posters that are valid somewhere
    private suspend fun organisedPosters(images: ResponseImages): String {
        val validPosterIds = mutableListOf<String>()
        images.posters?.let { validPosterIds.addAll(validatePosterIds(it)) }
        images.logo?.let { validPosterIds.addAll(validatePosterIds(it)) }
        images.banner?.let { validPosterIds.addAll(validatePosterIds(it)) }
        images.background?.let { validPosterIds.addAll(validatePosterIds(it)) }
        images.logo?.let { validPosterIds.addAll(validatePosterIds(it)) }
        images.thumb?.let { validPosterIds.addAll(validatePosterIds(it)) }
        Timber.e("VALID URLs: $validPosterIds")
        return if (validPosterIds.size == 0) {
            ""
        } else {
            var urls = ""
            validPosterIds.forEach {
                if (urls.length == 0) {
                    urls += it
                } else {
                    urls += ";$it"
                }
            }
            urls
        }
    }

    // TODO: 18-01-2021 revisit method - rename maybe
    private fun validatePosterIds(list: List<ResponsePoster>?): List<String> {
        val validUrls = mutableListOf<String>()
        list?.forEach {
            validUrls.add(it.url)
        }
        return validUrls
    }

    // TODO: 18-01-2021 takes too much time to load all images
    private suspend fun movieEntityOf(movie: ResponseMovie): MovieEntity {
        val entity = movie.asEntity()
        try {
            withContext(dispatcher) {
                entity.posterUrl = validPosterUrl("${movie.identifiers.tmdb}")
            }
        } catch (e: Exception) {
            Timber.e("$e")
        }
        return entity
    }
}

