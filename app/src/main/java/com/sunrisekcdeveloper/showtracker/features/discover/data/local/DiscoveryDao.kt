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

package com.sunrisekcdeveloper.showtracker.features.discover.data.local

import androidx.room.*
import com.sunrisekcdeveloper.showtracker.commons.models.local.RecentlyAddedMediaEntity
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.DiscoveryPopularEntity
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.DiscoveryTopRatedEntity
import com.sunrisekcdeveloper.showtracker.features.discover.domain.model.DiscoveryUpcomingEntity
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.WatchListEntity
import com.sunrisekcdeveloper.showtracker.models.local.categories.PopularListEntity
import com.sunrisekcdeveloper.showtracker.models.local.core.MediaEntity

@Dao
abstract class DiscoveryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWatchListEntity(vararg media: WatchListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMedia(vararg movie: MediaEntity)

    @Query("SELECT * FROM tbl_discovery_popular")
    abstract suspend fun popularList(): List<DiscoveryPopularEntity>

    @Query("SELECT * FROM tbl_discovery_top_rated")
    abstract suspend fun topRatedList(): List<DiscoveryTopRatedEntity>

    @Query("SELECT * FROM tbl_discovery_upcoming")
    abstract suspend fun upcomingList(): List<DiscoveryUpcomingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPopularEntity(vararg media: DiscoveryPopularEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertTopRatedEntity(vararg media: DiscoveryTopRatedEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertUpcomingEntity(vararg media: DiscoveryUpcomingEntity)
}
