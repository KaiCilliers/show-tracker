package com.sunrisekcdeveloper.showtracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.databinding.FragmentMovieDetailBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DetailedMovie
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import com.sunrisekcdeveloper.showtracker.ui.rc.FilterAdapter
import com.sunrisekcdeveloper.showtracker.ui.rc.FilterMediumAdapter
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction
import timber.log.Timber

// TODO make new activity: reason: no bottom nav bar and to get back button in titlebargit
class DetailFragment : Fragment() {
    private val adapter by lazy {
        FilterMediumAdapter(PosterClickAction {
            Timber.d("DETAIL TITLE: $it")
            // TODO refresh view with new movie data and scroll to top
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentDestSelf("FROM SELF"))
        })
    }
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
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.rcMoreLikeThis.adapter = adapter
        binding.rcMoreLikeThis.layoutManager = GridLayoutManager(
            requireContext(), 3, GridLayoutManager.VERTICAL, false
        )
        binding.rcMoreLikeThis.isNestedScrollingEnabled = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.addData(fakedata())
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
        DisplayMovie("Forest Gump"),
        DisplayMovie("End"),
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
        DisplayMovie("Forest Gump"),
        DisplayMovie("End")
    )
}