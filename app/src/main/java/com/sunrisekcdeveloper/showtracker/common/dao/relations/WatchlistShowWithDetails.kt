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

package com.sunrisekcdeveloper.showtracker.common.dao.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.sunrisekcdeveloper.cache.models.EntityShow
import com.sunrisekcdeveloper.cache.models.EntityWatchlistShow

// todo extract
data class WatchlistShowWithDetails(
    @Embedded val status: EntityWatchlistShow,
    @Relation(
        parentColumn = "watch_show_id",
        entityColumn = "show_id"
    )
    val details : EntityShow
)