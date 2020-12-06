package com.sunrisekcdeveloper.showtracker.remote.source

import com.sunrisekcdeveloper.showtracker.entities.network.ResponseImage

interface NetworkImageDataSource {
    suspend fun poster(id: String): ResponseImage
}