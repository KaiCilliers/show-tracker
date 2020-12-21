/*
 * Copyright © 2020. The Android Open Source Project
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

package com.sunrisekcdeveloper.showtracker.data.network

import com.sunrisekcdeveloper.showtracker.data.network.model.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.data.network.model.envelopes.*
import com.sunrisekcdeveloper.showtracker.util.datastate.Resource
import retrofit2.Response

interface NetworkDataSourceContract {
    suspend fun search(type: String, searchText: String, field: String = ""): Resource<*>
    suspend fun fetchBox(): Resource<List<EnvelopeRevenue>>
    suspend fun fetchTrend(): Resource<List<EnvelopeWatchers>>
    suspend fun fetchPop(): Resource<List<ResponseMovie>>
    suspend fun fetchMostPlayed(): Resource<List<EnvelopeViewStats>>
    suspend fun fetchMostWatched(): Resource<List<EnvelopeViewStats>>
    suspend fun fetchAnticipated(): Resource<List<EnvelopeListCount>>
    suspend fun fetchRecommended(): Resource<List<EnvelopeUserCount>>
}