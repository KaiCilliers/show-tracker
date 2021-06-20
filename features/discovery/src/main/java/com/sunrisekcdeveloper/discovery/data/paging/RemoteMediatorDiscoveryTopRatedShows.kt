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

package com.sunrisekcdeveloper.discovery.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.sunrisekcdeveloper.cache.ListType
import com.sunrisekcdeveloper.cache.TrackerDatabase
import com.sunrisekcdeveloper.cache.UIModelDiscovery
import com.sunrisekcdeveloper.cache.models.RemoteKeys
import com.sunrisekcdeveloper.discovery.asUIModelDiscovery
import com.sunrisekcdeveloper.discovery.data.network.RemoteDataSourceDiscoveryContract
import com.sunrisekcdeveloper.network.NetworkResult
import java.io.InvalidObjectException

@ExperimentalPagingApi
class RemoteMediatorDiscoveryTopRatedShows(
    private val remote: RemoteDataSourceDiscoveryContract,
    private val database: TrackerDatabase
) : RemoteMediator<Int, UIModelDiscovery>() {

    companion object {
        private const val TMDB_STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UIModelDiscovery>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = remoteKeysClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: TMDB_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                // This check is due to bug on first load PREPEND is called and RemoteKeys is null
                // https://issuetracker.google.com/issues/162252536
                if (remoteKeysForFirstItem(state) == null) {
                    TMDB_STARTING_PAGE_INDEX
                } else {
                    val remoteKeys = remoteKeysForFirstItem(state)
                        ?: throw InvalidObjectException("Remote key and the prevKey should not be null")
                    if (remoteKeys.prevKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKeys.prevKey
                }
            }
            LoadType.APPEND -> {
                val remoteKeys = remoteKeysForLastItem(state)
                // This check is due to bug on first load PREPEND is called and RemoteKeys is null
                // https://issuetracker.google.com/issues/162252536
                if (remoteKeys?.nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = false)
//                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }
        }

        return when (val response = remote.topRatedShows(page!!)) {
            is NetworkResult.Success -> {

                val uiModels =
                    response.data.media.map { it.asUIModelDiscovery(ListType.ShowTopRated) }
                val endOfPagination = uiModels.isEmpty()

                try {

                    database.withTransaction {

                        if (loadType == LoadType.REFRESH) {
                            database.remoteKeysDiscovery().clearByListType(
                                ListType.ShowTopRated
                            )
                            database.discoveryDao().clearList(ListType.ShowTopRated)
                        }

                        val prevKey = if (page == TMDB_STARTING_PAGE_INDEX) null else page - 1
                        val nextKey = if (endOfPagination) null else page + 1

                        val keys = uiModels.map {
                            RemoteKeys(
                                id = it.id,
                                listType = it.listType,
                                prevKey = prevKey,
                                nextKey = nextKey
                            )
                        }

                        database.remoteKeysDiscovery().insertAll(keys)
                        database.discoveryDao().insertAll(uiModels)

                    }

                } catch (exception: Exception) {
                    return MediatorResult.Error(exception)
                }

                MediatorResult.Success(endOfPagination)

            }
            is NetworkResult.Error -> {
                MediatorResult.Error(response.exception)
            }
        }
    }

    private suspend fun remoteKeysForLastItem(state: PagingState<Int, UIModelDiscovery>): RemoteKeys? {
        val singlePage = state.pages.lastOrNull() { it.data.isNotEmpty() }
        val list = singlePage?.data
        val lastInList = list?.lastOrNull()
        return lastInList?.let {
            database.remoteKeysDiscovery().remoteKeysByIdAndListType(it.id, it.listType)
        }
    }

    private suspend fun remoteKeysForFirstItem(state: PagingState<Int, UIModelDiscovery>): RemoteKeys? {
        val singlePage = state.pages.firstOrNull() { it.data.isNotEmpty() }
        val list = singlePage?.data
        val firstInList = list?.firstOrNull()
        return firstInList?.let {
            database.remoteKeysDiscovery().remoteKeysByIdAndListType(it.id, it.listType)
        }
    }

    private suspend fun remoteKeysClosestToCurrentPosition(state: PagingState<Int, UIModelDiscovery>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let {
                database.remoteKeysDiscovery()
                    .remoteKeysByIdAndListType(it.id, it.listType)
            }
        }
    }
}