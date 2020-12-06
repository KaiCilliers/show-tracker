package com.sunrisekcdeveloper.showtracker.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunrisekcdeveloper.showtracker.ui.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.FragmentProgressBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.Movie
import com.sunrisekcdeveloper.showtracker.entities.domain.SuggestionListModel
import com.sunrisekcdeveloper.showtracker.ui.components.adapters.impl.MovieSummaryAdapter
import com.sunrisekcdeveloper.showtracker.ui.components.adapters.impl.SmallPosterAdapter
import timber.log.Timber

class ProgressFragment : Fragment() {

    private val adapter by lazy {
        SmallPosterAdapter(
            object : ClickActionContract {
                override fun onClick(item: Any) {
                    Timber.d("TITLE: $item")
                    findNavController().navigate(
                        ProgressFragmentDirections.actionProgressFragmentDestToDetailFragment(
                            "FROM PROGRESS FRAGMENT"
                        )
                    )
                }
            }
        )
    }

    private val upComingAdapter by lazy {
        MovieSummaryAdapter(object : ClickActionContract {
            override fun onClick(item: Any) {
                Timber.d("SPECIAL click: $item")
            }
        })
    }

    private val dummyMovies: List<Movie> by lazy {
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

        binding.rcUpcoming.adapter = upComingAdapter
        binding.rcUpcoming.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.submit(dummyMovies)
        upComingAdapter.submit(mixeddummydata())
    }

    private fun mixeddummydata(): List<SuggestionListModel> =
        listOf(
            SuggestionListModel.HeaderItem("Today"),
            SuggestionListModel.MovieItem(Movie("Finding Nemo")),
            SuggestionListModel.MovieItem(Movie("Harry Potter")),
            SuggestionListModel.MovieItem(Movie("Deadpool")),
            SuggestionListModel.HeaderItem("Tomorrow"),
            SuggestionListModel.MovieItem(Movie("Jurassic Park")),
            SuggestionListModel.MovieItem(Movie("Forest Gump")),
            SuggestionListModel.HeaderItem("Next Week"),
            SuggestionListModel.MovieItem(Movie("Mall Cop")),
            SuggestionListModel.MovieItem(Movie("Miss Congeniality")),
            SuggestionListModel.MovieItem(Movie("Gladiator")),
            SuggestionListModel.MovieItem(Movie("Finding Dory")),
            SuggestionListModel.MovieItem(Movie("Shrek")),
            SuggestionListModel.MovieItem(Movie("Snow White"))
        )

    private fun populatedummydata(): List<Movie> =
        listOf<Movie>(
            Movie("Finding Nemo"),
            Movie("Harry Potter"),
            Movie("Deadpool"),
            Movie("Jurassic Park"),
            Movie("Forest Gump"),
            Movie("Mall Cop"),
            Movie("Miss Congeniality"),
            Movie("Gladiator"),
            Movie("Finding Dory")
        )
}