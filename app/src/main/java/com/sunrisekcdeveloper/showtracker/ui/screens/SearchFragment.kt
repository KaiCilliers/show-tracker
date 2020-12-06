package com.sunrisekcdeveloper.showtracker.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sunrisekcdeveloper.showtracker.ui.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.FragmentSearchBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.Movie
import com.sunrisekcdeveloper.showtracker.ui.components.adapters.impl.MediumPosterAdapter
import timber.log.Timber

class SearchFragment : Fragment() {
    private val adapter by lazy {
        MediumPosterAdapter(object : ClickActionContract {
            override fun onClick(item: Any) {
                Timber.d("Search Filter: $item")
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentDestToDetailFragment(
                        "FROM SEARCH FRAGMENT"
                    )
                )
            }
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

    private fun fakedata(): List<Movie> = listOf(
        Movie("Finding Nemo"),
        Movie("Harry Potter"),
        Movie("Deadpool"),
        Movie("Jurassic Park"),
        Movie("Forest Gump"),
        Movie("Mall Cop"),
        Movie("Miss Congeniality"),
        Movie("Gladiator"),
        Movie("Finding Dory"),
        Movie("Finding Nemo"),
        Movie("Harry Potter"),
        Movie("Deadpool"),
        Movie("Jurassic Park"),
        Movie("Forest Gump"),
        Movie("Mall Cop"),
        Movie("Miss Congeniality"),
        Movie("Gladiator"),
        Movie("Finding Dory"),
        Movie("Finding Nemo"),
        Movie("Harry Potter"),
        Movie("Deadpool"),
        Movie("Jurassic Park"),
        Movie("Forest Gump")
    )
}