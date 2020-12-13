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

package com.sunrisekcdeveloper.showtracker.remote.source

import com.sunrisekcdeveloper.showtracker.BuildConfig
import com.sunrisekcdeveloper.showtracker.entities.network.base.ResponseMovieImages
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface FanartDataSource : NetworkImageDataSource{
    @Headers("Content-Type: application/json")
    @GET("movies/{id}?api_key=${BuildConfig.FANART_API_KEY}")
    override suspend fun poster(@Path("id") id: String): ResponseMovieImages
}