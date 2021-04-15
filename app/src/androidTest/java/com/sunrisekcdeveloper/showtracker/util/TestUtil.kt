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

package com.sunrisekcdeveloper.showtracker.util

import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityEpisode
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityMovie
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntitySeason
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityShow
import kotlin.random.Random

object TestUtil {
    fun createEntityMovie(
        id: Int = Random(Int.MAX_VALUE).nextInt(),
        title: String = "${Random(Int.MAX_VALUE).nextInt()}"
    ): EntityMovie {
        return EntityMovie(
            id = "$id",
            title = title,
            overview = "",
            posterPath = "",
            backdropPath = "",
            rating = 1f,
            popularityValue = 1f,
            certification = "",
            releaseDate = "",
            runTime = "",
            dateLastUpdated = -1L
        )
    }

    fun createEntityShow(
        id: Int = Random(Int.MAX_VALUE).nextInt(),
        title: String = "${Random(Int.MAX_VALUE).nextInt()}"
    ): EntityShow {
        return EntityShow(
            id = "$id",
            title = title,
            overview = "",
            certification = "",
            posterPath = "",
            backdropPath = "",
            popularityValue = 1f,
            firstAirDate = "",
            rating = 1f,
            episodeTotal = 1,
            seasonTotal = 1,
            lastUpdated = -1L
        )
    }

    fun createEntitySeason(
        showId: Int = Random(Int.MAX_VALUE).nextInt(),
        id: Int = Random(Int.MAX_VALUE).nextInt(),
        seasonNumber: Int = Random(Int.MAX_VALUE).nextInt(),
        name: String = ""
    ): EntitySeason {
        return EntitySeason(
            showId = "$showId",
            id = id,
            number = seasonNumber,
            name = name,
            overview = "",
            posterPath = "",
            airDate = 0L,
            episodeTotal = 0,
            lastUpdated = -1L
        )
    }

    fun createEntityEpisode(
        showId: Int = Random(Int.MAX_VALUE).nextInt(),
        seasonNumber: Int = Random(Int.MAX_VALUE).nextInt(),
        number: Int = Random(Int.MAX_VALUE).nextInt(),
        name: String = ""
    ): EntityEpisode {
        return EntityEpisode(
            showId = "$showId",
            seasonNumber = seasonNumber,
            number = number,
            name = name,
            airDate = 0L,
            overview = "",
            stillPath = "",
            lastUpdated = 0L
        )
    }
}