package com.sunrisekcdeveloper.showtracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sunrisekcdeveloper.showtracker.databinding.FragmentSearchBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import com.sunrisekcdeveloper.showtracker.ui.rc.FilterMediumAdapter
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction
import timber.log.Timber

class SearchFragment : Fragment() {
    private val adapter by lazy {
        FilterMediumAdapter(PosterClickAction { title ->
            Timber.d("Search Filter: $title")
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentDestToDetailFragment("FROM SEARCH FRAGMENT"))
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchBinding.inflate(inflater)
        binding.rcSearchResults.adapter = adapter
        binding.rcSearchResults.layoutManager = GridLayoutManager(
            requireContext(), 3, GridLayoutManager.VERTICAL, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.submit(fakedata())
    }

    private fun fakedata(): List<DisplayMovie> = listOf(
        DisplayMovie("Finding Nemo"),
        DisplayMovie("Harry Potter"),
        DisplayMovie("Deadpool"),
        DisplayMovie("Jurassic Park"),
        DisplayMovie("Forest Gump"),
        DisplayMovie("Mall Cop"),
        DisplayMovie("Miss Congeniality"),
        DisplayMovie("Gladiator"),
        DisplayMovie("Finding Dory"),
        DisplayMovie("Finding Nemo"),
        DisplayMovie("Harry Potter"),
        DisplayMovie("Deadpool"),
        DisplayMovie("Jurassic Park"),
        DisplayMovie("Forest Gump"),
        DisplayMovie("Mall Cop"),
        DisplayMovie("Miss Congeniality"),
        DisplayMovie("Gladiator"),
        DisplayMovie("Finding Dory"),
        DisplayMovie("Finding Nemo"),
        DisplayMovie("Harry Potter"),
        DisplayMovie("Deadpool"),
        DisplayMovie("Jurassic Park"),
        DisplayMovie("Forest Gump")
    )
}