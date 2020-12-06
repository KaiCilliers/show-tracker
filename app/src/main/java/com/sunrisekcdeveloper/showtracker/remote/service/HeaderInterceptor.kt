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

package com.sunrisekcdeveloper.showtracker.remote.service

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    /**
     * Interceptor class for setting of the headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().apply {
            newBuilder()
                .addHeader("Content-type", "application/json")
                .addHeader("trakt-api-key", "62845b4c84daa248ede22b78b90b5b13cc7d7dc39830d2eb408bd1d54ca55db1")
                .addHeader("trakt-api-version", "2")
                .build()
        }
        return chain.proceed(request)
    }
}