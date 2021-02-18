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

package com.sunrisekcdeveloper.showtracker.features.detail.data.network

import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseImages
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.models.network.full.ResponseFullMovie
import retrofit2.Response

interface DetailServiceContract {
    /**
     * Movies related to a specific movie
     * Supports Pagination, Extended Info
     *
     * @param id
     * @return
     */
    suspend fun moviesRelatedTo(id: String): Response<List<ResponseMovie>>
    suspend fun movieFull(id: String, extended: String = ""): Response<ResponseFullMovie>
    /** IMAGES */
    suspend fun poster(id: String): Response<ResponseImages>
}