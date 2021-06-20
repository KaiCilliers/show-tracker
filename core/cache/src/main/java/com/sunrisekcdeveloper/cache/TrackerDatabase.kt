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

package com.sunrisekcdeveloper.cache

import androidx.room.*
import com.sunrisekcdeveloper.cache.dao.*
import com.sunrisekcdeveloper.cache.models.*

@Database(
    entities = [
        EntityMovie::class, EntityWatchlistMovie::class,
        EntityShow::class, EntityWatchlistShow::class,
        EntitySeason::class, EntityWatchlistSeason::class,
        EntityEpisode::class, EntityWatchlistEpisode::class,
        EntityWatchlistBatch::class, UIModelDiscovery::class,
        RemoteKeys::class
    ],
    version = 68,
    exportSchema = false
)
@TypeConverters(TrackerTypeConverters::class)
abstract class TrackerDatabase : RoomDatabase() {
    abstract fun episodeDao(): DaoEpisode
    abstract fun movieDao(): DaoMovie
    abstract fun seasonDao(): DaoSeason
    abstract fun showDao(): DaoShow
    abstract fun watchlistEpisodeDao(): DaoWatchlistEpisode
    abstract fun watchlistMovieDao(): DaoWatchlistMovie
    abstract fun watchlistSeasonDao(): DaoWatchlistSeason
    abstract fun watchlistShowDao(): DaoWatchlistShow
    // todo refactor to have a dao for each table and not for each feature
    abstract fun remoteKeysDiscovery(): DaoRemoteKeys
    abstract fun discoveryDao(): DaoDiscovery
}