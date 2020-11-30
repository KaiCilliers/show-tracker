package com.sunrisekcdeveloper.showtracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.databinding.FragmentProgressBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import com.sunrisekcdeveloper.showtracker.ui.rc.FilterAdapter
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction
import com.sunrisekcdeveloper.showtracker.util.click
import timber.log.Timber

class ProgressFragment : Fragment() {

    private val adapter by lazy {
        FilterAdapter(
            PosterClickAction { title ->
                Timber.d("TITLE: $title")
            }
        )
    }
    private val dummyMovies: List<DisplayMovie> by lazy {
        populatedummydata()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentProgressBinding.inflate(inflater)
        binding.rcFilterOptions.adapter = adapter
        binding.rcFilterOptions.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.addData(dummyMovies)
    }

    private fun populatedummydata(): List<DisplayMovie> =
        listOf<DisplayMovie>(
            DisplayMovie("Finding Nemo"),
            DisplayMovie("Harry Potter"),
            DisplayMovie("Deadpool"),
            DisplayMovie("Jurassic Park"),
            DisplayMovie("Forest Gump"),
            DisplayMovie("Mall Cop"),
            DisplayMovie("Miss Congeniality"),
            DisplayMovie("Gladiator"),
            DisplayMovie("Finding Dory")
        )
}