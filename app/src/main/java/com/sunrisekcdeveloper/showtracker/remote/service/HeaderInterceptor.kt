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