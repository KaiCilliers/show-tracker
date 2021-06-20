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

package com.sunrisekcdeveloper.cache.dao

import androidx.room.Dao
import androidx.room.Query
import com.sunrisekcdeveloper.cache.models.EntityMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

// todo all room functions need to either return nullable values (record might not be there)
//  or return a List - thus an empty list is returned when no records are found
@Dao
abstract class DaoMovie : DaoBase<EntityMovie> {

    @Query("SELECT * FROM tbl_movie WHERE movie_id = :id")
    abstract suspend fun withId(id: String): EntityMovie?

    @Query("SELECT * FROM tbl_movie WHERE movie_id = :id")
    protected abstract fun movieFlow(id: String): Flow<EntityMovie?>

    open fun distinctMovieFlow(id: String): Flow<EntityMovie?> = movieFlow(id).distinctUntilChanged()
}