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

import com.sunrisekcdeveloper.showtracker.commons.models.local.*
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.DetailClient
import com.sunrisekcdeveloper.showtracker.features.detail.data.local.DetailDao
import com.sunrisekcdeveloper.showtracker.features.detail.data.network.DetailDataSourceContract
import com.sunrisekcdeveloper.showtracker.features.detail.domain.repository.DetailRepositoryContract
import com.sunrisekcdeveloper.showtracker.models.local.core.MediaEntity

class DetailRepository(
    @DetailClient private val remote: DetailDataSourceContract,
    private val dao: DetailDao
) : DetailRepositoryContract {

    override suspend fun media(id: Long): MediaEntity = dao.media(id)

    override suspend fun removeMediaFromRecentlyAdded(id: Long) {
        dao.removeRecentlyAddedMedia(id)
    }

    override suspend fun removeMediaFromUpcoming(id: Long) {
        dao.removeUpcomingMedia(id)
    }

    override suspend fun removeMediaFromCompleted(id: Long) {
        dao.removeCompletedMedia(id)
    }

    override suspend fun removeMediaFromAnticipated(id: Long) {
        dao.removeAnticipatedMedia(id)
    }

    override suspend fun removeMediaFromInProgress(id: Long) {
        dao.removeInProgressMedia(id)
    }

    override suspend fun addMediaToRecentlyAdded(media: RecentlyAddedMediaEntity) =
        dao.insertRecentlyAddedMedia(media)

    override suspend fun addMediaToUpcoming(media: UpcomingMediaEntity) =
        dao.insertUpcomingMedia(media)

    override suspend fun addMediaToCompleted(media: CompletedMediaEntity) =
        dao.insertCompletedMedia(media)

    override suspend fun addMediaToAnticipated(media: AnticipatedMediaEntity) =
        dao.insertAnticipatedMedia(media)

    override suspend fun addMediaToInProgress(media: InProgressMediaEntity) =
        dao.insertInProgressMedia(media)
}

