package com.sunrisekcdeveloper.showtracker.entities.domain

/**
 * Wrapper for data types for a single recyclerview
 */
sealed class SuggestionListModel {
    data class MovieItem(val movie: Movie) : SuggestionListModel()
    data class HeaderItem(val name: String) : SuggestionListModel()
}