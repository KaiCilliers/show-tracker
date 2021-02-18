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

package com.sunrisekcdeveloper.showtracker.features.detail.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sunrisekcdeveloper.showtracker.commons.models.local.*
import com.sunrisekcdeveloper.showtracker.models.local.core.MediaEntity

@Dao
abstract class DetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMovie(vararg item: MediaEntity)

    @Query("SELECT * FROM tbl_media_dump WHERE id = :id")
    abstract suspend fun media(id: Long): MediaEntity

    @Query("DELETE FROM tbl_watchlist_recently_added WHERE id = :id")
    abstract suspend fun removeRecentlyAddedMedia(id: Long)

    @Query("DELETE FROM tbl_watchlist_upcoming WHERE id = :id")
    abstract suspend fun removeUpcomingMedia(id: Long)

    @Query("DELETE FROM tbl_watchlist_completed WHERE id = :id")
    abstract suspend fun removeCompletedMedia(id: Long)

    @Query("DELETE FROM tbl_watchlist_in_progress WHERE id = :id")
    abstract suspend fun removeInProgressMedia(id: Long)

    @Query("DELETE FROM tbl_watchlist_anticipated WHERE id = :id")
    abstract suspend fun removeAnticipatedMedia(id: Long)

    @Insert
    abstract suspend fun insertRecentlyAddedMedia(media: RecentlyAddedMediaEntity)

    @Insert
    abstract suspend fun insertUpcomingMedia(media: UpcomingMediaEntity)

    @Insert
    abstract suspend fun insertCompletedMedia(media: CompletedMediaEntity)

    @Insert
    abstract suspend fun insertInProgressMedia(media: InProgressMediaEntity)

    @Insert
    abstract suspend fun insertAnticipatedMedia(media: AnticipatedMediaEntity)
}