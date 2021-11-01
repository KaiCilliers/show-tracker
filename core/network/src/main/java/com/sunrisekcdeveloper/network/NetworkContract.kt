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

package com.sunrisekcdeveloper.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

interface NetworkContract {
    suspend fun <T> request(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        request: suspend () -> Response<T>) : NetworkResult<T>

    class Impl : NetworkContract {
        override suspend fun <T> request(
            dispatcher: CoroutineDispatcher,
            request: suspend () -> Response<T>
        ): NetworkResult<T> {
            return withContext(dispatcher) {
                return@withContext try {
                    val response = request()
                    if (response.isSuccessful) {
                        response.body()?.let { return@withContext NetworkResult.success(it) }
                    }
                    NetworkResult.error("Code: ${response.code()} | ${response.errorBody()}")
                } catch (e: Exception) {
                    NetworkResult.error(e.message ?: e.toString())
                }
            }
        }
    }
}