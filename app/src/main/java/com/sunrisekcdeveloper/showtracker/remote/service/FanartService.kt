package com.sunrisekcdeveloper.showtracker.remote.service

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.sunrisekcdeveloper.showtracker.remote.source.FanartDataSource
import com.sunrisekcdeveloper.showtracker.remote.source.NetworkImageDataSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface FanartService {
    companion object {
        private const val BASE_URL = "https://webservice.fanart.tv/v3/"
        fun create(): NetworkImageDataSource {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(
                    MoshiConverterFactory.create(
                        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                    )
                )
                .build()
                .create(FanartDataSource::class.java)
        }
    }
}