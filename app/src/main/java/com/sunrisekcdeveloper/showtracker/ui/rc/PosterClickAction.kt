package com.sunrisekcdeveloper.showtracker.ui.rc

import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie

class PosterClickAction(val clickListener: (title: String) -> Unit) {
    fun onClick(movie: DisplayMovie) = clickListener(movie.title)
}