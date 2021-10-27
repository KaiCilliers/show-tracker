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
import kotlin.coroutines.RestrictsSuspension

open class RemoteDataSourceBase(
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun <T> safeApiCall(request: suspend () -> Response<T>): NetworkResult<T> =
        withContext(dispatcher) {
            return@withContext try {
                val response = request()
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let { data ->
                        return@withContext NetworkResult.success(data)
                    }
                }
                NetworkResult.error("Code: ${response.code()} | ${response.errorBody()}")
            } catch (e: Exception) {
                NetworkResult.error((e.message ?: e.toString()))
            }
        }
}

interface MNetwork{
    suspend fun <T> request(dispatcher: CoroutineDispatcher = Dispatchers.IO, request: suspend () -> Response<T>): NetworkResult<T>
}

class MIMple : MNetwork {
    override suspend fun <T> request(dispatcher: CoroutineDispatcher, request: suspend () -> Response<T>): NetworkResult<T> {
        return withContext(dispatcher) {
            return@withContext try {
                val response = request()
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let { data ->
                        return@withContext NetworkResult.success(data)
                    }
                }
                NetworkResult.error("Code: ${response.code()} | ${response.errorBody()}")
            } catch (e: Exception) {
                NetworkResult.error((e.message ?: e.toString()))
            }
        }
    }
}