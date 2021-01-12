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

package com.sunrisekcdeveloper.showtracker.features.watchlist.client

import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.NetworkModule.WatchlistApi
import retrofit2.Response
import timber.log.Timber

class WatchlistClient(
    @WatchlistApi private val api: WatchlistServiceContract
) : WatchlistDataSourceContract {
    private suspend fun <T> result(request: suspend () -> Response<T>): Resource<T> {
        return try {
            val response = request()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return Resource.Success(it)
                }
            }
            error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            error(e.message ?: e.toString(), e)
        }
    }
    private fun <T> error(message: String, e: Exception): Resource<T> {
        Timber.e(message)
        return Resource.Error("Network call has failed for the following reason: $message")
    }
}

