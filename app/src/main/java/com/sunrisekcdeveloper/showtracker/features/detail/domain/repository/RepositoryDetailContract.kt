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

import com.sunrisekcdeveloper.showtracker.common.util.Resource
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelMovieDetail
import com.sunrisekcdeveloper.showtracker.features.detail.domain.model.UIModelShowDetail
import kotlinx.coroutines.flow.Flow

// todo interface is too demanding with its amount of methods
interface RepositoryDetailContract {
    suspend fun movieDetails(id: String): Flow<Resource<UIModelMovieDetail>>
    suspend fun showDetails(id: String): Flow<Resource<UIModelShowDetail>>
    suspend fun addMovie(id: String)
    suspend fun fetchAndSaveMovieDetails(id: String)
    suspend fun watchMovie(id: String)
    suspend fun unwatchMovie(id: String)
    suspend fun removeMovie(id: String)
    suspend fun fetchAndSaveShowDetails(id: String)
    suspend fun addShow(id: String)
    suspend fun removeShow(id: String)
}