package com.sunrisekcdeveloper.showtracker.ui.rcupcoming

import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie

/**
 * Wrapper for data types for a single recyclerview
 */
sealed class UiModel {
    data class MovieItem(val movie: DisplayMovie) : UiModel()
    data class CategoryHeader(val name: String) : UiModel()
}