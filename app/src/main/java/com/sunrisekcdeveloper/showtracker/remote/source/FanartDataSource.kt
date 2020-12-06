package com.sunrisekcdeveloper.showtracker.remote.source

import com.sunrisekcdeveloper.showtracker.entities.network.ResponseImage
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface FanartDataSource : NetworkImageDataSource{
    @Headers("Content-Type: application/json")
    @GET("movies/{id}?api_key=117256e8a6a5795b8dff651216120a8e")
    override suspend fun poster(@Path("id") id: String): ResponseImage
}