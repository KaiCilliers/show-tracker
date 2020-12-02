package com.sunrisekcdeveloper.showtracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.databinding.FragmentMovieDetailBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DetailedMovie
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import timber.log.Timber

class DetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMovieDetailBinding.inflate(inflater)
        binding.movie = DetailedMovie(
            DisplayMovie("The Incredibles"),
            "1997",
            "16+",
            "1h58m",
            getString(R.string.movie_description_dummy),
            "Jack Black, Peter Griff.. more",
            "James Cameron"
        )
        Timber.d(arguments?.getString("dummy","2ndDef"))
        return binding.root
    }
}