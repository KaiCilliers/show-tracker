package com.sunrisekcdeveloper.showtracker.ui.rc

import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import timber.log.Timber

class PosterClickAction(val clickListener: (title: String) -> Unit) {
    fun onClick(vararg movie: DisplayMovie): () -> Unit = {
        Timber.d("$movie")
        clickListener(movie[0].title)
    }
}