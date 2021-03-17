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

package com.sunrisekcdeveloper.showtracker.features.detail.data.repository

import com.sunrisekcdeveloper.showtracker.common.NetworkResult
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.SourceDetail
import com.sunrisekcdeveloper.showtracker.features.detail.data.local.DaoDetail
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseMovieDetail
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseMovieReleaseDates
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseShowCertification
import com.sunrisekcdeveloper.showtracker.features.detail.data.model.ResponseShowDetail
import com.sunrisekcdeveloper.showtracker.updated.features.detail.data.network.RemoteDataSourceDetailContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelMovieDetail
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelShowDetail
import com.sunrisekcdeveloper.showtracker.features.detail.domain.repository.RepositoryDetailContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityShow
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityWatchlistShow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

@ExperimentalCoroutinesApi
class RepositoryDetail(
    @SourceDetail private val remote: RemoteDataSourceDetailContract,
    private val local: DaoDetail,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RepositoryDetailContract {

    override suspend fun addShowToWatchlist(id: String) {
        local.addShowToWatchlist(EntityWatchlistShow.freshBareEntryFrom(id))
    }

    override suspend fun removeShowFromWatchlist(id: String) {
        local.removeShowFromWatchlist(id)
    }

    override suspend fun removeMovieFromWatchlist(id: String) {
        local.removeMovieFromWatchlist(id)
    }

    override suspend fun updateWatchlistMovieAsWatched(id: String) {
        // todo better implementation possible
        insertWatchlistMovieIfNotExists(id)
        local.setMovieAsWatched(id)
    }

    private suspend fun insertWatchlistMovieIfNotExists(id: String) {
        local.insertWatchlistMovieExists(EntityWatchlistMovie.unWatchedfrom(id))
    }

    override suspend fun updateWatchlistMovieAsNotWatched(id: String) {
        local.setMovieAsNotWatched(id)
    }

    override suspend fun addMovieToWatchlist(id: String) {
        local.addMovieToWatchlist(EntityWatchlistMovie.unWatchedfrom(id))
    }

    override suspend fun fetchAndSaveMovieDetails(id: String) {
        withContext(dispatcher) {
            launch {
                val responseMovie = remote.movieDetails(id)
                val responseCertification = remote.movieReleaseDates(id)

                when (responseMovie) {
                    is NetworkResult.Success -> {
                        var result = responseMovie.data.asEntityMovie()
                        when (responseCertification) {
                            is NetworkResult.Success -> {
                                result = result.copy(
                                    certification = movieCertificationUSIfPossible(
                                        responseCertification.data.results
                                    )
                                )
                            }
                            is NetworkResult.Error -> {
                                Timber.e("Error fetching certification data for movie with ID: $id [${responseCertification.message}]")
                            }
                        }
                        saveMovieDetails(result)
                    }
                    is NetworkResult.Error -> {
                        Timber.e("Error fetching movie details for movie with ID: $id [${responseMovie.message}]")
                    }
                }
            }
        }
    }

    override suspend fun movieDetails(id: String): Flow<Resource<UIModelMovieDetail>> {
        val deets = local.distinctMovieDetailFlow(id)
        val status = local.distinctWatchlistMovieFlow(id)

        return combine(deets, status) { d, s ->
            var watchlisted = false
            var watched = false
            s?.let {
                watchlisted = true
                watched = s.watched
            }
            return@combine if (d == null) {
                Resource.Error("No movie with ID: $id exists in database")
            } else {
                Resource.Success(d.asUIModelMovieDetail(watchlisted, watched))
            }
        }.onStart { emit(Resource.Loading) }
    }

    override suspend fun fetchAndSaveShowDetails(id: String) {
        withContext(dispatcher) {
            launch {
                val responseShow = remote.showDetail(id)
                val responseCertification = remote.showCertification(id)

                when (responseShow) {
                    is NetworkResult.Success -> {
                        var result = responseShow.data.asEntityShow()
                        when(responseCertification) {
                            is NetworkResult.Success -> {
                                result = result.copy(
                                    certification = showCertificationUSIfPossible(responseCertification.data.results)
                                )
                            }
                            is NetworkResult.Error -> {
                                Timber.e("Error fetching show certification for show: $id")
                            }
                        }
                        saveShowDetails(result)
                    }
                    is NetworkResult.Error -> {
                        Timber.e("Error fetching show details for show with id: $id")
                    }
                }
            }
        }
    }

    override suspend fun showDetails(id: String): Flow<Resource<UIModelShowDetail>> {

        val showDetails = local.distinctShowDetailFlow(id)
        val status = local.distinctWatchlistShowFlow(id)

       return combine(showDetails, status) { showDetails, status ->
            var watchlisted = false
            var started = false
            var upToDate = false

            status?.let {
                watchlisted = true
                started = it.started
                upToDate = it.upToDate
            }

            return@combine if (showDetails == null) {
                Resource.Error("No show with ID: $id exists in database")
            } else {
                Resource.Success(showDetails.asUIModelShowDetail(watchlisted, started, upToDate))
            }
        }.onStart { emit(Resource.Loading) }
    }

    private suspend fun saveMovieDetails(entity: EntityMovie) {
        local.addMovieDetails(entity)
    }

    private fun movieCertificationUSIfPossible(data: List<ResponseMovieReleaseDates>): String {
        return if (data.isNotEmpty()) {
            val iso = data.find { it.iso == "US" } ?: data[0]
            return if (iso.releaseDates.isNotEmpty()) {
                val certification = iso.releaseDates.find { it.releaseType == 3 }?.certification
                    ?: iso.releaseDates[0].certification
                return if (certification != "") {
                    certification
                } else {
                    "N/A"
                }
            } else {
                "N/A"
            }
        } else {
            "N/A"
        }
    }

    private suspend fun saveShowDetails(entity: EntityShow) {
        local.addShowDetails(entity)
    }

    private fun showCertificationUSIfPossible(data: List<ResponseShowCertification>): String {
        return if (data.isNotEmpty()) {
            data.find {
                it.iso == "US"
            }?.certification ?: data[0].certification
        } else {
            "N/A"
        }
    }
}

fun EntityShow.asUIModelShowDetail(
    watchlisted: Boolean = false,
    started: Boolean = false,
    upToDate: Boolean = false
) = UIModelShowDetail(
    id = id,
    name = title,
    posterPath = posterPath,
    overview = overview,
    certification = certification,
    firstAirDate = firstAirDate,
    seasonsTotal = seasonTotal,
    watchlisted = watchlisted,
    startedWatching = started,
    upToDate = upToDate
)

fun ResponseShowDetail.asEntityShow() = EntityShow(
    id = "$id",
    title = name,
    overview = overview,
    certification = "N/A",
    posterPath = posterPath?: "",
    backdropPath = backdropPath?: "",
    popularityValue = popularityValue,
    firstAirDate = firstAirYear,
    rating = rating,
    episodeTotal = episodeCount,
    seasonTotal = seasonCount,
    lastUpdated = System.currentTimeMillis()
)

fun ResponseMovieDetail.asEntityMovie() = EntityMovie(
    id = "$id",
    title = title,
    overview = overview,
    posterPath = posterPath ?: "",
    certification = "",
    releaseDate = releaseDate,
    runTime =  "$runtime"
)

fun EntityMovie.asUIModelMovieDetail(watchlisted: Boolean, watched: Boolean) = UIModelMovieDetail(
    id = id,
    title = title,
    posterPath = posterPath,
    overview = overview,
    releaseYear = releaseDate,
    certification = certification,
    runtime = runTime,
    watchlisted = watchlisted,
    watched = watched
)