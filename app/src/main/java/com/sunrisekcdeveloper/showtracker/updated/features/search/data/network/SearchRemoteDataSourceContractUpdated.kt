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

package com.sunrisekcdeveloper.showtracker.updated.features.search.data.network

import com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.network.NetworkResult
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.network.model.EnvelopePaginatedMovieUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.data.network.model.EnvelopePaginatedShowUpdated

interface SearchRemoteDataSourceContractUpdated {
    suspend fun moviesByTitle(query: String, page: Int) : NetworkResult<EnvelopePaginatedMovieUpdated>
    suspend fun showsByTitle(query: String, page: Int) : NetworkResult<EnvelopePaginatedShowUpdated>
}