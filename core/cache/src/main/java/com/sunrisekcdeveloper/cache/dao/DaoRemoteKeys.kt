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
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sunrisekcdeveloper.cache.ListType
import com.sunrisekcdeveloper.cache.models.RemoteKeys

// todo incorporate into table based DAO style that extends DaoBase
@Dao
interface DaoRemoteKeys {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeys>)

    @Query("SELECT * FROM tbl_remote_keys_discovery WHERE id = :id AND listType = :listType")
    suspend fun remoteKeysByIdAndListType(
        id: String,
        listType: ListType
    ): RemoteKeys?

    @Query("DELETE FROM tbl_remote_keys_discovery WHERE listType = :listType")
    suspend fun clearByListType(listType: ListType)

}