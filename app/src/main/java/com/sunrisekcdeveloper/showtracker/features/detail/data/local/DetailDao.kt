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
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.WatchListType
import com.sunrisekcdeveloper.showtracker.models.local.core.MediaEntity

@Dao
abstract class DetailDao {

    @Query("UPDATE tbl_watchlist_media SET watchlist_type = :watchListType WHERE id = :id ")
    abstract suspend fun updateWatchListType(id: Long, watchListType: WatchListType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun dumpMovie(vararg item: MediaEntity.MovieEntityTMDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun dumpShow(vararg item: MediaEntity.ShowEntityTMDB)

    @Query("SELECT * FROM tbl_movie_dump WHERE id = :id")
    abstract suspend fun fromMovieDump(id: Long): MediaEntity.MovieEntityTMDB

    @Query("SELECT * FROM tbl_show_dump WHERE id = :id")
    abstract suspend fun fromShowDump(id: Long): MediaEntity.ShowEntityTMDB
}