package com.sunrisekcdeveloper.showtracker.remote.source

import com.sunrisekcdeveloper.showtracker.entities.wrapper.ImageResponse

interface NetworkImageDataSource {
    suspend fun poster(id: String): ImageResponse
}