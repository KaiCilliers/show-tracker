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

package com.sunrisekcdeveloper.showtracker.features.detail.domain.repository

import com.sunrisekcdeveloper.showtracker.commons.models.local.*
import com.sunrisekcdeveloper.showtracker.models.local.core.MediaEntity

interface DetailRepositoryContract {

    suspend fun media(id: Long): MediaEntity

    suspend fun removeMediaFromRecentlyAdded(id: Long)
    suspend fun removeMediaFromUpcoming(id: Long)
    suspend fun removeMediaFromCompleted(id: Long)
    suspend fun removeMediaFromAnticipated(id: Long)
    suspend fun removeMediaFromInProgress(id: Long)

    suspend fun addMediaToRecentlyAdded(media: RecentlyAddedMediaEntity)
    suspend fun addMediaToUpcoming(media: UpcomingMediaEntity)
    suspend fun addMediaToCompleted(media: CompletedMediaEntity)
    suspend fun addMediaToAnticipated(media: AnticipatedMediaEntity)
    suspend fun addMediaToInProgress(media: InProgressMediaEntity)
}