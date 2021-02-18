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

package com.sunrisekcdeveloper.showtracker.features.detail.domain.usecase

import com.sunrisekcdeveloper.showtracker.commons.util.*
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.DetailRepo
import com.sunrisekcdeveloper.showtracker.features.detail.application.UpdateWatchListContentsUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.WatchListType
import com.sunrisekcdeveloper.showtracker.features.detail.domain.repository.DetailRepositoryContract

class UpdateWatchListContentsUseCase(
    @DetailRepo private val detailRepo: DetailRepositoryContract
) : UpdateWatchListContentsUseCaseContract {
    override suspend fun invoke(id: Long, from: WatchListType, to: WatchListType) {
        removeFromList(id, from)
        addToList(id, to)
    }

    private suspend fun removeFromList(id: Long, type: WatchListType) {
        return when (type) {
            WatchListType.NONE -> { }
            WatchListType.RECENTLY_ADDED -> { detailRepo.removeMediaFromRecentlyAdded(id) }
            WatchListType.ANTICIPATED -> { detailRepo.removeMediaFromAnticipated(id) }
            WatchListType.IN_PROGRESS -> { detailRepo.removeMediaFromInProgress(id) }
            WatchListType.UPCOMING -> { detailRepo.removeMediaFromUpcoming(id) }
            WatchListType.COMPLETED -> { detailRepo.removeMediaFromCompleted(id) }
        }
    }

    private suspend fun addToList(id: Long, type: WatchListType) {
        val media = detailRepo.media(id)
        return when (type) {
            WatchListType.NONE -> { }
            WatchListType.RECENTLY_ADDED -> { detailRepo.addMediaToRecentlyAdded(media.asRecentlyAddedMedia()) }
            WatchListType.ANTICIPATED -> { detailRepo.addMediaToAnticipated(media.asAnticipatedMediaEntity()) }
            WatchListType.IN_PROGRESS -> { detailRepo.addMediaToInProgress(media.asInProgressMediaEntity()) }
            WatchListType.UPCOMING -> { detailRepo.addMediaToUpcoming(media.asUpcomingMediaEntity()) }
            WatchListType.COMPLETED -> { detailRepo.addMediaToCompleted(media.asCompletedMediaEntity()) }
        }
    }
}